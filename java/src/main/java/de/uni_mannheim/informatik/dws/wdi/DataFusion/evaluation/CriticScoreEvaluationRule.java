package de.uni_mannheim.informatik.dws.wdi.DataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class CriticScoreEvaluationRule extends EvaluationRule<Game, Attribute> {
    @Override
    public boolean isEqual(Game game, Game recordType1, Attribute attribute) {
        // TODO implement
        return false;
    }

    @Override
    public boolean isEqual(Game game, Game recordType1, Correspondence<Attribute, Matchable> correspondence) {
        return false;
    }
}
