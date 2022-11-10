from xml.etree import ElementTree
import csv
import os
import re


def xml_to_csv(path_to_folder, attributes = ['id','name','platform','publishers','publicationDate',
      'globallySoldUnits','genres','criticScore','userScore',
      'developers','summary','rating','series'], list_att= ["publishers","genres","developers"] ):
        '''
        path_to_folder : is the path to the folder with the .xml files
        attributes : list of names of the attributes in the .xml files
        list_att : list of names of the attributes in attributes which are list attributes 
        '''
        path_to_folder = path_to_folder.replace("\\","/")
        paths = [path_to_folder+"/"+name for name in os.listdir(path_to_folder) if ".xml" in name]

        for f in paths:
            # PARSE XML
            #print(f)
            xml = ElementTree.parse(f)

            # CREATE CSV FILE
            with open(f.replace(".xml",".csv"), 'w', newline='',encoding="utf-8") as outfile:
                writer = csv.writer(outfile)
                #csvfile = open(f.replace(".xml",".csv"),'w',encoding='utf-8')
                #csvfile_writer = csv.writer(csvfile)

                # ADD THE HEADER TO CSV FILE
                #csvfile_writer.writerow(["id","name"])

                writer.writerow(attributes)

                # FOR EACH
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