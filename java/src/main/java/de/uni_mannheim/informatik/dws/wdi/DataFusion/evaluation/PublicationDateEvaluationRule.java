package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class PublicationDateEvaluationRule extends EvaluationRule<Game, Attribute> {
    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        if(record1.getPublicationDate()==null && record2.getPublicationDate()==null)
            return true;
        else if(record1.getPublicationDate()==null ^ record2.getPublicationDate()==null)
            return false;
        else
            return record1.getPublicationDate() == record2.getPublicationDate();
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }


}
