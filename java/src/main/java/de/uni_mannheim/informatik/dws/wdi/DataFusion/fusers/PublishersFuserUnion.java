package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class PublishersFuserUnion extends AttributeFuser<List<Publisher>, Game, Attribute> {
  
   public PublishersFuserUnion() {
		super(new Union<Publisher, Game, Attribute>());
	}
   
    @Override
    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        //TODO implement
        FusedValue<List<Publisher>, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
		gameFused.setPublishers(fused.getValue());
		gameFused.setAttributeProvenance(Game.PUBLISHERS, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
           return game.hasValue(Game.PUBLISHERS);
    }

    public List<Publisher> getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
		return game.getPublishers();
	}

    @Override
    public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        return null;
    }


}
