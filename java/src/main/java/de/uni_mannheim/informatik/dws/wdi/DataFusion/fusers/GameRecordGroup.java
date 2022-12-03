package de.uni_mannheim.informatik.dws.wdi.DataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.lang.reflect.Field;

public class GameRecordGroup<Game, SchemaElementType> extends RecordGroup {


    public GameRecordGroup() {
        super();
    }

    @Override
    public Correspondence getSchemaCorrespondenceForRecord(Matchable record, Processable schemaCorrespondences, Matchable schemaElement) {
        //super.getSchemaCorrespondenceForRecord(record, schemaCorrespondences, schemaElement);
        //String attributeIdentifier = attribute.getIdentifier();
        Class<?> clazz = record.getClass();
        Field currentField = null;
        try {
            currentField = clazz.getDeclaredField("asdf");
            currentField.setAccessible(true);

            Correspondence<Attribute, Matchable> schemaCor = new Correspondence<>();

            return schemaCor;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } finally {
            if (currentField != null)
                currentField.setAccessible(false);
        }

    }


}
