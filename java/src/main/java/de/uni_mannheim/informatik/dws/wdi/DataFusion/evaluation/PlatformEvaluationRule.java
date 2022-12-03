package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class PlatformEvaluationRule extends EvaluationRule<Game, Attribute> {
    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        if(record1.getPlatform()==null && record2.getPlatform()==null)
            return true;
        else if(record1.getPlatform()==null ^ record2.getPlatform()==null)
            return false;
        else
            return record1.getPlatform().equals(record2.getPlatform());
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }

    SimilarityMeasure<String> sim;

    Double threshold;

    public PlatformEvaluationRule(SimilarityMeasure<String> sim, Double threshold) {
        this.sim = sim;
        this.threshold = threshold;
    }

    public PlatformEvaluationRule() {
        this(0.9);
    }

    public PlatformEvaluationRule(Double threshold) {
        this(new LevenshteinSimilarity(), threshold);
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        String string1 = record1.getPlatform();
        String string2 = record2.getPlatform();
        if (string1 == null && string2 == null)
            return true;
        else if (string1 == null ^ string2 == null)
            return false;
        else if (string1.equals(string2))
            return true;
        else
            return isEqualSimilarity(string1, string2);
    }

    public boolean isEqualSimilarity(String string1, String string2){
        return sim.calculate(string1, string2) >= this.threshold;
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute) null);
    }


}
