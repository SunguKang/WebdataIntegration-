package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.wdi.model.Publisher;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFusionLogger;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PublishersFuserUnion extends AttributeValueFuser<List<Publisher>, Game, Attribute> {

	private ConflictResolutionFunction<List<Publisher>, Game, Attribute> conflictResolution;

   public PublishersFuserUnion() {
	   super(new SmartUnion<Publisher, Game, Attribute>(new LevenshteinSimilarity(), 0.9));
	}

    @Override
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<Publisher>, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setPublishers(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.DEVELOPERS, fused.getOriginalIds());
	}
  
    @Override
	public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Game.DEVELOPERS);
	}

	@Override
	public List<Publisher> getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getPublishers();
	}

}
