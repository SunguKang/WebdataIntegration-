package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.Similarities;

import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.date.YearSimilarity;

import java.time.LocalDateTime;

public class YearSimilarityEquals extends SimilarityMeasure<LocalDateTime> {

    private static final long serialVersionUID = 1L;
    private int maxDifference;

    /**
     *
     * maximal difference between two dates in years.
     */
    public YearSimilarityEquals() {
    }

    @Override
    public double calculate(LocalDateTime first, LocalDateTime second) {
        if (first == null || second == null) {
            return 0.0;
        } else {
            int diff = Math.abs(first.getYear()-second.getYear());
            if (diff == 0)
                return 1.0;
            else
                return 0.0;
        }
    }
}
