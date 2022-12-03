package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class FloatEvaluationRule extends EvaluationRule<Game, Attribute> {

    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        String attributeIdentifier = attribute.getIdentifier();
        Class<?> clazz = Game.class;
        Field currentField = null;
        try {
            currentField = clazz.getDeclaredField(attributeIdentifier);
            currentField.setAccessible(true);

            Float number1 = (Float) currentField.get(record1);
            Float number2 = (Float) currentField.get(record1);
            if (number1 == null && number2 == null)
                return true;
            else if (number1 == null ^ number2 == null)
                return false;
            else
                return number1.compareTo(number2) == 0;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (currentField != null)
                currentField.setAccessible(false);
        }
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
    	Attribute attribute = correspondence.getFirstRecord();
        return isEqual(record1, record2, attribute);
    }


}
