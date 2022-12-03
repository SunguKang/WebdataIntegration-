package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFusionLogger;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.RecordCSVFormatter;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Record;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Defines which fuser should be applied and which evaluation rules should be
 * used during data fusion process.
 *
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 * @param <RecordType>	the type that represents a record
 */
public class GameDataFusionStrategy<RecordType extends Matchable, SchemaElementType> extends DataFusionStrategy {

    private static final Logger logger = WinterLogManager.getLogger();
    private String filePathDebugResults;

    public GameDataFusionStrategy(FusibleFactory factory) {
        super(factory);

    }

    @Override
    public void activateDebugReport(String filePath, int maxSize, DataSet goldStandard){
        if(filePath != null){
            super.activateDebugReport(filePath,maxSize, goldStandard);
            this.filePathDebugResults = filePath;

        }
    }

    @Override
    protected void calculateRecordLevelDebugResultsAndWriteToFile(FusibleDataSet fusedDataSet){
        HashedDataSet<Record, Attribute> debugFusionResults = super.getDebugFusionResults();
        if(debugFusionResults != null) {
            FusibleHashedDataSet<Record, Attribute> debugFusionResultsRecordLevel = new FusibleHashedDataSet<Record, de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute>();
            List<Attribute> headerDebugResultsRecordLevel = new LinkedList<Attribute>();

            // Initialise Attributes
            de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute attributeRecordIDS = new de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute("RecordIDS");
            debugFusionResultsRecordLevel.addAttribute(attributeRecordIDS);
            headerDebugResultsRecordLevel.add(attributeRecordIDS);

            de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute attributeAvgConsistency = new de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute("AverageConsistency");
            debugFusionResultsRecordLevel.addAttribute(attributeAvgConsistency);
            headerDebugResultsRecordLevel.add(attributeAvgConsistency);

            Set<String> attributeSet = new HashSet<String>();
            HashMap<String, Attribute> attributeHashMap = new HashMap<String, de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute>();
            Set<String> recordsIDSet = new HashSet<String>();

            for (Record record : debugFusionResults.get()){
                String attributeName = record.getValue(AttributeFusionLogger.ATTRIBUTE_NAME);
                if(!attributeSet.contains(attributeName)){
                    attributeSet.add(attributeName);
                    de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute attributeConsistency = new de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute(attributeName + "-Consistency");
                    debugFusionResultsRecordLevel.addAttribute(attributeConsistency);
                    headerDebugResultsRecordLevel.add(attributeConsistency);
                    attributeHashMap.put(attributeName + "-Consistency", attributeConsistency);

                    de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute attributeValues = new de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute(attributeName + "-Values");
                    debugFusionResultsRecordLevel.addAttribute(attributeValues);
                    headerDebugResultsRecordLevel.add(attributeValues);
                    attributeHashMap.put(attributeName + "-Values", attributeValues);
                }
                recordsIDSet.add(record.getValue(AttributeFusionLogger.RECORDIDS));
            }

            // Generate Record Level Debug Record
            for (String recordIDs: recordsIDSet){

                //Use original ID to initialize new debug record with full list of identifiers
                String [] originalIDS = recordIDs.split("\\+");
                RecordType fusedRecord = (RecordType) fusedDataSet.getRecord(originalIDS[0]);
                try {
                    String fusedRecordIdentifier = fusedRecord.getIdentifier();

                    Record record = debugFusionResultsRecordLevel.getRecord(fusedRecordIdentifier);
                    if (record == null){
                        record = new Record(fusedRecord.getIdentifier());
                        record.setValue(attributeRecordIDS, fusedRecord.getIdentifier());
                    }

                    for (String attributeName: attributeSet){
                        String recordIdentifier = attributeName + "-{" + recordIDs + "}";
                        Record debugRecord = debugFusionResults.getRecord(recordIdentifier);
                        if(debugRecord != null){
                            de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute attributeConsistency = attributeHashMap.get(attributeName + "-Consistency");
                            String consistency = debugRecord.getValue(AttributeFusionLogger.CONSISTENCY);
                            record.setValue(attributeConsistency, consistency);

                            de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute attributeValues = attributeHashMap.get(attributeName + "-Values");
                            String values = debugRecord.getValue(AttributeFusionLogger.VALUES);
                            record.setValue(attributeValues, values);
                        }
                    }
                    debugFusionResultsRecordLevel.add(record);
                }catch (NullPointerException e){
                        logger.error("Null ID for FusedDataSet");
                    }
            }
            //Update Attribute consistencies
            for(Record debugRecord: debugFusionResultsRecordLevel.get()){
                double sumConsistencies = 0;
                int countAttributes = 0;
                for (String attributeName: attributeSet){
                    de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute attributeConsistency = attributeHashMap.get(attributeName + "-Consistency");
                    String consistency = debugRecord.getValue(attributeConsistency);
                    if (consistency != null){
                        sumConsistencies = sumConsistencies + Double.parseDouble(consistency);
                        countAttributes++;
                    }
                }
                double avgConsistency = sumConsistencies/countAttributes;
                debugRecord.setValue(attributeAvgConsistency, Double.toString(avgConsistency));
            }


            // UPDATE write to file part once new ds is generated
            String debugReportfilePath = this.filePathDebugResults.replace(".csv", "_recordLevel.csv");
            try {
                new RecordCSVFormatter().writeCSV(new File(debugReportfilePath), debugFusionResultsRecordLevel, headerDebugResultsRecordLevel);
                logger.info("Debug results on record level written to file: " + debugReportfilePath);
            } catch (IOException e) {
                logger.error("Debug results on record level could not be written to file: " + debugReportfilePath);
            }
    }
}
}
