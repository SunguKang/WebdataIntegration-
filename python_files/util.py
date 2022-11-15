
import random
import pandas as pd
import numpy as np

gold_path = r"../data/gold_standard/gold_standard_leon"
compare = [["A","B"],["A","D"],["B","C"],["C","D"],["C","E"]]
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
##creation of convinience files

def combine_samples_from_files():
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
                        
def look_up_matches_gs():
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