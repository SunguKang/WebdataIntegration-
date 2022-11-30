package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.time.LocalDateTime;
import java.util.Collection;

public class DateConflictResolution<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends
    ConflictResolutionFunction<LocalDateTime, RecordType, SchemaElementType> {

    @Override
    public FusedValue<LocalDateTime, RecordType, SchemaElementType> resolveConflict(
            Collection<FusibleValue<LocalDateTime, RecordType, SchemaElementType>> values) {

        FusibleValue<LocalDateTime, RecordType, SchemaElementType> currentFusedDate = null;
        // TODO add trustscore of source
        for(FusibleValue<LocalDateTime, RecordType, SchemaElementType> value : values) {
            // in the following line the first of a year is being handled separately as it was a filler value during
            // schema matching
            LocalDateTime valueDate = value.getValue();
            if(currentFusedDate==null || (valueDate.isBefore(currentFusedDate.getValue()) && !(
                    (valueDate.getYear() == currentFusedDate.getValue().getYear()) &&
                            (valueDate.getDayOfMonth() == 1) && (valueDate.getMonthValue() == 1)))
                  ) {
                currentFusedDate = value;
            }
            else if (valueDate.isAfter(currentFusedDate.getValue()) &&  (
                    valueDate.getYear() == currentFusedDate.getValue().getYear()) &&
                    (currentFusedDate.getValue().getDayOfMonth() == 1) && (currentFusedDate.getValue().getMonthValue() == 1)){
                currentFusedDate = value;
            }

        }

        return new FusedValue<>(currentFusedDate);

        }
    }






