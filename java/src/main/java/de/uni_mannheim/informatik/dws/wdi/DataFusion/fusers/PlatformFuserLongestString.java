package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class PlatformFuserLongestString extends AttributeFuser<String, Game, Attribute> {
   
    public PlatformFuserLongestString() {
		super(new LongestString<Game, Attribute>());
	}

    @Override
    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        //TODO implement
        FusedValue<String, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
		gameFused.setPlatform(fused.getValue());
		gameFused.setAttributeProvenance(Game.PLATFORM, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
        return game.hasValue(Game.PLATFORM);
    }

    public String getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
		return game.getPlatform();
	}

    @Override
    public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        return null;
    }

}
