package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class SeriesEvaluationRule extends EvaluationRule<Game, Attribute> {

    SimilarityMeasure<String> sim;

    Double threshold;

    public SeriesEvaluationRule(SimilarityMeasure<String> sim, Double threshold) {
        this.sim = sim;
        this.threshold = threshold;
    }

    public SeriesEvaluationRule() {
        this(0.9);
    }

    public SeriesEvaluationRule(Double threshold) {
        this(new LevenshteinSimilarity(), threshold);
    }

    @Override
    public boolean isEqual(Game record1, Game record2, Attribute attribute) {
        String string1 = record1.getSeries();
        String string2 = record2.getSeries();
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
