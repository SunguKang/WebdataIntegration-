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
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Game}s.
 * 
 */
public class GameXMLFormatter extends XMLFormatter<Game> {

	GenreXMLFormatter genreFormatter = new GenreXMLFormatter();
	DeveloperXMLFormatter developerFormatter = new DeveloperXMLFormatter();
	PublisherXMLFormatter publisherFormatter = new PublisherXMLFormatter();

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("videogames");
	}

	@Override
	public Element createElementFromRecord(Game record, Document doc) {
		Element gameElem = doc.createElement("videogame");
		gameElem.appendChild(createTextElement("id", record.getIdentifier(), doc));
		gameElem.appendChild(createTextElement("name", record.getName(), doc));
		gameElem.appendChild(createTextElement("platform", record.getPlatform(), doc));
		gameElem.appendChild(createTextElement("publicationDate", record.getPublicationDate().toString(), doc));
		gameElem.appendChild(createTextElement("globallySoldUnits", record.getGloballySoldUnits().toString(), doc));
		gameElem.appendChild(createTextElement("criticScore", record.getCriticScore().toString(), doc));
		gameElem.appendChild(createTextElement("userScore", record.getUserScore().toString(), doc));
		gameElem.appendChild(createTextElement("rating", record.getRating(), doc));
		gameElem.appendChild(createTextElement("summary", record.getSummary(), doc));
		gameElem.appendChild(createTextElement("series", record.getSeries(), doc));

		gameElem.appendChild(createGenresElement(record, doc));
		gameElem.appendChild(createDevelopersElement(record, doc));
		gameElem.appendChild(createPublishersElement(record, doc));

		return gameElem;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

	protected Element createGenresElement(Game record, Document doc) {
		Element genreRoot = genreFormatter.createRootElement(doc);

		for (Genre genre : record.getGenres()) {
			genreRoot.appendChild(genreFormatter.createElementFromRecord(genre, doc));
		}

		return genreRoot;
	}

	protected Element createDevelopersElement(Game record, Document doc) {
		Element developerRoot = developerFormatter.createRootElement(doc);

		for (Developer developer : record.getDevelopers()) {
			developerRoot.appendChild(developerFormatter.createElementFromRecord(developer, doc));
		}

		return developerRoot;
	}

	protected Element createPublishersElement(Game record, Document doc) {
		Element publisherRoot = publisherFormatter.createRootElement(doc);

		for (Publisher publisher : record.getPublishers()) {
			publisherRoot.appendChild(publisherFormatter.createElementFromRecord(publisher, doc));
		}

		return publisherRoot;
	}

}
