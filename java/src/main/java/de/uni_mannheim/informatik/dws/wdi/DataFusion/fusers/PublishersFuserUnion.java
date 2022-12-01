package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.wdi.model.Publisher;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.util.List;

public class PublishersFuserUnion extends AttributeValueFuser<List<Publisher>, Game, Attribute> {
  
   public PublishersFuserUnion() {
		super(new Union<Publisher, Game, Attribute>());
	}
   
////	  changed it so it is like in the excercise
//   import de.uni_mannheim.informatik.dws.wdi.model.Developer;
//   import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
//   import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
//    @Override
//    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//        FusedValue<List<Publisher>, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
//		gameFused.setPublishers(fused.getValue());
//		gameFused.setAttributeProvenance(Game.PUBLISHERS, fused.getOriginalIds());
//    }

    @Override
    //  //TODO implement comparison (if attribute values are too similar)
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<Publisher>, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setPublishers(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.DEVELOPERS, fused.getOriginalIds());
	}
  
    @Override
	public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Game.DEVELOPERS);
	}

	@Override
	public List<Publisher> getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getPublishers();
	}

//  //we propably don't need this 
//  @Override
//  public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//      return null;
//  }


}
