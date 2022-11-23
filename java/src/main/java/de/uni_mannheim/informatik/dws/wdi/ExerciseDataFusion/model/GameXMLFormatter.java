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
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Game}s.
 * 
 *
 * 
 */
public class GameXMLFormatter extends XMLFormatter<Game> {

	ActorXMLFormatter actorFormatter = new ActorXMLFormatter();

	PublisherXMLFormatter publisherFormatter = new PublisherXMLFormatter();
	GenreXMLFormatter genreFormatter = new GenreXMLFormatter();
	DeveloperXMLFormatter developerFormatter = new DeveloperXMLFormatter();
	
	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("videogames");
	}

	@Override
	public Element createElementFromRecord(Game record, Document doc) {
		Element game = doc.createElement("game");
		game.appendChild(createTextElement("id", record.getIdentifier(), doc));

		game.appendChild(createTextElementWithProvenance("name",
				record.getName(),
				record.getMergedAttributeProvenance(Game.NAME), doc));
		game.appendChild(createTextElementWithProvenance("platform",
				record.getPlatform(),
				record.getMergedAttributeProvenance(Game.PLATFORM), doc));
		game.appendChild(createTextElementWithProvenance("publicationDate",
				record.getPublicationDate().toString(),
				record.getMergedAttributeProvenance(Game.PUBLICATIONDATE), doc));
		game.appendChild(createTextElementWithProvenance("globallySoldUnits",
				Float.toString(record.getGloballySoldUnits()),
				record.getMergedAttributeProvenance(Game.GLOBALLYSOLDUNITS), doc));
		game.appendChild(createTextElementWithProvenance("criticScore",
				Float.toString(record.getCriticScore()),
				record.getMergedAttributeProvenance(Game.CRITICSCORE), doc));
		game.appendChild(createTextElementWithProvenance("userScore",
				Float.toString(record.getUserScore()),
				record.getMergedAttributeProvenance(Game.USERSCORE), doc));
		game.appendChild(createTextElementWithProvenance("summary",
				record.getSummary(),
				record.getMergedAttributeProvenance(Game.SUMMARY), doc));
		game.appendChild(createTextElementWithProvenance("rating",
				record.getRating(),
				record.getMergedAttributeProvenance(Game.RATING), doc));
		game.appendChild(createTextElementWithProvenance("series",
				record.getSeries(),
				record.getMergedAttributeProvenance(Game.SERIES), doc));


		game.appendChild(createPublisherElement(record, doc));
		game.appendChild(createGenreElement(record, doc));
		game.appendChild(createDeveloperElement(record, doc));


		return game;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

	
	protected Element createPublisherElement(Game record, Document doc) {
	Element publishersRoot = publisherFormatter.createRootElement(doc);
	publishersRoot.setAttribute("provenance",
			record.getMergedAttributeProvenance(Game.PUBLISHERS));

	for (Publisher p : record.getPublishers()) {
		publishersRoot.appendChild(publisherFormatter
				.createElementFromRecord(p, doc));
	}
	
		return publishersRoot;
	}
	
	protected Element createGenreElement(Game record, Document doc) {
		Element genresRoot = genreFormatter.createRootElement(doc);
		genresRoot.setAttribute("provenance",
				record.getMergedAttributeProvenance(Game.GENRES));
	
		for (Genre g : record.getGenres()) {
			genresRoot.appendChild(genreFormatter
					.createElementFromRecord(g, doc));
		}
	
		return genresRoot;
	}
	
	protected Element createDeveloperElement(Game record, Document doc) {
		Element developersRoot = developerFormatter.createRootElement(doc);
		developersRoot.setAttribute("provenance",
				record.getMergedAttributeProvenance(Game.DEVELOPERS));
	
		for (Developer d : record.getDevelopers()) {
			developersRoot.appendChild(developerFormatter
					.createElementFromRecord(d, doc));
		}
	
		return developersRoot;
	}
}
