package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class NameFuserFavourSource extends AttributeValueFuser<String, Game, Attribute> {

    public NameFuserFavourSource() {

        super(new FavourSources<String, Game, Attribute>());
    }

    @Override
    public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
        FusedValue<String, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
        gameFused.setRating(fused.getValue());
        gameFused.setAttributeProvenance(Game.RATING, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
        return game.hasValue(Game.RATING);
    }

    @Override
    public String getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
        return game.getRating();
    }

}
