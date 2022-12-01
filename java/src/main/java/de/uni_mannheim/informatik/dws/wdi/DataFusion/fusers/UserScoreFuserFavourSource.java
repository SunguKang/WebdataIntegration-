package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
//TODO remove unused imports

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class UserScoreFuserFavourSource extends AttributeValueFuser<Float, Game, Attribute> {
    //attribute in dataset A and C
//	TODO Test
    public UserScoreFuserFavourSource() {
		super(new FavourSources<Float, Game, Attribute>());
	}

	@Override
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<Float, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setUserScore(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.USERSCORE, fused.getOriginalIds());
	}
	
////	 Took what was in the excercise
//	deleted
	//import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
	//import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
//    @Override
//    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//        FusedValue<Float, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
//		gameFused.setUserScore(fused.getValue());
//		gameFused.setAttributeProvenance(Game.USERSCORE, fused.getOriginalIds());
//    }


    @Override
    public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
        return game.hasValue(Game.USERSCORE);
    }

    public Float getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
		return game.getUserScore();
	}

//    @Override
//    public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//        // We probably don't need it
//        return null;
//    }
    
    
}
