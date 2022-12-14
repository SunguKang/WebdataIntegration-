package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.LongestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class NameFuserLongestString extends AttributeValueFuser<String, Game, Attribute> {
  
	public NameFuserLongestString() {
		super(new LongestString<Game, Attribute>());
	}

	@Override
	public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Game.NAME);
	}

	@Override
	public String getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getName();
	}

	@Override
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<String, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setName(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.NAME,
				fused.getOriginalIds());
	}
//    public NameFuserLongestString() {
//		super(new LongestString<Game, Attribute>());
//	}
//   
//    @Override
//    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//        FusedValue<String, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
//		gameFused.setName(fused.getValue());
//		gameFused.setAttributeProvenance(Game.NAME, fused.getOriginalIds());
//    }
//
//    @Override
//    public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
//        return game.hasValue(Game.NAME);
//    }
//
//    public String getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
//		return game.getName();
//	}
//
//    Dont need
//    @Override
//    public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//        return null;
//    }


}
