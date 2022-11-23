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
 * {@link XMLFormatter} for {@link Developer}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class DeveloperXMLFormatter extends XMLFormatter<Developer> {

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("developers");
	}

	@Override
	public Element createElementFromRecord(Developer record, Document doc) {
		Element developer = doc.createElement("developer");

		developer.appendChild(createTextElement("developerName", record.getDeveloperName(), doc));
		return developer;
	}

}
