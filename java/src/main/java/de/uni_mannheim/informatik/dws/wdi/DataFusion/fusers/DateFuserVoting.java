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
package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import java.time.LocalDateTime;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
/**
 * {@link AttributeValueFuser} for the date of {@link Game}s.
 *
 */
public class DateFuserVoting extends AttributeValueFuser<LocalDateTime, Game, Attribute> {

	public DateFuserVoting() {
		super(new Voting<LocalDateTime, Game, Attribute>());
	}

    @Override
    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        FusedValue<LocalDateTime, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
		gameFused.setPublicationDate(fused.getValue());
		gameFused.setAttributeProvenance(Game.PUBLICATIONDATE, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
        return game.hasValue(Game.PUBLICATIONDATE);
    }

    public LocalDateTime getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
		return game.getPublicationDate();
	}
//	@Override
//	public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
//		return record.hasValue(Game.DATE);
//	}
//
//	@Override
//	public LocalDateTime getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
//		return record.getDate();
//	}
//
//	@Override
//	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
//		FusedValue<LocalDateTime, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
//		fusedRecord.setDate(fused.getValue());
//		fusedRecord.setAttributeProvenance(Game.DATE, fused.getOriginalIds());
//	}

}
