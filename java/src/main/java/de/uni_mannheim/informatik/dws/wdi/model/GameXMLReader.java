/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

/**
 * A {@link XMLMatchableReader} for {@link Game}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class GameXMLReader extends XMLMatchableReader<Game, Attribute> implements
FusibleFactory<Game, Attribute> {

/* (non-Javadoc)
* @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
*/
@Override
protected void initialiseDataset(DataSet<Game, Attribute> dataset) {
super.initialiseDataset(dataset);

// the schema is defined in the Game class and not interpreted from the file, so we have to set the attributes manually
dataset.addAttribute(Game.NAME);
dataset.addAttribute(Game.PLATFORM);
dataset.addAttribute(Game.PUBLISHERS);
dataset.addAttribute(Game.PUBLICATIONDATE);
dataset.addAttribute(Game.GLOBALLYSOLDUNITS);
dataset.addAttribute(Game.GENRES);
dataset.addAttribute(Game.CRITICSCORE);
dataset.addAttribute(Game.USERSCORE);
dataset.addAttribute(Game.DEVELOPERS);
dataset.addAttribute(Game.SUMMARY);
dataset.addAttribute(Game.RATING);
dataset.addAttribute(Game.SERIES);


}

@Override
public Game createModelFromElement(Node node, String provenanceInfo) {
String id = getValueFromChildElement(node, "id");

	// create the object with id and provenance information
	Game game = new Game(id, provenanceInfo);
	
	// fill the attributes
	game.setName(getValueFromChildElement(node, "name"));
	game.setPlatform(getValueFromChildElement(node, "platform"));
	//Numeric Variables have to be parsed
	game.setGloballyySoldUnits(Float.parseFloat(getValueFromChildElement(node, "globallySoldUnits")));
	game.setCriticScore(Float.parseFloat(getValueFromChildElement(node, "criticScore")));
	game.setUserScore(Float.parseFloat(getValueFromChildElement(node, "userScore")));
	game.setSummary(getValueFromChildElement(node, "summary"));
	game.setRating(getValueFromChildElement(node, "rating"));
	game.setSeries(getValueFromChildElement(node, "series"));
	
	// convert the publicationDate string into a DateTime object
	try {
		String date = getValueFromChildElement(node, "publicationDate");
		if (date != null && !date.isEmpty()) {
			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			        .appendPattern("yyyy-MM-dd['T'HH:mm:ss.SSS]")
			        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
			        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
					.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
					.optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
			        .toFormatter(Locale.ENGLISH);
			LocalDateTime dt = LocalDateTime.parse(date, formatter);
			game.setPublicationDate(dt);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	// load the List variables
//	List<Actor> actors = getObjectListFromChildElement(node, "actors",
//			"actor", new ActorXMLReader(), provenanceInfo);
//	movie.setActors(actors);
	List<Publisher> publishers = getObjectListFromChildElement(node, "publishers",
			"publishers", new PublisherXMLReader(), provenanceInfo);
	game.setPublishers(publishers);
	List<Genre> genres = getObjectListFromChildElement(node, "genres",
			"genres", new GenreXMLReader(), provenanceInfo);
	game.setGenres(genres);
	List<Developer> developers = getObjectListFromChildElement(node, "developers",
			"developers", new DeveloperXMLReader(), provenanceInfo);
	game.setDevelopers(developers);
	
	return game;
	}
	
	@Override
	public Game createInstanceForFusion(RecordGroup<Game, Attribute> cluster) {
	
	List<String> ids = new LinkedList<>();
	
	for (Game m : cluster.getRecords()) {
		ids.add(m.getIdentifier());
	}
	
	Collections.sort(ids);
	
	String mergedId = StringUtils.join(ids, '+');
	
	return new Game(mergedId, "fused");
	}

}
