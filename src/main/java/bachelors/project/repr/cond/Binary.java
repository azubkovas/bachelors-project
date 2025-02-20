package bachelors.project.repr.cond;

import bachelors.project.repr.NotWellFormedException;
import bachelors.project.repr.nodepattern.VariableContainer;
import bachelors.project.util.DiffData;

import java.util.Map;

public class Binary extends Condition {
    private final Object left;
    private final Object right;
    private final Operator operator;

    public Binary(Object left, Object right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public  Object evaluate(VariableContainer variables, DiffData diffData) throws NotWellFormedException {
        Object leftValue, rightValue;
        leftValue = left;
        rightValue = right;
        if (left instanceof Evaluatable leftEval) leftValue = leftEval.evaluate(variables, diffData);
        if (right instanceof Evaluatable rightEval) rightValue = rightEval.evaluate(variables, diffData);
        return switch (operator) {
            case AND -> (Boolean) leftValue && (Boolean) rightValue;
            case OR -> (Boolean) leftValue || (Boolean) rightValue;
            case EQ -> leftValue.equals(rightValue);
            case NEQ -> !leftValue.equals(rightValue);
            case GT -> ((Comparable<Object>) leftValue).compareTo(rightValue) > 0;
            case LT -> ((Comparable<Object>) leftValue).compareTo(rightValue) < 0;
            case GTE -> ((Comparable<Object>) leftValue).compareTo(rightValue) >= 0;
            case LTE -> ((Comparable<Object>) leftValue).compareTo(rightValue) <= 0;
        };
    }
}

