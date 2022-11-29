package de.uni_mannheim.informatik.dws.wdi.IdentityResolution.util;

import de.uni_mannheim.informatik.dws.winter.matching.rules.comparators.Comparator;

public class HelperClassComparatorWeightPair {

    Comparator comparator;
    Double weight;

    public HelperClassComparatorWeightPair(Comparator comparator, Double weight) {
        this.comparator = comparator;
        this.weight = weight;
    }

    public HelperClassComparatorWeightPair(Comparator comparator, Integer weight) {
        this.comparator = comparator;
        this.weight = Double.valueOf(weight);
    }

    public Comparator getComparator() {
        return comparator;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setWeight(int weight) {
        this.weight = (double) weight;
    }
}
