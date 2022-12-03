package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.clustering.ConnectedComponentClusterer;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CriticScoreFuserFavourSource extends AttributeValueFuser<Float, Game, Attribute> {
    //attribute in dataset A and C

    public CriticScoreFuserFavourSource() {
		super(new FavourSources<Float, Game, Attribute>());
	}

    //TODO Test
	@Override
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<Float, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setCriticScore(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.CRITICSCORE, fused.getOriginalIds());
	}

    @Override
    public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
     	return game.hasValue(Game.CRITICSCORE);
	}

	public Float getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
		return game.getCriticScore();
	}

}
