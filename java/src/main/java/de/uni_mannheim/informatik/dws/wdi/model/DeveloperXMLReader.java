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

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

/**
 * A {@link XMLMatchableReader} for {@link Developer}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class DeveloperXMLReader extends XMLMatchableReader<Developer, Attribute> {

	@Override
	public Developer createModelFromElement(Node node, String provenanceInfo) {
		Node subnode = node.getChildNodes().item(0);
		if (subnode != null) {
			Node sibling = subnode.getNextSibling();
			if (sibling != null){
				Node siblingNodeList = sibling.getFirstChild();
				if (siblingNodeList != null){
					String developerName = siblingNodeList.getNodeValue();
					String id = developerName;

					// create the object with id and provenance information
					Developer developer = new Developer(id, provenanceInfo);

					// fill the attribute
					developer.setDeveloperName(developerName);

					return developer;
				}}}
		return null;
	}

}
