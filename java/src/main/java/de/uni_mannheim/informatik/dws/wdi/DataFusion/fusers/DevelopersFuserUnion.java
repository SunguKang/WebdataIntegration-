package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Developer;
import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.util.List;

public class DevelopersFuserUnion extends AttributeValueFuser<List<Developer>, Game, Attribute> {

    public DevelopersFuserUnion() {
		super(new Union<Developer, Game, Attribute>());
	}

//	  changed it so it is like in the excercise
//    import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
//    import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule; 
//    @Override
//    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//        FusedValue<List<Developer>, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
//		gameFused.setDevelopers(fused.getValue());
//		gameFused.setAttributeProvenance(Game.DEVELOPERS, fused.getOriginalIds());
//    }
    
    @Override
    //  //TODO implement comparison (if attribute values are too similar)
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<Developer>, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setDevelopers(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.DEVELOPERS, fused.getOriginalIds());
	}
  
    @Override
	public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Game.DEVELOPERS);
	}

	@Override
	public List<Developer> getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getDevelopers();
	}


//    //we propably don't need this 
//    @Override
//    public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//        return null;
//    }
}
