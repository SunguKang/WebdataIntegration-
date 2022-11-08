import pandas as pd

targetpath = "integrated_target_schema\Dataset_E.xlsx"
sourcepath = "integrated_target_schema\wikidata_integrated_target_schema.xml"

df = pd.read_xml(sourcepath) 

print(df.head())

df.to_excel(targetpath, index=False)

print("done")
