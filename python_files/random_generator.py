import random
import pandas as pd


lower_bound = 3800
upper_bound = 7600
no_generated = 200
random_generated_set = set()


while len(random_generated_set) < no_generated:
    no = random.randrange(lower_bound+1, upper_bound+2)
    random_generated_set.add(no)
    print("generated number: {}".format(no))


random_generated_list = sorted(list(random_generated_set))
print("length is: {}".format(len(random_generated_list)))
for elem in random_generated_list:
    print(elem)


filename = "Dataset_C.xlsx"
path = "integrated_target_schema/"
filepath = path + filename

df = pd.read_excel(filepath)
#subset_df = df.iloc[random_generated_list]
#mask = subset_df["platform"].str.contains('PlayStation')
#subset_df = subset_df.loc[mask, :]

df = df.loc[df["platform"] == "PC"]
subset_df = df.sample(n=no_generated)

subset_df.to_excel("tmp/subset_florian_only_PC_" + filename, index=False)

print("done")