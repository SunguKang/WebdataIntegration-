package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class DateFuserMostRecent extends AttributeFuser<LocalDateTime, Game, Attribute> {
    
    public DateFuserMostRecent() {
		super(new MostRecent<LocalDateTime, Game, Attribute>());
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

    @Override
    public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        return null;
    }
// TODO
}