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

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("videogames");
	}

	@Override
	public Element createElementFromRecord(Game record, Document doc) {
		Element Game = doc.createElement("Game");

		Game.appendChild(createTextElement("id", record.getIdentifier(), doc));

		Game.appendChild(createTextElementWithProvenance("title",
				record.getTitle(),
				record.getMergedAttributeProvenance(Game.TITLE), doc));
		Game.appendChild(createTextElementWithProvenance("director",
				record.getDirector(),
				record.getMergedAttributeProvenance(Game.DIRECTOR), doc));
		Game.appendChild(createTextElementWithProvenance("date", record
				.getDate().toString(), record
				.getMergedAttributeProvenance(Game.DATE), doc));

		Game.appendChild(createActorsElement(record, doc));

		return Game;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

	protected Element createActorsElement(Game record, Document doc) {
		Element actorRoot = actorFormatter.createRootElement(doc);
		actorRoot.setAttribute("provenance",
				record.getMergedAttributeProvenance(Game.ACTORS));

		for (Actor a : record.getActors()) {
			actorRoot.appendChild(actorFormatter
					.createElementFromRecord(a, doc));
		}

		return actorRoot;
	}

}
