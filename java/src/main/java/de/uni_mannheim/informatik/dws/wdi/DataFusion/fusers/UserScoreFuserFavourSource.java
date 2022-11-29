package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class UserScoreFuserFavourSource extends AttributeValueFuser<Float, Game, Attribute> {
    //attribute only in dataset A

    public UserScoreFuserFavourSource() {
		super(new FavourSources<Float, Game, Attribute>());
	}

    @Override
    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        // TODO implement
        FusedValue<Float, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
		gameFused.setUserScore(fused.getValue());
		gameFused.setAttributeProvenance(Game.USERSCORE, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
        return game.hasValue(Game.USERSCORE);
    }

    public Float getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
		return game.getUserScore();
	}


    @Override
    public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        return null;
    }
}
