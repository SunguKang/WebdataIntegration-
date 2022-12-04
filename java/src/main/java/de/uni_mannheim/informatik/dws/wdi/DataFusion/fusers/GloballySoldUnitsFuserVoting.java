package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;
import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
public class GloballySoldUnitsFuserVoting extends AttributeValueFuser<Float, Game, Attribute> {

    public GloballySoldUnitsFuserVoting() {
		super(new Voting<Float, Game, Attribute>());
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
		fusedRecord.setGloballySoldUnits(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.GLOBALLYSOLDUNITS, fused.getOriginalIds());
	}

}
