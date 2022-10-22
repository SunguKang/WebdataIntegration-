import json
import pandas as pd 

files=['wikidata_video_games_X-2000_simple.json', 'wikidata_video_games_2000-2010_simple.json',
        'wikidata_video_games_2010-2020_simple.json', 'wikidata_video_games_2020-X_simple.json']
path = 'C:/Users/flola/IONOS HiDrive/Studium Master/Web Data Integration/Project/'

# def merge_JsonFiles(files):
#     result = list()
#     for file in files:
#         filepath = path + file
#         with open(filepath, 'r') as infile:
#             result.extend(json.load(infile))
#
#    with open('wikidata_video_games_full.json', 'w') as output_file:
#       json.dump(result, output_file)

df_list = []

for filename in files:
    filepath = path + filename
    with open(filepath, encoding='utf-8') as f:                  
        data = json.load(f)
    
    df = pd.DataFrame(data)  
    df_list.append(df)
    
merged_df = pd.concat(df_list, axis=0)  
merged_df = merged_df.reset_index()      

merged_df.to_json("wikidata_video_games_complete.json")
merged_df.to_csv("wikidata_video_games_complete.csv")

