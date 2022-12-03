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

public class GloballysoldunitsFuserFavourSource extends AttributeValueFuser<Float, Game, Attribute> {
   
	public GloballysoldunitsFuserFavourSource() {
		super(new FavourSources<Float, Game, Attribute>());
	}

	@Override
	public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Game.GLOBALLYSOLDUNITS);
	}

	@Override
	public Float getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getGloballySoldUnits();
	}

	@Override
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<Float, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setGloballyySoldUnits(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.GLOBALLYSOLDUNITS, fused.getOriginalIds());
	}

//  @Override
//  public void fuse(RecordGroup<Game, Attribute> recordGroup, Game gameFused, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//      FusedValue<Float, Game, Attribute> fused = getFusedValue(recordGroup, processable, attribute);
//		gameFused.setGloballyySoldUnits(fused.getValue());
//		gameFused.setAttributeProvenance(Game.GLOBALLYSOLDUNITS, fused.getOriginalIds());
//  }
//
//  @Override
//  public boolean hasValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
//      return game.hasValue(Game.GLOBALLYSOLDUNITS);
//  }
//
//  public Float getValue(Game game, Correspondence<Attribute, Matchable> correspondence) {
//		return game.getGloballySoldUnits();
//	}
	
//    I think we don't need it
//    @Override
//    public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//        return null;
//    }
}
