package org.defdiff.deflang.cond;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.deflang.cond.eval.Variable;
import org.defdiff.util.DiffData;
import org.defdiff.util.JoernClient;

import java.io.IOException;

public class RefCondition extends Condition {
    Variable referer;
    Variable referee;

    public RefCondition(Variable referer, Variable referee) {
        this.referer = referer;
        this.referee = referee;
    }

    @Override
    public Object evaluate(VariableContainer  variables, DiffData diffData) {
        VariableValue refererVal = (VariableValue) referer.evaluate(variables, diffData);
        VariableValue refereeVal = (VariableValue) referee.evaluate(variables, diffData);
        assert (refererVal != null && refereeVal != null);
        String refererQuery = refererVal.getCorrespondingPattern().getJoernQuery(refererVal.getCorrespondingNode());
        String refereeQuery = refereeVal.getCorrespondingPattern().getJoernQuery(refereeVal.getCorrespondingNode());
        String funcName = switch (refererVal.getCorrespondingPattern().getNodeType()) {
            case IDENTIFIER -> "refsTo";
            case CALL -> "callee";
            case FIELD_ACCESS -> "referencedMember";
            default -> throw new RuntimeException("Unsupported node type");
        };
        try {
            return JoernClient.getInstance().executeQuery(refererQuery + ".%s.toSet == ".formatted(funcName) + refereeQuery + ".toSet").equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
