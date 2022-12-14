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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Publisher}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class PublisherXMLFormatter extends XMLFormatter<Publisher> {

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("publishers");
	}

	@Override
	public Element createElementFromRecord(Publisher record, Document doc) {
		if (record != null && doc != null){ // record.hasValue(Publisher.PublisherName)){
			Element publisher = doc.createElement("publisher");

			publisher.appendChild(createTextElement("publisherName", record.getPublisherName(), doc));
			return publisher;
		}
		else{
			return null;
	}
	}
}
