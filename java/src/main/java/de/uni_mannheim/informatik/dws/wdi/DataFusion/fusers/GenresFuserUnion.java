package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

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

public class GenresFuserUnion extends AttributeValueFuser<List<Genre>, Game, Attribute> {

    public GenresFuserUnion() {
		super(new SmartUnion<Genre, Game, Attribute>(new LevenshteinSimilarity(), 0.9));
	}
    
    @Override
	public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<Genre>, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setGenres(fused.getValue());
		fusedRecord.setAttributeProvenance(Game.GENRES, fused.getOriginalIds());
	}
  
    @Override
	public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Game.GENRES);
	}

	@Override
	public List<Genre> getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getGenres();
	}


 	
//   //we propably don't need this 
//   @Override
//   public Double getConsistency(RecordGroup<Game, Attribute> recordGroup, EvaluationRule<Game, Attribute> evaluationRule, Processable<Correspondence<Attribute, Matchable>> processable, Attribute attribute) {
//       return null;
//   }
}
