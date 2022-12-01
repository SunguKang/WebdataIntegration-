package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

import java.lang.reflect.Field;

public class StringEvaluationRule extends EvaluationRule<Game, Attribute> {

    SimilarityMeasure<String> sim;

    Double threshold;

    public StringEvaluationRule(SimilarityMeasure<String> sim, Double threshold) {
        // TODO reason about the decision of this threshold
        if (sim != null)
            this.sim = sim;
        else
            this.sim = new TokenizingJaccardSimilarity();
        if (threshold != null)
            this.threshold = threshold;
        else
            this.threshold = 0.9;
    }

    public StringEvaluationRule() {
        this.sim = new TokenizingJaccardSimilarity();
        this.threshold = 0.9;
    }

    public StringEvaluationRule(Double threshold) {
        this.sim = new TokenizingJaccardSimilarity();
        this.threshold = threshold;
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        String attributeIdentifier = attribute.getIdentifier();
        Class<?> clazz = Game.class;
        Field currentField = null;
        try {
            currentField = clazz.getDeclaredField(attributeIdentifier);
            currentField.setAccessible(true);

            String string1 = (String) currentField.get(record1);
            String string2 = (String) currentField.get(record1);
            if (string1 == null && string2 == null)
                return true;
            else if (string1 == null ^ string2 == null)
                return false;
            else if (string1.equals(string2))
                return true;
            else
                return isEqualSimilarity(string1, string2);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (currentField != null)
                currentField.setAccessible(false);
        }


    }

    public boolean isEqualSimilarity(String string1, String string2){
        return sim.calculate(string1, string2) >= threshold;
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        Attribute attribute = correspondence.getFirstRecord();
        return isEqual(record1, record2, attribute);
    }


}
