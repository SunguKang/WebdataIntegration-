package de.uni_mannheim.informatik.dws.wdi.DataFusion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation.*;
import de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers.*;
import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.wdi.model.GameCSVFormatter;
import de.uni_mannheim.informatik.dws.wdi.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.wdi.model.GameXMLFormatter;
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

	private static final Logger logger = WinterLogManager.activateLogger("infoFile");
	
	public static void main( String[] args ) throws Exception
    {
		try (InputStream is = Files.newInputStream(Paths.get("src/main/resources/config.properties"))) {
			Properties prop = new Properties();
			prop.load(is);

			String dataFolderPath = prop.getProperty("data.path");
			String folderPathXMLPreprocessedFiles = dataFolderPath + prop.getProperty("data.preprocessing.path")
					+ prop.getProperty("data.preprocessing.preprocessed_xml.path");
//unused    String folderGoldStandardIR = dataFolderPath + prop.getProperty("data.gold_standard_ir.path");
			String correspondencesFolderPath = dataFolderPath + prop.getProperty("data.correspondences.path");
			String fusedFolderPath = dataFolderPath + prop.getProperty("data.fused.path");
			String goldStandardFusionPath = dataFolderPath + prop.getProperty("data.gold_standard_fusion.path");

			String record_path = "/videogames/videogame";
			String debugResultsOutputPath = "data/output/";

			// Load the Data as FusibleHashedDataSet (original idea:  data set A as HashedDataset and the rest are FusibleDataSet, might not work) 
			logger.info("*\tLoading datasets\t*");
			FusibleDataSet<Game, Attribute> data_A = new FusibleHashedDataSet<>();
			FusibleDataSet<Game, Attribute> data_B = new FusibleHashedDataSet<>();
			FusibleDataSet<Game, Attribute> data_C = new FusibleHashedDataSet<>();
			FusibleDataSet<Game, Attribute> data_D = new FusibleHashedDataSet<>();
			FusibleDataSet<Game, Attribute> data_E = new FusibleHashedDataSet<>();

			//relative paths within the git folder
			logger.info("*\tLoading from XML\t*");
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_B.xml"),
					record_path, data_B);
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_A.xml"),
					record_path, data_A);
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_C.xml"),
					record_path, data_C);
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_D.xml"),
					record_path, data_D);
			new GameXMLReader().loadFromXML(new File(folderPathXMLPreprocessedFiles + "Dataset_E.xml"),
					record_path, data_E);

			data_A.printDataSetDensityReport();
			data_B.printDataSetDensityReport();
			data_C.printDataSetDensityReport();
			data_D.printDataSetDensityReport();
			data_E.printDataSetDensityReport();

			// Provenance Scores (e.g. from rating)
			// the higher the score the more trust worthy the data is (acording to the tutors)
			/*
			 * Dataset_A scraped from Metacritic
			Dataset_B scraped from Wikipedia, igdb
			Dataset_C scraped from VGChartz and Metacritic
			Dataset_D Web Scraping from TrueTrophies.com
			Dataset_E Query from WikiData
			 */
			data_A.setScore(2.0);  //one source, not wiki (Metacritic)
			data_B.setScore(3.0); //multiple sources, one is wiki
			data_C.setScore(4.0); //multiple sources, none is wiki
			data_D.setScore(2.0); //one source, not wiki (TrueTrophies.com)
			data_E.setScore(1.0); //raw wikipedia
			
			
			// Date (e.g. last update)
			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			        .appendPattern("yyyy-MM-dd")
			        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
			        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			        .toFormatter(Locale.ENGLISH);


			data_A.setDate(LocalDateTime.parse("2022-02-01", formatter)); //imputed day-part of date
			data_B.setDate(LocalDateTime.parse("2021-12-30", formatter));
			data_C.setDate(LocalDateTime.parse("2017-01-01", formatter)); //imputed day-part of date
			data_D.setDate(LocalDateTime.parse("2021-12-01", formatter)); //imputed day-part of date
			data_E.setDate(LocalDateTime.parse("2022-10-04", formatter)); 


			// load correspondences
			logger.info("*\tLoading correspondences\t*");
			CorrespondenceSet<Game, Attribute> correspondences = new CorrespondenceSet<>();
			correspondences.loadCorrespondences(new File(correspondencesFolderPath + "A_B_correspondences.csv"), data_A, data_B);
			correspondences.loadCorrespondences(new File(correspondencesFolderPath + "A_D_correspondences.csv"), data_A, data_D);
			correspondences.loadCorrespondences(new File(correspondencesFolderPath + "B_C_correspondences.csv"), data_B, data_C);
			correspondences.loadCorrespondences(new File(correspondencesFolderPath + "C_D_correspondences.csv"), data_C, data_D);
			correspondences.loadCorrespondences(new File(correspondencesFolderPath + "C_E_correspondences.csv"), data_C, data_E);

			// write group size distribution
			correspondences.printGroupSizeDistribution();

			// load the gold standard
			logger.info("*\tEvaluating results\t*");
			DataSet<Game, Attribute> gs = new FusibleHashedDataSet<>();
			new GameXMLReader().loadFromXML(new File(goldStandardFusionPath + "gold.xml"),
					record_path, gs);

			for (Game g : gs.get()) {
				logger.info(String.format("gs: %s", g.getIdentifier()));
			}

			// define the fusion strategy
			GameDataFusionStrategy<Game, Attribute> strategy = new GameDataFusionStrategy<>(new GameXMLReader());
			
			// write debug results to file
			strategy.activateDebugReport(debugResultsOutputPath + "debugResultsDatafusion.csv", -1, gs);

			strategy.addAttributeFuser(Game.NAME, new NameFuserLongestString(), new NameEvaluationRule());
			strategy.addAttributeFuser(Game.PLATFORM, new PlatformFuserLongestString(), new PlatformEvaluationRule(1.0));
			strategy.addAttributeFuser(Game.PUBLISHERS, new PublishersFuserUnion(), new PublishersEvaluationRule());
			strategy.addAttributeFuser(Game.PUBLICATIONDATE, new DateFuserVoting(), new PublicationDateEvaluationRule());
			strategy.addAttributeFuser(Game.GLOBALLYSOLDUNITS, new GloballySoldUnitsFuserVoting(), new GloballySoldUnitsEvaluationRule());
			strategy.addAttributeFuser(Game.GENRES, new GenresFuserUnion(), new GenresEvaluationRule());
			strategy.addAttributeFuser(Game.CRITICSCORE, new CriticScoreFuserVoting(), new CriticScoreEvaluationRule());
			strategy.addAttributeFuser(Game.USERSCORE, new UserScoreFuserVoting(), new UserScoreEvaluationRule());
			strategy.addAttributeFuser(Game.DEVELOPERS, new DevelopersFuserUnion(), new DevelopersEvaluationRule());
			strategy.addAttributeFuser(Game.SUMMARY, new SummaryFuserLongestString(), new SummaryEvaluationRule());
			strategy.addAttributeFuser(Game.RATING, new RatingFuserVoting(), new RatingEvaluationRule());
			strategy.addAttributeFuser(Game.SERIES, new SeriesFuserFavourSource(), new SeriesEvaluationRule());

			// create the fusion engine
			DataFusionEngine<Game, Attribute> engine = new DataFusionEngine<>(strategy);

			//engine.printClusterConsistencyReport(correspondences, null);

			// print record groups sorted by consistency
			engine.writeRecordGroupsByConsistency(new File(debugResultsOutputPath + "recordGroupConsistencies.csv"), correspondences, null);

			// run the fusion
			logger.info("*\tRunning data fusion\t*");

			FusibleDataSet<Game, Attribute> fusedDataSet = engine.run(correspondences, null);

			// write the result
			new GameXMLFormatter().writeXML(new File(fusedFolderPath + "fused.xml"), fusedDataSet);
			Collection attributeCollection = fusedDataSet.getSchema().get();
			List<Attribute> csvHeaders = new ArrayList<Attribute>(attributeCollection);
			new GameCSVFormatter().writeCSV(new File(fusedFolderPath + "fused.csv"), fusedDataSet, csvHeaders);
			// evaluate
			DataFusionEvaluator<Game, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Game, Attribute>());

			double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

			logger.info(String.format("*\tAccuracy: %.2f", accuracy));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
