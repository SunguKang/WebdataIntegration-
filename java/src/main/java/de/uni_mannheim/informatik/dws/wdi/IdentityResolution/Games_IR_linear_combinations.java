package de.uni_mannheim.informatik.dws.wdi.IdentityResolution;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.wdi.model.GameXMLReader;
import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.GameBlockingKeyByPlatformGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.GameNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
//import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
//import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
//import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
//import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class Games_IR_linear_combinations 
{
private static final Logger logger = WinterLogManager.activateLogger("trace");
	
    public static void main( String[] args ) throws Exception
    {
    	// loading data
    			logger.info("*\tLoading datasets\t*");
    			HashedDataSet<Game, Attribute> data_A = new HashedDataSet<>();
    			HashedDataSet<Game, Attribute> data_B = new HashedDataSet<>();
    			HashedDataSet<Game, Attribute> data_C = new HashedDataSet<>();
    			HashedDataSet<Game, Attribute> data_D = new HashedDataSet<>();
    			HashedDataSet<Game, Attribute> data_E = new HashedDataSet<>();
    			//relative paths within the git folder
    			new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/integrated_target_schema_Windows.xml"), "/videogames/videogame", data_B);
    			new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/target_schema_metacritic.xml"), "/videogames/videogame", data_A);
    			new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/target_schema_Video_Games_Sales.xml"), "/videogames/videogame", data_C);
    			new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/integrated_target_schemaPS4.xml"), "/videogames/videogame", data_D);
    			new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/wikidata_integrated_target_schema.xml"), "/videogames/videogame", data_E);

    			
    			//load the gold standard (test set) from the git folder structure
    			logger.info("*\tLoading gold standard\t*");
    			MatchingGoldStandard gsTestA_B = new MatchingGoldStandard();
    			gsTestA_B.loadFromCSVFile(new File(
    					"../data/gold_standard/merged/A-B.csv"));
    			MatchingGoldStandard gsTestA_D = new MatchingGoldStandard();
    			gsTestA_D.loadFromCSVFile(new File(
    					"../data/gold_standard/merged/A-D.csv"));
    			MatchingGoldStandard gsTestB_C = new MatchingGoldStandard();
    			gsTestB_C.loadFromCSVFile(new File(
    					"../data/gold_standard/merged/B-C.csv"));
    			MatchingGoldStandard gsTestC_D = new MatchingGoldStandard();
    			gsTestC_D.loadFromCSVFile(new File(
    					"../data/gold_standard/merged/C-D.csv"));
    			MatchingGoldStandard gsTestC_E = new MatchingGoldStandard();
    			gsTestC_E.loadFromCSVFile(new File(
    	    			"../data/gold_standard/merged/C-E.csv"));
				
    			// create a matching rule
    			LinearCombinationMatchingRule<Game, Attribute> matchingRuleA_B = new LinearCombinationMatchingRule<>(
    					0.7);
    			LinearCombinationMatchingRule<Game, Attribute> matchingRuleA_D = new LinearCombinationMatchingRule<>(
    					0.7);
    			LinearCombinationMatchingRule<Game, Attribute> matchingRuleB_C = new LinearCombinationMatchingRule<>(
    					0.7);
    			LinearCombinationMatchingRule<Game, Attribute> matchingRuleC_D = new LinearCombinationMatchingRule<>(
    					0.7);
    			LinearCombinationMatchingRule<Game, Attribute> matchingRuleC_E = new LinearCombinationMatchingRule<>(
    					0.7);
    			
    			//this exports the debug report
    			matchingRuleA_B.activateDebugReport("data/output/debugResultsMatchingRuleA_B.csv", 1000, gsTestA_B);
    			matchingRuleA_B.addComparator(new GameNameComparatorLevenshtein(), 1);
    			matchingRuleA_D.activateDebugReport("data/output/debugResultsMatchingRuleA_D.csv", 1000, gsTestA_D);
    			matchingRuleA_D.addComparator(new GameNameComparatorLevenshtein(), 1);
    			matchingRuleB_C.activateDebugReport("data/output/debugResultsMatchingRuleB_C.csv", 1000, gsTestB_C);
    			matchingRuleB_C.addComparator(new GameNameComparatorLevenshtein(), 1);
    			matchingRuleC_D.activateDebugReport("data/output/debugResultsMatchingRuleC_D.csv", 1000, gsTestC_D);
    			matchingRuleC_D.addComparator(new GameNameComparatorLevenshtein(), 1);
    			matchingRuleC_E.activateDebugReport("data/output/debugResultsMatchingRuleC_E.csv", 1000, gsTestC_E);
    			matchingRuleC_E.addComparator(new GameNameComparatorLevenshtein(), 1);
    		
    			// create a blocker (blocking strategy)
    			
    			//include year in GameBlockingKeyByPlatformGenerator because lazy
    			StandardRecordBlocker<Game, Attribute> blockerA_B = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformGenerator());
    			StandardRecordBlocker<Game, Attribute> blockerA_D = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformGenerator());
    			StandardRecordBlocker<Game, Attribute> blockerB_C = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformGenerator());
    			StandardRecordBlocker<Game, Attribute> blockerC_D = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformGenerator());
    			StandardRecordBlocker<Game, Attribute> blockerC_E = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformGenerator());

    			//StandardRecordBlocker<Game, Attribute> blocker2 = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByYearGenerator());

    			// NoBlocker<Movie, Attribute> blocker = new NoBlocker<>();
    			
    			//other way to block by year and search in a range
    			//SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);
    			blockerA_B.setMeasureBlockSizes(true);
    			blockerA_D.setMeasureBlockSizes(true);
    			blockerB_C.setMeasureBlockSizes(true);
    			blockerC_D.setMeasureBlockSizes(true);
    			blockerC_E.setMeasureBlockSizes(true);

    			//blocker2.setMeasureBlockSizes(true);

    			//Write debug results to file:
    			blockerA_B.collectBlockSizeData("data/output/debugResultsBlockingA_B.csv", 100);
    			blockerA_D.collectBlockSizeData("data/output/debugResultsBlockingB_D.csv", 100);
    			blockerB_C.collectBlockSizeData("data/output/debugResultsBlockingB_C.csv", 100);
    			blockerC_D.collectBlockSizeData("data/output/debugResultsBlockingC_D.csv", 100);
    			blockerC_E.collectBlockSizeData("data/output/debugResultsBlockingC_E.csv", 100);

    			//blocker2.collectBlockSizeData("data/output/debugResultsBlocking2.csv", 100);

    			// Initialize Matching Engine
    			MatchingEngine<Game, Attribute> engineA_B = new MatchingEngine<>();
    			MatchingEngine<Game, Attribute> engineA_D = new MatchingEngine<>();
    			MatchingEngine<Game, Attribute> engineB_C = new MatchingEngine<>();
    			MatchingEngine<Game, Attribute> engineC_D = new MatchingEngine<>();
    			MatchingEngine<Game, Attribute> engineC_E = new MatchingEngine<>();

    			// Execute the matching
    			logger.info("*\tRunning identity resolution\t*");
    			Processable<Correspondence<Game, Attribute>> correspondencesA_B = engineA_B.runIdentityResolution(
    					data_A, data_B, null, matchingRuleA_B,
    					blockerA_B);
    			Processable<Correspondence<Game, Attribute>> correspondencesA_D = engineA_D.runIdentityResolution(
    					data_A, data_D, null, matchingRuleA_D,
    					blockerA_D);
    			Processable<Correspondence<Game, Attribute>> correspondencesB_C = engineB_C.runIdentityResolution(
    					data_B, data_C, null, matchingRuleB_C,
    					blockerB_C);
    			Processable<Correspondence<Game, Attribute>> correspondencesC_D = engineC_D.runIdentityResolution(
    					data_C, data_D, null, matchingRuleC_D,
    					blockerC_D);
    			Processable<Correspondence<Game, Attribute>> correspondencesC_E = engineC_E.runIdentityResolution(
    					data_C, data_E, null, matchingRuleC_E,
    					blockerC_E);
    			
    			
    			// Create a top-1 global matching
//    			  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

//    			 Alternative: Create a maximum-weight, bipartite matching
//    			 MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
//    			 maxWeight.run();
//    			 correspondences = maxWeight.getResult();

    			// write the correspondences to the output file (in the data folder of the git repository)
    			new CSVCorrespondenceFormatter().writeCSV(new File("../data/correspondences/A_B_correspondences.csv"), correspondencesA_B);		
    			new CSVCorrespondenceFormatter().writeCSV(new File("../data/correspondences/A_D_correspondences.csv"), correspondencesA_D);		
    			new CSVCorrespondenceFormatter().writeCSV(new File("../data/correspondences/B_C_correspondences.csv"), correspondencesB_C);		
    			new CSVCorrespondenceFormatter().writeCSV(new File("../data/correspondences/C_D_correspondences.csv"), correspondencesC_D);		
    			new CSVCorrespondenceFormatter().writeCSV(new File("../data/correspondences/C_E_correspondences.csv"), correspondencesC_E);		

    			logger.info("*\tEvaluating result\t*");
    			// evaluate your result
    			MatchingEvaluator<Game, Attribute> evaluatorA_B = new MatchingEvaluator<Game, Attribute>();
    			Performance perfTestA_B = evaluatorA_B.evaluateMatching(correspondencesA_B,
    					gsTestA_B);
    			MatchingEvaluator<Game, Attribute> evaluatorA_D = new MatchingEvaluator<Game, Attribute>();
    			Performance perfTestA_D = evaluatorA_D.evaluateMatching(correspondencesA_D,
    					gsTestA_D);
    			MatchingEvaluator<Game, Attribute> evaluatorB_C = new MatchingEvaluator<Game, Attribute>();
    			Performance perfTestB_C = evaluatorB_C.evaluateMatching(correspondencesB_C,
    					gsTestB_C);
    			MatchingEvaluator<Game, Attribute> evaluatorC_D = new MatchingEvaluator<Game, Attribute>();
    			Performance perfTestC_D = evaluatorC_D.evaluateMatching(correspondencesC_D,
    					gsTestC_D);
    			MatchingEvaluator<Game, Attribute> evaluatorC_E = new MatchingEvaluator<Game, Attribute>();
    			Performance perfTestC_E = evaluatorC_E.evaluateMatching(correspondencesC_E,
    					gsTestC_E);
    			// print the evaluation result
    			logger.info("data_A <-> data_B");
    			logger.info(String.format(
    					"Precision: %.4f",perfTestA_B.getPrecision()));
    			logger.info(String.format(
    					"Recall: %.4f",	perfTestA_B.getRecall()));
    			logger.info(String.format(
    					"F1: %.4f",perfTestA_B.getF1()));
    			logger.info("data_A <-> data_D");
    			logger.info(String.format(
    					"Precision: %.4f",perfTestA_D.getPrecision()));
    			logger.info(String.format(
    					"Recall: %.4f",	perfTestA_D.getRecall()));
    			logger.info(String.format(
    					"F1: %.4f",perfTestA_D.getF1()));
    			logger.info("data_B <-> data_C");
    			logger.info(String.format(
    					"Precision: %.4f",perfTestB_C.getPrecision()));
    			logger.info(String.format(
    					"Recall: %.4f",	perfTestB_C.getRecall()));
    			logger.info(String.format(
    					"F1: %.4f",perfTestB_C.getF1()));
    			logger.info("data_C <-> data_D");
    			logger.info(String.format(
    					"Precision: %.4f",perfTestC_D.getPrecision()));
    			logger.info(String.format(
    					"Recall: %.4f",	perfTestC_D.getRecall()));
    			logger.info(String.format(
    					"F1: %.4f",perfTestC_D.getF1()));
    			logger.info("data_C <-> data_E");
    			logger.info(String.format(
    					"Precision: %.4f",perfTestC_E.getPrecision()));
    			logger.info(String.format(
    					"Recall: %.4f",	perfTestC_E.getRecall()));
    			logger.info(String.format(
    					"F1: %.4f",perfTestC_E.getF1()));
    			
    }
}