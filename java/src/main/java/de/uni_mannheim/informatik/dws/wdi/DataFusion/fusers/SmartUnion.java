package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class SmartUnion<String, RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends
        ConflictResolutionFunction<List<String>, RecordType, SchemaElementType> {
    SimilarityMeasure<String> sim;
    Double threshold;

    public SmartUnion(SimilarityMeasure sim, Double threshold) {
        this.sim = sim;
        this.threshold = threshold;
    }


    public SmartUnion(SimilarityMeasure sim) {
        this(sim, 0.9);
    }


    @Override
    public FusedValue<List<String>, RecordType, SchemaElementType> resolveConflict(
            Collection<FusibleValue<List<String>, RecordType, SchemaElementType>> values) {

        HashSet<String> union = new HashSet<>();

//        for (FusibleValue<List<ValueType>, RecordType, SchemaElementType> value : values) {
//            union.addAll(value.getValue());
//        }
        // Cubic asymptotic runtime is not optimal of course, although not likely to occur very often
        for (FusibleValue<List<String>, RecordType, SchemaElementType> value : values) {
            List<String> valueList = value.getValue();
            for (String listVal : valueList){
                if (!union.contains(listVal)) {
                    union.add(listVal);
                }
                else{
                    // if a value already is in there with a high similarity it is not added
                    for (String unionElem  : union){
                        if (sim.calculate(unionElem, listVal) < this.threshold){
                            union.add(listVal);
                        }
                    }
                }
            }
        }

        List<String> list = new LinkedList<>(union);
        FusedValue<List<String>, RecordType, SchemaElementType> fused = new FusedValue<>(list);

        for (FusibleValue<List<String>, RecordType, SchemaElementType> value : values) {
            fused.addOriginalRecord(value);
        }

        return fused;
    }

}
