
import pandas as pd
import numpy as np 

import csv
import math
import os
import re
import string 
from xml.etree import ElementTree

#path to folder with 
path_xml = r"..\data\schema_mapping\integrated_target_schema_xml".replace("\\","/")
path_mapping = r"../data/schema_mapping"
path_schema_csv = r"../data/schema_mapping/integrated_target_schema_csv"
gold_path = r"../data/gold_standard/gold_standard_leon"
#the paths to the original .csv datasets  

paths = [r"integrated_target_schema_Windows.csv"
,r"integrated_target_schemaPS4.csv"
,r"target_schema_metacritic.csv"
,r"target_schema_Video_Games_Sales.csv"
,r"wikidata_integrated_target_schema.csv"]

#path to godl folder
pre_pro_path = r"../data/preprocessing".replace("\\","/")

#the paths to the matches
c_paths= [r"A-B.csv",
r"A-D.csv",
r"B-C.csv",
r"C-D.csv",
r"C-E.csv"]

#preprocessing the pathnames for gold_stadard
for i in range(0,len(paths)):
    paths[i] = path_schema_csv+"/"+paths[i]

for i in range(0,len(c_paths)):
    c_paths[i] = gold_path+"/"+c_paths[i]

#Naming the paths
names = ["B","D","A","C","E"]
paths = dict(zip(names,paths))
compare = [["A","B"],["A","D"],["B","C"],["C","D"],["C","E"]]

#function that lower the text
def lower_text(text):
    text=text.lower()
    return text 

#function that remove punctuations
def remove_punctuations(text):
    for punctuation in string.punctuation:
        text = text.replace(punctuation, '')
    return text

#function that remove double spaces   
def double_space(text):
    for i in range(0, len(text)):
        text= text.replace(' +', ' ')
    return text

#function that remove special characters 
def remove_special(text):
    ls=[]
    for k in text.split("\n"):
        ls.append(re.sub(r"[^a-zA-Z0-9]+", ' ', k))
    text= " ".join(ls)
    return text

#function that runs all cleaning functions
def text_cleaning_run(df:pd.DataFrame, column_ls:list):
    for col in column_ls:
        df[col + '_preprocessed'] = df[col].apply(lower_text).apply(remove_punctuations).apply(double_space).apply(remove_special)
    return df
    

        


#function that creates .csv files from .xml files
def xml_to_csv(path_source_folder, path_target_folder = "", attributes = ['id','name','platform','publishers','publicationDate',
      'globallySoldUnits','genres','criticScore','userScore',
      'developers','summary','rating','series'], list_att= ["publishers","genres","developers"] ):
        '''
        path_source_folder : is the path to the folder with the .xml files
        attributes : list of names of the attributes in the .xml files
        list_att : list of names of the attributes in attributes which are list attributes 
        '''
        path_source_folder = path_source_folder.replace("\\","/")
        names = [name for name in os.listdir(path_source_folder) if ".xml" in name]
        paths = [path_source_folder+"/"+name for name in names]

        if (""==path_target_folder):
            path_target_folder = path_source_folder

        for i,f in enumerate(paths):
            # PARSE XML
            #print(f)
            xml = ElementTree.parse(f)

            # CREATE CSV FILE
            with open(path_target_folder + "/" + names[i].replace(".xml",".csv"), 'w', newline='',encoding="utf-8") as outfile:
                writer = csv.writer(outfile)
                #csvfile = open(f.replace(".xml",".csv"),'w',encoding='utf-8')
                #csvfile_writer = csv.writer(csvfile)

                # ADD THE HEADER TO CSV FILE
                #csvfile_writer.writerow(["id","name"])

                writer.writerow(attributes)

                # FOR EACH EMPLOYEE
                for i, videogame in enumerate(xml.findall("videogame")):
                    if(videogame):
                        csv_line = list()
                        for a in attributes:
                                locals()[a] = videogame.find(a)
                                #print("bool",a,":", None != locals()[a])
                                if (None != locals()[a]):
                                    if (a in list_att):
                                        sub_list = list()
                                        for c1 in locals()[a].findall("*"):
                                            for c2 in c1.findall("*"):
                                                sub_list.append(c2.text)
                                        csv_line.append(",".join(sub_list))
                                    else: 
                                        csv_line.append(locals()[a].text)
                                else:
                                    csv_line.append("")
                                    #print("line after append:",csv_line)
                        # ADD A NEW ROW TO CSV FILE
                        #csvfile_writer.writerow(csv_line)
                        writer.writerow(csv_line)
                        #print("final line:",csv_line)
                #csvfile.close()
                
#create .csv Files from the .xml files using the function
xml_to_csv(path_source_folder=path_xml, path_target_folder=path_schema_csv)
#save integrated scemas also as Dataset_X.csv (make shure the subfolder "integratet_target_schema_csv" exists)
for n in names:
    csv = pd.read_csv(paths[n])
    csv.to_csv(path_schema_csv+"/Dataset_"+n+".csv", sep=";", index=False)
    

#find all platform names in dataset

#all_platforms = [set(pd.read_csv(p)["platform"]) for p in paths.values()]
#export them
#pd.DataFrame(all_platforms).to_csv(path_to_folder+ "/" + "platforms"+".csv",index=False, sep=";")  
#reimport them (after they were edited and matched by hand)
platforms = pd.read_csv(pre_pro_path+"/platforms.csv", sep =";", header=None)

#transpose
platforms = platforms.transpose()
#select ony the ones in c (imputed for the ones in a)
platforms = platforms.loc[~platforms.isnull()[3],range(0,5)]
#drop the first row
platforms = platforms.drop(0)
#replace strings in with string lists
for j in range (0,len(platforms.columns)):
    for i in range(0,len(platforms)):
        try:
            platforms.iloc[i,j] = platforms.iloc[i,j].split(",")
        except AttributeError:
            if not math.isnan(platforms.iloc[i,j]):
                print("ERROR was not detected as string, col:",j,"row:",i, platforms.iloc[i,j])    
    

#change platform names, only keep the ones present in A and C (platforms[3])
#for all datesets
for i in range(len(names)):
    data_csv = pd.read_csv(paths[names[i]])
    #for all platform names
    for j in range(0,len(platforms)):
    #if the platform is in this dataframe
        if type(platforms.iloc[j,i]) == list:
            #find matching platform name and replace with entry from dataset C
            data_csv["platform"] = np.where(data_csv["platform"].isin(platforms.iloc[j,i]),platforms.iloc[j,3],data_csv["platform"])
    #delete from dataset5
    if("E"== names[i]):
        p = [item for sublist in platforms[3] for item in sublist]
        cdata_csvsv = data_csv[data_csv["platform"].isin(p)]
    #save as _uni_plat.csv
    #############################################################
    # TODO insert other preprocessing here 
    
    #Run the text cleaning functions(df need to be given)
    data_csv = text_cleaning_run(data_csv, ["name"])
    ##############################################################
    data_csv.to_csv(pre_pro_path+"/uniform_platform_names/Dataset_"+names[i]+".csv", index=False, sep=";")




