package bachelors.project.repr.cond;

import bachelors.project.repr.VariableContainer;
import bachelors.project.repr.VariableValue;
import bachelors.project.repr.cond.eval.Variable;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernClient;

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
