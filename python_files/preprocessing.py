import csv
import math
import os
import re
import string 
from datetime import datetime
from xml.etree import ElementTree

import pandas as pd
import numpy as np 

#path to folder with 
path_xml = r".\data\schema_mapping\integrated_target_schema_xml".replace("\\","/")
path_mapping = r"./data/schema_mapping"
path_schema_csv = r"./data/schema_mapping/integrated_target_schema_csv"
gold_path = r"./data/gold_standard/gold_standard_leon"
#the paths to the original .csv datasets  

paths = [r"integrated_target_schema_Windows.csv"
,r"integrated_target_schemaPS4.csv"
,r"target_schema_metacritic.csv"
,r"target_schema_Video_Games_Sales.csv"
,r"wikidata_integrated_target_schema.csv"]

#path to godl folder
pre_pro_path = r"./data/preprocessing".replace("\\","/")

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

def lower_text(text):
    #function that lower the text
    text=text.lower()
    return text 

def remove_punctuations(text):
    #function that remove punctuation
    for punctuation in string.punctuation:
        text = text.replace(punctuation, '')
    return text

def double_space(text):
    #function that remove double spaces  
    for i in range(0, len(text)):
        text= text.replace(' +', ' ')
    return text

def remove_special(text):
    #function that remove special characters
    ls=[]
    for k in text.split("\n"):
        ls.append(re.sub(r"[^a-zA-Z0-9]+", ' ', k))
    text= " ".join(ls)
    return text

def text_cleaning_run(df:pd.DataFrame, column_ls:list):
    #function that runs all cleaning functions
    for col in column_ls:
        df[col + '_preprocessed'] = df[col].apply(lower_text).apply(remove_punctuations).apply(double_space).apply(remove_special)
    return df

def format_date(s:str):
    #format string as datetime type
    if type(s) != str and (math.isnan(s) or s is None):
        return np.nan
    else:
        try: 
            return datetime.strptime(s, '%Y-%m-%d')
        except ValueError as e:
            if "TBA" in s or "N/A" in s:
                    return np.nan
            try: 
                return datetime.strptime(s, '%B %d, %Y')
            except:
                print("uncaught: {}".format(s))
                return np.nan
    

def xml_to_csv(path_source_folder, path_target_folder = "", attributes = ['id','name','platform','publishers','publicationDate',
      'globallySoldUnits','genres','criticScore','userScore',
      'developers','summary','rating','series'], list_att= ["publishers","genres","developers"] ):
        '''
        function that creates .csv files from .xml files
        
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

#find all platform names in dataset
#all_platforms = [set(pd.read_csv(p)["platform"]) for p in paths.values()]
#export them
#pd.DataFrame(all_platforms).to_csv(path_to_folder+ "/" + "platforms"+".csv",index=False, sep=";")  
#reimport them (after they were edited and matched by hand)
platforms = pd.read_csv(pre_pro_path+"/platforms2.csv", sep =";", index_col=None)

unified_platforms_column = 2

#select only the ones in c (imputed for the ones in a)
for col in platforms.columns:
    platforms[col] = platforms[col].apply(lambda x: x.split(',') if type(x) == str else x)

#change platform names, only keep the ones present in A and C (platforms[3])
#for all datesets
for i in range(len(names)):
    dataset_name = names[i]
    #save integrated schemas also as Dataset_X.csv (make sure the subfolder "integratet_target_schema_csv" exists)
    data_df = pd.read_csv(paths[dataset_name])
    data_df.to_csv(path_schema_csv+"/Dataset_"+ dataset_name +".csv", sep=";", index=False)
    #data_df = pd.read_csv(paths[dataset_name])
    #for all platform names
    for j, row in platforms.iterrows():
        #if the platform is in this dataframe
        
        if type(row[i]) == list and type(row[unified_platforms_column]) == list:
            unified_platforms_cur = row[unified_platforms_column]
            cur_platform = row[i][0]
            #if cur_platform in truth_platforms_cur:
            #find matching platform name and replace with entry from dataset C
            print("platform: {}".format(row[i]))
            selection = data_df.loc[data_df["platform"] == cur_platform, :] 
            
            data_df.loc[data_df["platform"] == cur_platform, 'platform'] = unified_platforms_cur[0]
                #data_df["platform"] = np.where(data_df["platform"].isin(platforms.iloc[j,i]),platforms.iloc[j,truth_platforms_column],data_df["platform"])
    
    #delete from dataset5
    if("E"== dataset_name):
        p = [item for sublist in platforms.iloc[:,[unified_platforms_column]].dropna().values for item in sublist]
        p = [item for sublist in p for item in sublist]
        data_df = data_df[data_df["platform"].isin(p)]

    #############################################################
    # TODO insert other preprocessing here 
    #preprocess data to the data type
    data_df["publicationDate"] = data_df["publicationDate"].apply(lambda x: format_date(x))
    data_df["publicationDate"] = pd.to_datetime(data_df['publicationDate'], errors='coerce').dt.strftime('%Y-%m-%d')
                        
    #Run the text cleaning functions(df need to be given)
    data_df = text_cleaning_run(data_df, ["name"])
    
    #strip leading and trailing spaces
    data_df_obj = data_df.select_dtypes(['object'])
    data_df[data_df_obj.columns] = data_df_obj.apply(lambda x: x.str.strip())
    
    ##############################################################
    #save preprocessed data again
    data_df.to_csv(pre_pro_path+"/preprocessed_csv_files/Dataset_"+ dataset_name + ".csv", index=False, sep=";")
    data_df.to_xml(pre_pro_path+"/preprocessed_csv_files/Dataset_"+ dataset_name + ".xml", index=False, elem_cols=["publishers","genres","developers"])







