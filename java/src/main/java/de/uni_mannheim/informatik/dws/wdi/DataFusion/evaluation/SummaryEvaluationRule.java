package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class SummaryEvaluationRule extends EvaluationRule<Game, Attribute> {
	//is not perfect but I don't want to check for context without nltk
    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        if(record1.getSummary()==null && record2.getSummary()==null)
            return true;
        else if(record1.getSummary()==null ^ record2.getSummary()==null)
            return false;
        else
            return record1.getSummary().equals(record2.getSummary());
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }

}
