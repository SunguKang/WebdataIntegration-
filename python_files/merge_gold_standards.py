import pandas as pd
import os.path
import re
path = "data/gold_standard/"
dataset_names = ["A-B", "A-D", "B-C", "C-D", "C-E"]

postfix_ls = [["sunggu", ","], ["1of5", ";"], ["florian", ";"], ["jiyeon", ","], ["lara", ","]]

def get_string_match(s:str):
    if isinstance(s, str):
        result = re.search(r"^[a-zA-Z0-9_]+[0-9]+", s)
        if result != None:
            return result.group(0)
    return None

for dataset_name in dataset_names:
    tmp_df = pd.DataFrame()
    for postfix in postfix_ls:
        dir_path = path + "gold_standard_"  + postfix[0] + "/"
        filepath = dir_path + dataset_name
        if os.path.isfile(filepath + ".csv"): 
            sep = postfix[1]
            read_df =pd.read_csv(filepath + ".csv", header=None, sep=sep, quotechar='"')
            print("read file {}".format(filepath))
        elif os.path.isfile(filepath + ".xlsx"): 
            read_df = pd.read_excel(filepath + ".xlsx", header=None)
            print("read file {}".format(filepath))
        print(read_df.head())
        tmp_df = pd.concat([tmp_df, read_df])

    for i in range(2, len(tmp_df.columns)):
        print(i)
        col = tmp_df.columns[i] 
        tmp_df[col] = tmp_df[col].apply(str).apply(lambda x : re.sub(r"TRUE|True|1.0|^1$", "True", x))
        tmp_df[col] = tmp_df[col].apply(str).apply(lambda x: re.sub(r"FALSE|False|0.0|^0$", "False", x))
    tmp_df = tmp_df.dropna(subset=tmp_df.columns[0])
    for i in range(2):
        col = tmp_df.columns[i]
        tmp_df[col] = tmp_df[col].str.replace(r"C:\\Users\\flola\\IONOS HiD", "wikidata_id_")
        tmp_df[col] = tmp_df[col].apply(lambda x: get_string_match(x))
    print(tmp_df.head())
    tmp_df.to_csv(path + "merged/" + dataset_name + ".csv", index=False)

print("done")