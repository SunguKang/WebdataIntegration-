#import libraries
import csv
import math
import os
import random
from xml.etree import ElementTree

import numpy as np
import pandas as pd

#path to folder with 
path_xml = r"..\data\schema_mapping\integrated_target_schema_xml".replace("\\","/")
path_mapping = r"../data/schema_mapping"
path_scema_csv = r"../data/schema_mapping/integrated_target_schema_csv"
gold_path = r"../data/gold_standard/gold_standard_leon"
#the paths to the original .csv datasets  
paths = [r"integrated_target_schema_Windows.csv"
,r"integrated_target_schemaPS4.csv"
,r"target_schema_metacritic.csv"
,r"target_schema_Video_Games_Sales.csv"
,r"wikidata_integrated_target_schema.csv"]

#path to godl folder
prePro_path = r"../data/preprocessing".replace("\\","/")
#the paths to the matches
c_paths= [r"A-B.csv",
r"A-D.csv",
r"B-C.csv",
r"C-D.csv",
r"C-E.csv"]

#preprocessing the pathnames for gold_stadard
for i in range(0,len(paths)):
    paths[i] = path_scema_csv+"/"+paths[i]

for i in range(0,len(c_paths)):
    c_paths[i] = gold_path+"/"+c_paths[i]

#Naming the paths
names = ["B","D","A","C","E"]
paths = dict(zip(names,paths))
compare = [["A","B"],["A","D"],["B","C"],["C","D"],["C","E"]]

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
xml_to_csv(path_source_folder=path_xml, path_target_folder=path_scema_csv)
#save integrated scemas also as Dataset_X.csv (make shure the subfolder "integratet_target_schema_csv" exists)
for n in names:
    csv = pd.read_csv(paths[n])
    csv.to_csv(path_scema_csv+"/Dataset_"+n+".csv", sep=";", index=False)
    

#find all platform names in dataset

#all_platforms = [set(pd.read_csv(p)["platform"]) for p in paths.values()]
#export them
#pd.DataFrame(all_platforms).to_csv(path_to_folder+ "/" + "platforms"+".csv",index=False, sep=";")  
#reimport them (after they were edited and matched by hand)
platforms = pd.read_csv(prePro_path+"/platforms.csv", sep =";", header=None)
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
                print("col:",j,"row:",i, platforms.iloc[i,j])    
    

#change platform names, only keep the ones present in A and C (platforms[3])
#for all datesets
for i in range(0,5):
    csv = pd.read_csv(paths[names[i]])
#for all platform names
    for j in range(0,len(platforms)):
    #if the platform is in this dataframe
        if type(platforms.iloc[j,i]) == list:
            #find matching platform name and replace with entrie from dataset C
            csv["platform"] = np.where(csv["platform"].isin(platforms.iloc[j,i]),platforms.iloc[j,3],csv["platform"])
    #delete from dataset5
    if(4==i):
        p = [item for sublist in platforms[3] for item in sublist]
        csv = csv[csv["platform"].isin(p)]
    #save as _uni_plat.csv
    csv.to_csv(prePro_path+"/uniform_platform_names/Dataset_"+names[i]+".csv", index=False, sep=";")


##creation of convinience files


#combine a sample of a fith with another sample (the j+1 to fith), this can be done to create nonmatches for the gold standard
#make shure, prePro_path has a subfolder "random_draws"
for j in range(5):
    for i in range(0,len(compare)):
            num = 12
            csv_1 = pd.read_csv(paths[compare[i][0]])
            csv_2 = pd.read_csv(paths[compare[i][1]])
            sample1 = csv_1.iloc[random.sample(range(j*csv_1.shape[0]//5,(((j+1)*csv_1.shape[0]//5)-1)),num)]
            sample2 = csv_2.iloc[random.sample(range(0,csv_2.shape[0]),num)]
            pd.concat([sample1.reset_index(drop=True),
                       sample2.reset_index(drop=True)],axis=1).to_csv(gold_path+
                                                                      "/../random_combinations/" +
                                                                      "-".join(compare[i]) +
                                                                      "_"+str(num)+"_random_combinations"+
                                                                      str(j+1)+"_fith.csv",
                                                                      index=False, sep =";")  
#look up found matches from goldstandard files and put their rows in a table
for i in range(0,len(c_paths)):               
    csv_1 = pd.read_csv(paths[compare[i][0]])
    csv_2 = pd.read_csv(paths[compare[i][1]])
    compare_csv =  pd.read_csv(c_paths[i],sep=";")
    merged_csv = pd.DataFrame(np.zeros((0,0)))
    for j in range(0,len(compare_csv)):
        temprow1 = csv_1[compare_csv.iloc[j][0] == csv_1["id"]]
        temprow2 = csv_2[compare_csv.iloc[j][1] == csv_2["id"]]
        merged =pd.concat([temprow1.reset_index(drop=True),temprow2.reset_index(drop=True)], axis=1)
        merged_csv = pd.concat([merged_csv,merged], axis=0)
    #save as dataset1-dataset2_new.csv
    merged_csv.to_csv(c_paths[i].replace(r".csv", "_full_rows.csv"), index=False, sep=";")