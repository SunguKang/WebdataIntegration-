package de.uni_mannheim.informatik.dws.wdi.DataFusion;

import java.io.File;

//TODO write + import classes for Game attributes
import de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation.*;
import de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers.*;
import de.uni_mannheim.informatik.dws.wdi.model.Game;
//TODO adapt class
import de.uni_mannheim.informatik.dws.wdi.model.GameXMLReader;
//TODO adapt class
import de.uni_mannheim.informatik.dws.wdi.model.GameXMLFormatter;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
//import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;



public class Games_DataFusion_Main 
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
		FusibleDataSet<Game, Attribute> data_A = new FusibleHashedDataSet<>();
		FusibleDataSet<Game, Attribute> data_B = new FusibleHashedDataSet<>();
		FusibleDataSet<Game, Attribute> data_C = new FusibleHashedDataSet<>();
		FusibleDataSet<Game, Attribute> data_D = new FusibleHashedDataSet<>();
		FusibleDataSet<Game, Attribute> data_E = new FusibleHashedDataSet<>();

		//relative paths within the git folder
		String folderPathXMLSourceFiles = "../data/preprocessing/preprocessed_xml_files/";
		new GameXMLReader().loadFromXML(new File(folderPathXMLSourceFiles + "Dataset_B.xml"),
				"/videogames/videogame", data_B);
		new GameXMLReader().loadFromXML(new File(folderPathXMLSourceFiles + "Dataset_A.xml"),
				"/videogames/videogame", data_A);
		new GameXMLReader().loadFromXML(new File(folderPathXMLSourceFiles + "Dataset_C.xml"),
				"/videogames/videogame", data_C);
		new GameXMLReader().loadFromXML(new File(folderPathXMLSourceFiles + "Dataset_D.xml"),
				"/videogames/videogame", data_D);
		new GameXMLReader().loadFromXML(new File(folderPathXMLSourceFiles + "Dataset_E.xml"),
				"/videogames/videogame", data_E);
		
		data_A.printDataSetDensityReport();
		data_B.printDataSetDensityReport();
		data_C.printDataSetDensityReport();
		data_D.printDataSetDensityReport();
		data_E.printDataSetDensityReport();

		// Maintain Provenance
		// Scores (e.g. from rating)
		// TODO set valid scores
		data_A.setScore(1.0);
		data_B.setScore(2.0);
		data_C.setScore(3.0);
		data_D.setScore(3.0);
		data_E.setScore(3.0);
		
		//		TODO: Take dates of provenance files
		//		ds1.setDate(LocalDateTime.parse("2012-01-01", formatter));
		//		ds2.setDate(LocalDateTime.parse("2010-01-01", formatter));
		//		ds3.setDate(LocalDateTime.parse("2008-01-01", formatter));


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
		new GameXMLReader().loadFromXML(new File("../data/goldstandard/gold.xml"), "/videogames/videogame", gs);
				
		for(Game g : gs.get()) {
			logger.info(String.format("gs: %s", g.getIdentifier()));
		}

		// define the fusion strategy
		DataFusionStrategy<Game, Attribute> strategy = new DataFusionStrategy<>(new GameXMLReader());
		// write debug results to file
		strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);
		
		// add attribute fusers
		
		//TODO Adapt
		//TODO create the mentioned Fusers and Evaluators
		strategy.addAttributeFuser(Game.NAME, new NameFuserLongestString(), new NameEvaluationRule());
		strategy.addAttributeFuser(Game.PLATFORM, new PlatformFuserLongestString(), new PlatformEvaluationRule());
		strategy.addAttributeFuser(Game.PUBLISHERS, new PublishersFuserUnion(),new PublishersEvaluationRule());
		strategy.addAttributeFuser(Game.PUBLICATIONDATE, new DateFuser(), new PublicationDateEvaluationRule());
		strategy.addAttributeFuser(Game.GLOBALLYSOLDUNITS, new GloballysoldunitsFuserLongestString(), new GloballySoldUnitsEvaluationRule());
		strategy.addAttributeFuser(Game.GENRES, new GenresFuserUnion(),new GenresEvaluationRule());
		strategy.addAttributeFuser(Game.CRITICSCORE, new CriticScoreFuserFavourSource(),new CriticScoreEvaluationRule());
		strategy.addAttributeFuser(Game.USERSCORE, new UserScoreFuserFavourSource(),new UserScoreEvaluationRule());
		strategy.addAttributeFuser(Game.DEVELOPERS, new DevelopersFuserUnion(),new DevelopersEvaluationRule());
		strategy.addAttributeFuser(Game.SUMMARY, new SummaryFuserFavourSource(),new SummaryEvaluationRule());
		strategy.addAttributeFuser(Game.RATING, new RatingFuserFavourSource(),new RatingEvaluationRule());
		strategy.addAttributeFuser(Game.SERIES, new SeriesFuserFavourSource(),new SeriesEvaluationRule());
		
		// create the fusion engine
		DataFusionEngine<Game, Attribute> engine = new DataFusionEngine<>(strategy);

		// print consistency report
		engine.printClusterConsistencyReport(correspondences, null);
		
		// print record groups sorted by consistency
		engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

		// run the fusion
		logger.info("*\tRunning data fusion\t*");
		FusibleDataSet<Game, Attribute> fusedDataSet = engine.run(correspondences, null);

		// write the result
		new GameXMLFormatter().writeXML(new File("../data/fused/fused.xml"), fusedDataSet);

		// evaluate
		DataFusionEvaluator<Game, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Game, Attribute>());
		
		double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

		logger.info(String.format("*\tAccuracy: %.2f", accuracy));
    }	    
}
