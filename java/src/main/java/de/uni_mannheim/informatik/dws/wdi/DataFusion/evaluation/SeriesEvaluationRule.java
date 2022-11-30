package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class SeriesEvaluationRule extends EvaluationRule<Game, Attribute> {
    // TODO change maybe to other
    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        String string1 = record1.getSeries();
        String string2 = record2.getSeries();
        if(string1==null && record2.getPublicationDate()==null)
            return true;
        else if(record1.getPublicationDate()==null ^ record2.getPublicationDate()==null)
            return false;
        else
            return string1.equals(string2);
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }


}
