package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.HashSet;
import java.util.Set;

public class PublishersEvaluationRule extends EvaluationRule<Game, Attribute> {
    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        Set<String> attrListSet1 = new HashSet<>();
        Set<String> attrListSet2 = new HashSet<>();
        try {
            record1.getPublishers().forEach((x) -> {
                attrListSet1.add(x.getPublisherName());
            });
            record1.getPublishers().forEach((x) -> {
                attrListSet2.add(x.getPublisherName());
            });
            return attrListSet1.containsAll(attrListSet2) && attrListSet2.containsAll(attrListSet1);
        }
        catch (NullPointerException e){
                return false;
            }
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }


}
