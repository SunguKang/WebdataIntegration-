package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.lang.reflect.Field;

public class GloballySoldUnitsEvaluationRule extends EvaluationRule<Game, Attribute> {

    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        Float number1 = record1.getGloballySoldUnits();
        Float number2 = record2.getGloballySoldUnits();
        if (number1 == null && number2 == null)
            return true;
        else if (number1 == null ^ number2 == null)
            return false;
        else
            return number1.compareTo(number2) == 0;
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute) null);
    }

}
