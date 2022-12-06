package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Developer;
import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.wdi.model.Genre;
import de.uni_mannheim.informatik.dws.wdi.model.Publisher;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

import java.util.List;

public class DevelopersFuserUnion extends AttributeValueFuser<List<Developer>, Game, Attribute> {

    public DevelopersFuserUnion() {
		//super(new SmartUnion<Developer, Game, Attribute>(new LevenshteinSimilarity(), 0.9));
		super(new Union<Developer, Game, Attribute>());
	}

    @Override
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<Developer>, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setDevelopers(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.DEVELOPERS, fused.getOriginalIds());
	}
  
    @Override
	public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		boolean hasValue = record.hasValue(Game.DEVELOPERS);
		if (hasValue)
			System.out.println(record.getName());
		return hasValue;
	}

	@Override
	public List<Developer> getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getDevelopers();
	}

}
