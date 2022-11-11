package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.GameBlockingKeyByPlatformGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.GameNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Game;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
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
    			new GameXMLReader().loadFromXML(new File("data/input/target_schema_metacritic.xml"), "/videogames/videogame", data_A);
    			HashedDataSet<Game, Attribute> data_B = new HashedDataSet<>();
    			new GameXMLReader().loadFromXML(new File("data/input/integrated_target_schema_Windows.xml"), "/videogames/videogame", data_B);

    			//load the gold standard (test set)
    			logger.info("*\tLoading gold standard\t*");
    			MatchingGoldStandard gsTest = new MatchingGoldStandard();
    			gsTest.loadFromCSVFile(new File(
    					"data/goldstandard/A-B.csv"));
				
    			// create a matching rule
    			LinearCombinationMatchingRule<Game, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
    					0.7);
    			
    			//this exports the debug report
    			matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTest);
    			    			
//More rules?    		
    			matchingRule.addComparator(new GameNameComparatorLevenshtein(), 1);
    			
    			// create a blocker (blocking strategy)
    			StandardRecordBlocker<Game, Attribute> blocker = new StandardRecordBlocker<Game, Attribute>(new GameBlockingKeyByPlatformGenerator());
    			// NoBlocker<Movie, Attribute> blocker = new NoBlocker<>();
    			//SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);
    			blocker.setMeasureBlockSizes(true);
    			//Write debug results to file:
    			blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

    }
}