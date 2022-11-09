import pandas as pd

path = "data/schema_mapping/integrated_target_schema_datasets_xlsx/"
dataset_pairs_dict = {"A":"B", "A":"D", "B":"C", "C":"D", "C":"E"}
sample_size = 150

for dataset_one, dataset_two in dataset_pairs_dict.items():
    df_one = pd.read_excel(path + "Dataset_" + dataset_one + ".xlsx")
    df_two = pd.read_excel(path + "Dataset_" + dataset_two + ".xlsx")
    df_one_sample = df_one.sample(n=sample_size).reset_index()
    df_two_sample = df_two.sample(n=sample_size).reset_index()
    df_random_occs = pd.concat([df_one_sample, df_two_sample], axis=1)
    
    #df_random_occs.to_csv(path + dataset_one + "-" + dataset_two + ".csv", index=False)
    print(df_random_occs.head())

print("done")

