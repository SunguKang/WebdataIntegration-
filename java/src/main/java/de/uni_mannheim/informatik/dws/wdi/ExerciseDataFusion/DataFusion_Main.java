package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.ActorsEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.DateEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.DirectorEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.TitleEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.ActorsFuserUnion;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.DateFuserFavourSource;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.DateFuserVoting;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.DirectorFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.TitleFuserShortestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.FusibleMovieFactory;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.MovieXMLFormatter;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.MovieXMLReader;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

public class DataFusion_Main 
{
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("default");
	
	public static void main( String[] args ) throws Exception
    {
		// Load the Data into FusibleDataSet, data set A as HashedDataset and the rest are FusibleDataset (might not work)
		logger.info("*\tLoading datasets\t*");
		HashedDataSet<Game, Attribute> data_A = new HashedDataSet<>();
		FusibleDataSet<Game, Attribute> data_B = new FusibleHashedDataSet<>();
		FusibleDataSet<Game, Attribute> data_C = new FusibleHashedDataSet<>();
		FusibleDataSet<Game, Attribute> data_D = new FusibleHashedDataSet<>();
		FusibleDataSet<Game, Attribute> data_E = new FusibleHashedDataSet<>();

		
		
		//relative paths within the git folder
		new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/integrated_target_schema_Windows.xml"), "/videogames/videogame", data_B);
		new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/target_schema_metacritic.xml"), "/videogames/videogame", data_A);
		new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/target_schema_Video_Games_Sales.xml"), "/videogames/videogame", data_C);
		new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/integrated_target_schemaPS4.xml"), "/videogames/videogame", data_D);
		new GameXMLReader().loadFromXML(new File("../data/preprocessing/preprocessed_xml_files/wikidata_integrated_target_schema.xml"), "/videogames/videogame", data_E);

	
		data_A.printDataSetDensityReport();
		data_B.printDataSetDensityReport();
		data_C.printDataSetDensityReport();
		data_D.printDataSetDensityReport();
		data_E.printDataSetDensityReport();

		// Maintain Provenance
		// Scores (e.g. from rating)
		data_A.setScore(1.0);
		data_B.setScore(2.0);
		data_C.setScore(3.0);
		data_D.setScore(3.0);
		data_E.setScore(3.0);


		// load correspondences
		logger.info("*\tLoading correspondences\t*");
		CorrespondenceSet<Game, Attribute> correspondences = new CorrespondenceSet<>();
		correspondences.loadCorrespondences(new File("../data/output/A_B_correspondences.csv"),data_A, data_B);
		correspondences.loadCorrespondences(new File("../data/output/A_D_correspondences.csv"),data_A, data_D);
		correspondences.loadCorrespondences(new File("../data/output/B_C_correspondences.csv"),data_B, data_C);
		correspondences.loadCorrespondences(new File("../data/output/C_D_correspondences.csv"),data_C, data_D);
		correspondences.loadCorrespondences(new File("../data/output/C_E_correspondences.csv"),data_C, data_E);
	

		// write group size distribution
		correspondences.printGroupSizeDistribution();

		// load the gold standard
		logger.info("*\tEvaluating results\t*");
		DataSet<Game, Attribute> gs = new FusibleHashedDataSet<>();
		new MovieXMLReader().loadFromXML(new File("data/goldstandard/gold.xml"), "/movies/movie", gs);
		
		new GameCSVReader().loadFromCSV(new File("../data/goldstandard/A-B.csv"), "/videogames/videogame", data_E);
		for(Movie m : gs.get()) {
			logger.info(String.format("gs: %s", m.getIdentifier()));
		}

		// define the fusion strategy
		DataFusionStrategy<Movie, Attribute> strategy = new DataFusionStrategy<>(new MovieXMLReader());
		// write debug results to file
		strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);
		
		// add attribute fusers
		strategy.addAttributeFuser(Movie.TITLE, new TitleFuserShortestString(),new TitleEvaluationRule());
		strategy.addAttributeFuser(Movie.DIRECTOR,new DirectorFuserLongestString(), new DirectorEvaluationRule());
		strategy.addAttributeFuser(Movie.DATE, new DateFuserFavourSource(),new DateEvaluationRule());
		strategy.addAttributeFuser(Movie.ACTORS,new ActorsFuserUnion(),new ActorsEvaluationRule());
		
		// create the fusion engine
		DataFusionEngine<Movie, Attribute> engine = new DataFusionEngine<>(strategy);

		// print consistency report
		engine.printClusterConsistencyReport(correspondences, null);
		
		// print record groups sorted by consistency
		engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

		// run the fusion
		logger.info("*\tRunning data fusion\t*");
		FusibleDataSet<Movie, Attribute> fusedDataSet = engine.run(correspondences, null);

		// write the result
		new MovieXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

		// evaluate
		DataFusionEvaluator<Movie, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Movie, Attribute>());
		
		double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

		logger.info(String.format("*\tAccuracy: %.2f", accuracy));
    }
}
