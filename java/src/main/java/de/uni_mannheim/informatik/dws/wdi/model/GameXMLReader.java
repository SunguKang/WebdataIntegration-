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
import java.util.*;

import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.NodeList;

/**
 * A {@link XMLMatchableReader} for {@link Game}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class GameXMLReader extends XMLMatchableReader<Game, Attribute> implements
FusibleFactory<Game, Attribute> {

	private static final Logger logger = WinterLogManager.activateLogger("default");

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
	Float soldUnits = null;
	try{
		soldUnits = Float.parseFloat(getValueFromChildElement(node, "globallySoldUnits"));
	}catch (NumberFormatException e){
		logger.debug("Got NumberFormatException for soldUnits game: " + game.getName());
	}
	game.setGloballySoldUnits(soldUnits);

	Float criticScore = null;
	try{
		criticScore = Float.parseFloat(getValueFromChildElement(node, "criticScore"));
	} catch	(NumberFormatException e){
		logger.debug("Got NumberFormatException for criticScore game: " + game.getName());
	}
	game.setCriticScore(criticScore);

	Float userScore = null;
	try{
		userScore = Float.parseFloat(getValueFromChildElement(node, "userScore"));
	} catch (NumberFormatException e){
		logger.debug("Got NumberFormatException for userScore game: " + game.getName());
	}
	game.setUserScore(userScore);
	game.setSummary(getValueFromChildElement(node, "summary"));
	game.setRating(getValueFromChildElement(node, "rating"));
	game.setSeries(getValueFromChildElement(node, "series"));
	game.setNamePreprocessed(getValueFromChildElement(node, "name_preprocessed"));
	
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


	@Override
	protected <ItemType extends Matchable> List<ItemType> getObjectListFromChildElement(Node node, String childName, String objectNodeName, XMLMatchableReader<ItemType, Attribute> factory, String provenanceInfo) {
		if(node instanceof Element) {
			Element nodeElement = (Element)node;
			Node listAttribute = nodeElement.getElementsByTagName(childName).item(0); // e.g. get publishers node list
			NodeList children = listAttribute.getChildNodes();// e.g. list of publishers, i.e. each publisher as a list

			// get all child nodes
			// NodeList children = node.getChildNodes();
			// iterate over the child nodes until the node with childName is found
			//NodeList children = node.getChildNodes();

			// iterate over the child nodes until the node with childName is found
			List<ItemType> values = new LinkedList<>();
			for (int j = 0; j < children.getLength(); j++) {
				Node child = children.item(j);
				//NodeList childList = children.item(j).getChildNodes();
				// Node subchild = childList.item(0);
				// check the node type and name
//				if (child.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
//						&& child.getNodeName().equals(childName)) {

				// prepare a list to hold all values
				values.add(factory.createModelFromElement(child, provenanceInfo));
				// iterate the value nodes
//				for (int i = 0; i < child.getChildNodes().getLength(); i++) {
//					Node valueNode = child.getChildNodes().item(i);
//
//					// check the node type and name
//					if (valueNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
//							&& valueNode.getNodeName().equals(objectNodeName)) {
						// add the value
						//values.add(factory.createModelFromElement(valueNode, provenanceInfo));
					//}
				}
					return values;


			}


		return null;
		}


}
