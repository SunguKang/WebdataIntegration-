package de.uni_mannheim.informatik.dws.wdi.IdentityResolution;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.GameBlockingKeyByPlatformYearGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.GameBlockingKeyByPlatformYearsGenerator;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Comparators.*;
import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.util.HelperClassComparatorWeightPair;
import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.wdi.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.comparators.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.*;
import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Blocking.GameBlockingKeyByPlatformGenerator;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
//import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
//import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
//import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
//import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

import static java.util.Map.entry;

public class Games_IR_linear_combinations 
{
private static final Logger logger = WinterLogManager.activateLogger("trace");

    public static void main( String[] args ) throws Exception
    {
		try (InputStream is = Files.newInputStream(Paths.get("src/main/resources/config.properties"))) {
			Properties prop = new Properties();
			prop.load(is);
			
			PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);


			//		String folderPathXMLSourceFiles = "../data/preprocessing/preprocessed_xml_files/";
			//		String folderGoldStandardIR = "../data/gold_standard/merged/";
			//		String correspondencesFolderPath = "../data/correspondences/";
			String dataFolderPath = prop.getProperty("data.path");
			String folderPathXMLPreprocessedFiles = dataFolderPath + prop.getProperty("data.preprocessing.path")
					+ prop.getProperty("data.preprocessing.preprocessed_xml.path");
			String folderGoldStandardIR = dataFolderPath + prop.getProperty("data.gold_standard_ir.path");
			String correspondencesFolderPath = dataFolderPath + prop.getProperty("data.correspondences.path");

			String debugResultsOuputPath = "data/output/";
			// loading data
			logger.info("*\tLoading datasets\t*");
			HashedDataSet<Game, Attribute> data_A = new HashedDataSet<>();
			HashedDataSet<Game, Attribute> data_B = new HashedDataSet<>();
			HashedDataSet<Game, Attribute> data_C = new HashedDataSet<>();
			HashedDataSet<Game, Attribute> data_D = new HashedDataSet<>();
			HashedDataSet<Game, Attribute> data_E = new HashedDataSet<>();

			//relative paths within the git folder
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_B.xml"),
					"/videogames/videogame", data_B);
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_A.xml"),
					"/videogames/videogame", data_A);
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_C.xml"),
					"/videogames/videogame", data_C);
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_D.xml"),
					"/videogames/videogame", data_D);
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_E.xml"),
					"/videogames/videogame", data_E);

			//load the gold standard (test set) from the git folder structure
			logger.info("*\tLoading gold standard\t*");
			MatchingGoldStandard gsTestA_B = new MatchingGoldStandard();
			gsTestA_B.loadFromCSVFile(new File(folderGoldStandardIR + "A-B.csv"));
			MatchingGoldStandard gsTestA_D = new MatchingGoldStandard();
			gsTestA_D.loadFromCSVFile(new File(folderGoldStandardIR + "A-D.csv"));
			MatchingGoldStandard gsTestB_C = new MatchingGoldStandard();
			gsTestB_C.loadFromCSVFile(new File(folderGoldStandardIR + "B-C.csv"));
			MatchingGoldStandard gsTestC_D = new MatchingGoldStandard();
			gsTestC_D.loadFromCSVFile(new File(folderGoldStandardIR + "C-D.csv"));
			MatchingGoldStandard gsTestC_E = new MatchingGoldStandard();
			gsTestC_E.loadFromCSVFile(new File(folderGoldStandardIR + "C-E.csv"));

			// create a matching rule
			double thresh = 0.9;
			LinearCombinationMatchingRule<Game, Attribute> matchingRuleA_B = new LinearCombinationMatchingRule<>(
					thresh);
			LinearCombinationMatchingRule<Game, Attribute> matchingRuleA_D = new LinearCombinationMatchingRule<>(
					thresh);
			LinearCombinationMatchingRule<Game, Attribute> matchingRuleB_C = new LinearCombinationMatchingRule<>(
					thresh);
			LinearCombinationMatchingRule<Game, Attribute> matchingRuleC_D = new LinearCombinationMatchingRule<>(
					thresh);
			LinearCombinationMatchingRule<Game, Attribute> matchingRuleC_E = new LinearCombinationMatchingRule<>(
					thresh);

			//this exports the debug report
			Map<String, List<Serializable>> pairsDict = Map.ofEntries(
					entry("A_B", Arrays.asList(matchingRuleA_B, gsTestA_B)),
					entry("A_D", Arrays.asList(matchingRuleA_D, gsTestA_D)),
					entry("B_C", Arrays.asList(matchingRuleB_C, gsTestB_C)),
					entry("C_D", Arrays.asList(matchingRuleC_D, gsTestC_D)),
					entry("C_E", Arrays.asList(matchingRuleC_E, gsTestC_E))
			);
			
			int comparatorSetChosenKey = 7;
			
			// nur Jahr und Name
			HashMap<Integer, List> comparatorSetsDict = new HashMap<>();

			List<HelperClassComparatorWeightPair> compartorSetOne =  new ArrayList<HelperClassComparatorWeightPair>();
			compartorSetOne.add(new HelperClassComparatorWeightPair(new GameNameComparatorLevenshtein(), 0.7));
			compartorSetOne.add(new HelperClassComparatorWeightPair(new GamePlatformComparatorEqual(), 0.3));
			comparatorSetsDict.put(1, compartorSetOne);

			List<HelperClassComparatorWeightPair> compartorSetTwo =  new ArrayList<HelperClassComparatorWeightPair>();
			compartorSetTwo.add(new HelperClassComparatorWeightPair(new GameNamePreprocessedComparatorJaccard(), 0.7));
			compartorSetTwo.add(new HelperClassComparatorWeightPair(new GamePlatformComparatorEqual(), 0.3));
			comparatorSetsDict.put(2, compartorSetTwo);

			List<HelperClassComparatorWeightPair> comparatorSetThree =  new ArrayList<HelperClassComparatorWeightPair>();
			comparatorSetThree.add(new HelperClassComparatorWeightPair(new GameNamePreprocessedComparatorLevenshtein(), 0.7));
			comparatorSetThree.add(new HelperClassComparatorWeightPair(new GamePlatformComparatorEqual(), 0.3));
			comparatorSetsDict.put(3, comparatorSetThree);

			List<HelperClassComparatorWeightPair> comparatorSetFour =  new ArrayList<HelperClassComparatorWeightPair>();
			comparatorSetFour.add(new HelperClassComparatorWeightPair(new GameNamePreprocessedComparatorEqual(), 0.7));
			comparatorSetFour.add(new HelperClassComparatorWeightPair(new GamePlatformComparatorEqual(), 0.3));
			comparatorSetsDict.put(4, comparatorSetFour);

			List<HelperClassComparatorWeightPair> comparatorSetFive =  new ArrayList<HelperClassComparatorWeightPair>();
			comparatorSetFive.add(new HelperClassComparatorWeightPair(new GameNamePreprocessedComparatorJaccard(), 0.7));
			comparatorSetFive.add(new HelperClassComparatorWeightPair(new GameDateComparator3Years(), 0.3));
			comparatorSetFive.add(new HelperClassComparatorWeightPair(new GamePlatformComparatorEqual(), 0.3));
			comparatorSetsDict.put(5, comparatorSetFive);

			List<HelperClassComparatorWeightPair> comparatorSetSix =  new ArrayList<HelperClassComparatorWeightPair>();
			comparatorSetSix.add(new HelperClassComparatorWeightPair(new GameNamePreprocessedComparatorJaccard(), 0.7));
			comparatorSetSix.add(new HelperClassComparatorWeightPair(new GameDateComparatorYearEqual(), 0.3));
			comparatorSetSix.add(new HelperClassComparatorWeightPair(new GamePlatformComparatorEqual(), 0.3));
			comparatorSetsDict.put(6, comparatorSetSix);

			List<HelperClassComparatorWeightPair> comparatorSetSeven =  new ArrayList<HelperClassComparatorWeightPair>();
			comparatorSetSeven.add(new HelperClassComparatorWeightPair(new GameNamePreprocessedComparatorJaccard(), 0.7));
			comparatorSetSeven.add(new HelperClassComparatorWeightPair(new GameDateComparator3Years(), 0.3));
			comparatorSetsDict.put(7, comparatorSetSeven);
			
			List<HelperClassComparatorWeightPair> compartorSetNine =  new ArrayList<HelperClassComparatorWeightPair>();
			compartorSetNine.add(new HelperClassComparatorWeightPair(new GameNameComparatorLevenshtein(), 0.7));
			compartorSetNine.add(new HelperClassComparatorWeightPair(new GameDateComparator3Years(), 0.3));
			comparatorSetsDict.put(9, compartorSetNine);



			List<HelperClassComparatorWeightPair> chosenCompartorSet = comparatorSetsDict.get(comparatorSetChosenKey);
			for (String datasetKey : pairsDict.keySet()){
				List<Serializable> val = pairsDict.get(datasetKey);
				LinearCombinationMatchingRule currentMatchingRule = (LinearCombinationMatchingRule) val.get(0);
				MatchingGoldStandard currentGS = (MatchingGoldStandard) val.get(1);
				currentMatchingRule.activateDebugReport(debugResultsOuputPath + "debugResultsMatchingRule" +
								datasetKey + ".csv", 10000, currentGS);
				for (HelperClassComparatorWeightPair comparatorWeightPair : chosenCompartorSet) {
					currentMatchingRule.addComparator(comparatorWeightPair.getComparator(), comparatorWeightPair.getWeight());
				}
			}

			// create a blocker (blocking strategy)
			// did not use NeighbourhoodBlocker as that was not documented well enough, better to achieve with
			// StandardRecordBlocker
			StandardRecordBlocker<Game, Attribute> blockerA_D = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformYearsGenerator());
			StandardRecordBlocker<Game, Attribute> blockerA_B = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformYearsGenerator());
			StandardRecordBlocker<Game, Attribute> blockerB_C = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformYearsGenerator());
			StandardRecordBlocker<Game, Attribute> blockerC_D = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformYearsGenerator());
			StandardRecordBlocker<Game, Attribute> blockerC_E = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformYearsGenerator());

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
			blockerA_B.collectBlockSizeData(debugResultsOuputPath + "debugResultsBlockingA_B.csv", 100);
			blockerA_D.collectBlockSizeData(debugResultsOuputPath + "debugResultsBlockingA_D.csv", 100);
			blockerB_C.collectBlockSizeData(debugResultsOuputPath + "debugResultsBlockingB_C.csv", 100);
			blockerC_D.collectBlockSizeData(debugResultsOuputPath + "debugResultsBlockingC_D.csv", 100);
			blockerC_E.collectBlockSizeData(debugResultsOuputPath + "debugResultsBlockingC_E.csv", 100);


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
					data_A, data_B, null, matchingRuleA_B, blockerA_B);
			Processable<Correspondence<Game, Attribute>> correspondencesA_D = engineA_D.runIdentityResolution(
					data_A, data_D, null, matchingRuleA_D, blockerA_D);
			Processable<Correspondence<Game, Attribute>> correspondencesB_C = engineB_C.runIdentityResolution(
					data_B, data_C, null, matchingRuleB_C, blockerB_C);
			Processable<Correspondence<Game, Attribute>> correspondencesC_D = engineC_D.runIdentityResolution(
					data_C, data_D, null, matchingRuleC_D, blockerC_D);
			Processable<Correspondence<Game, Attribute>> correspondencesC_E = engineC_E.runIdentityResolution(
					data_C, data_E, null, matchingRuleC_E, blockerC_E);


			// Create a top-1 global matching
			// correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

			// Alternative: Create a maximum-weight, bipartite matching
			// MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
			// maxWeight.run();
			// correspondences = maxWeight.getResult();

			// write the correspondences to the output file (in the data folder of the git repository)
			new CSVCorrespondenceFormatter().writeCSV(
					new File(correspondencesFolderPath+ "A_B_correspondences.csv"), correspondencesA_B);
			new CSVCorrespondenceFormatter().writeCSV(
					new File(correspondencesFolderPath + "A_D_correspondences.csv"), correspondencesA_D);
			new CSVCorrespondenceFormatter().writeCSV(
					new File(correspondencesFolderPath + "B_C_correspondences.csv"), correspondencesB_C);
			new CSVCorrespondenceFormatter().writeCSV(
					new File(correspondencesFolderPath + "C_D_correspondences.csv"), correspondencesC_D);
			new CSVCorrespondenceFormatter().writeCSV(
					new File(correspondencesFolderPath + "C_E_correspondences.csv"), correspondencesC_E);

			logger.info("*\tEvaluating result\t*");
			// evaluate your result
			MatchingEvaluator<Game, Attribute> evaluatorA_B = new MatchingEvaluator<Game, Attribute>();
			Performance perfTestA_B = evaluatorA_B.evaluateMatching(correspondencesA_B, gsTestA_B);
			MatchingEvaluator<Game, Attribute> evaluatorA_D = new MatchingEvaluator<Game, Attribute>();
			Performance perfTestA_D = evaluatorA_D.evaluateMatching(correspondencesA_D, gsTestA_D);
			MatchingEvaluator<Game, Attribute> evaluatorB_C = new MatchingEvaluator<Game, Attribute>();
			Performance perfTestB_C = evaluatorB_C.evaluateMatching(correspondencesB_C, gsTestB_C);
			MatchingEvaluator<Game, Attribute> evaluatorC_D = new MatchingEvaluator<Game, Attribute>();
			Performance perfTestC_D = evaluatorC_D.evaluateMatching(correspondencesC_D, gsTestC_D);
			MatchingEvaluator<Game, Attribute> evaluatorC_E = new MatchingEvaluator<Game, Attribute>();
			Performance perfTestC_E = evaluatorC_E.evaluateMatching(correspondencesC_E, gsTestC_E);
			// print the evaluation result
			logger.info("data_A <-> data_B");
			logger.info(String.format("Precision: %.4f",perfTestA_B.getPrecision()));
			logger.info(String.format("Recall: %.4f",	perfTestA_B.getRecall()));
			logger.info(String.format("F1: %.4f",perfTestA_B.getF1()));
			logger.info("data_A <-> data_D");
			logger.info(String.format("Precision: %.4f",perfTestA_D.getPrecision()));
			logger.info(String.format("Recall: %.4f",	perfTestA_D.getRecall()));
			logger.info(String.format("F1: %.4f",perfTestA_D.getF1()));
			logger.info("data_B <-> data_C");
			logger.info(String.format("Precision: %.4f",perfTestB_C.getPrecision()));
			logger.info(String.format("Recall: %.4f",	perfTestB_C.getRecall()));
			logger.info(String.format("F1: %.4f",perfTestB_C.getF1()));
			logger.info("data_C <-> data_D");
			logger.info(String.format("Precision: %.4f",perfTestC_D.getPrecision()));
			logger.info(String.format("Recall: %.4f",	perfTestC_D.getRecall()));
			logger.info(String.format("F1: %.4f",perfTestC_D.getF1()));
			logger.info("data_C <-> data_E");
			logger.info(String.format("Precision: %.4f",perfTestC_E.getPrecision()));
			logger.info(String.format("Recall: %.4f",	perfTestC_E.getRecall()));
			logger.info(String.format("F1: %.4f",perfTestC_E.getF1()));
			
			System.setOut(out);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }
}