
import pandas as pd
import csv
import math
import os
import re
import string 
from datetime import datetime
from xml.etree import ElementTree
from lxml import etree
import numpy as np 

folder = "C:/Users/flola\IONOS HiDrive\Studium Master\Web Data Integration\Project\WebdataIntegration-\data/fused/"
path_xml="fused.xml"
path_csv = "data/fused/fused_converted"



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
                for i, videogame in enumerate(xml.findall("game")):
                    if i == 37:
                        print("test")
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
                                        sub_list = [str(elem) for elem in sub_list]
                                        csv_line.append("-".join(sub_list))
                                    else: 
                                        s = locals()[a].text
                                        csv_line.append(s)
                                else:
                                    csv_line.append("")
                                    #print("line after append:",csv_line)
                        # ADD A NEW ROW TO CSV FILE
                        #csvfile_writer.writerow(csv_line)
                        writer.writerow(csv_line)
                        #print("final line:",csv_line)
                #csvfile.close()


xml_to_csv(path_source_folder=folder, path_target_folder=path_csv)


fused_df = pd.read_csv(path_csv + "/fused.csv")

for col in ["userScore", "criticScore", "globallySoldUnits"]:
    fused_df[col] = fused_df[col].replace(['NaN'], np.nan)

print("consistency for columns in full dataset fused")
print("number of entries: {}".format(fused_df.shape[0]))
print("column | density")
for col in fused_df.columns:
    density = (fused_df.shape[0] - fused_df[col].isna().sum()) / fused_df.shape[0]
    print("{} | \t  {}".format(col, density))

print("done")
fused_df.to_csv("tmp.csv")
reloaded = pd.read_csv("tmp.csv")

print("done2")