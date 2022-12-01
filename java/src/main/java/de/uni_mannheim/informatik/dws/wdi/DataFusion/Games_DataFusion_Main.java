package de.uni_mannheim.informatik.dws.wdi.DataFusion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

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

			// Load the Data into FusibleDataSet, data set A as HashedDataset and the rest are FusibleDataset (might not work)
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

			// Maintain Provenance
			// Scores (e.g. from rating)
			// TODO set valid scores
			data_A.setScore(1.0);
			data_B.setScore(2.0);
			data_C.setScore(3.0);
			data_D.setScore(3.0);
			data_E.setScore(3.0);
			
			
			// Date (e.g. last update)
			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			        .appendPattern("yyyy-MM-dd")
			        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
			        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			        .toFormatter(Locale.ENGLISH);


			data_A.setDate(LocalDateTime.parse("2022-02-01", formatter)); //imputed day
			data_B.setDate(LocalDateTime.parse("2021-12-30", formatter));
			data_C.setDate(LocalDateTime.parse("2017-01-01", formatter)); //imputed day
			data_D.setDate(LocalDateTime.parse("2021-12-01", formatter)); //imputed day
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
			DataFusionStrategy<Game, Attribute> strategy = new DataFusionStrategy<>(new GameXMLReader());
			// write debug results to file
			//TODO comment in again
			//strategy.activateDebugReport(debugResultsOutputPath + "debugResultsDatafusion.csv", -1, gs);

			// add attribute fusers

			//TODO Adapt
			//TODO create the mentioned Fusers and Evaluators
			// TODO deleted not needed evaluators
			strategy.addAttributeFuser(Game.NAME, new NameFuserLongestString(), new StringEvaluationRule());
			strategy.addAttributeFuser(Game.PLATFORM, new PlatformFuserLongestString(), new StringEvaluationRule(1.0));
			strategy.addAttributeFuser(Game.PUBLISHERS, new PublishersFuserUnion(), new PublishersEvaluationRule());
			// TODO should not be the most recent as this is wrong for some datasets
			strategy.addAttributeFuser(Game.PUBLICATIONDATE, new DateFuserMostRecent(), new PublicationDateEvaluationRule());
			strategy.addAttributeFuser(Game.GLOBALLYSOLDUNITS, new GloballysoldunitsFuserFavourSource(), new FloatEvaluationRule());
			strategy.addAttributeFuser(Game.GENRES, new GenresFuserUnion(), new GenresEvaluationRule());
			strategy.addAttributeFuser(Game.CRITICSCORE, new CriticScoreFuserFavourSource(), new FloatEvaluationRule());
			strategy.addAttributeFuser(Game.USERSCORE, new UserScoreFuserFavourSource(), new FloatEvaluationRule());
			strategy.addAttributeFuser(Game.DEVELOPERS, new DevelopersFuserUnion(), new DevelopersEvaluationRule());
			strategy.addAttributeFuser(Game.SUMMARY, new SummaryFuserFavourSource(), new StringEvaluationRule());
			strategy.addAttributeFuser(Game.RATING, new RatingFuserFavourSource(), new StringEvaluationRule());
			strategy.addAttributeFuser(Game.SERIES, new SeriesFuserFavourSource(), new StringEvaluationRule());

			// create the fusion engine
			DataFusionEngine<Game, Attribute> engine = new DataFusionEngine<>(strategy);

			// print consistency report
			engine.printClusterConsistencyReport(correspondences, null);

			// print record groups sorted by consistency
			engine.writeRecordGroupsByConsistency(new File(debugResultsOutputPath + "recordGroupConsistencies.csv"), correspondences, null);

			// run the fusion
			logger.info("*\tRunning data fusion\t*");
			FusibleDataSet<Game, Attribute> fusedDataSet = engine.run(correspondences, null);

			// write the result
			new GameXMLFormatter().writeXML(new File(fusedFolderPath + "fused.xml"), fusedDataSet);

			// evaluate
			DataFusionEvaluator<Game, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Game, Attribute>());

			double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

			logger.info(String.format("*\tAccuracy: %.2f", accuracy));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
