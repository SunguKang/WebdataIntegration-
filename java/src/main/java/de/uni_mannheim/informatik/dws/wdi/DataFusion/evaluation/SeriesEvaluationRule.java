package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class SeriesEvaluationRule extends EvaluationRule<Game, Attribute> {
    
	@Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        if(record1.getSeries()==null && record2.getSeries()==null)
            return true;
        else if(record1.getSeries()==null ^ record2.getSeries()==null)
            return false;
        else
            return record1.getSeries().equals(record2.getSeries());
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }


}
