package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;


public class GameXMLReader extends XMLMatchableReader<Game, Attribute>  {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Game, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
	}
	
	@Override
	public Game createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Game game = new Game(id, provenanceInfo);

		// fill the attributes
		game.setName(getValueFromChildElement(node, "name"));
		game.setPlatform(getValueFromChildElement(node, "platform"));
		game.setSummary(getValueFromChildElement(node, "summary"));
		game.setRating(getValueFromChildElement(node, "rating"));
		game.setSeries(getValueFromChildElement(node, "series"));
		
		// convert the date string into a DateTime object
		try {
			String publicationDate = getValueFromChildElement(node, "publicationDate");
			if (publicationDate != null && !publicationDate.isEmpty()) {
				DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				        .appendPattern("yyyy-MM-dd")
				        .toFormatter(Locale.ENGLISH);
				LocalDateTime dt = LocalDateTime.parse(publicationDate, formatter);
				game.setPublicationDate(dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// load the list of publisherss
		List<Publisher> publishers = getObjectListFromChildElement(node, "publisherss",
				"publishers", new PublisherXMLReader(), provenanceInfo);
		game.setPublishers(publishers);
		// load the list of genres
		List<Genre> genres = getObjectListFromChildElement(node, "genres",
				"genre", new GenreXMLReader(), provenanceInfo);
		game.setGenres(genres);
		// load the list of developer
		List<Developer> developer = getObjectListFromChildElement(node, "genres",
				"genre", new DeveloperXMLReader(), provenanceInfo);
		game.setDevelopers(developer);		

		return game;
	}
}
