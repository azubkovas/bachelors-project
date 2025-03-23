package org.defdiff.deflang.cond;

import org.defdiff.deflang.NotWellFormedException;
import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.cond.eval.Evaluatable;
import org.defdiff.deflang.cond.eval.Variable;
import org.defdiff.deflang.nodepattern.LiteralPattern;
import org.defdiff.util.DiffData;

public class BinaryCondition extends Condition {
    private final Evaluatable left;
    private final Evaluatable right;
    private final Operator operator;

    public BinaryCondition(Evaluatable left, Evaluatable right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) throws NotWellFormedException {
        Object leftValue, rightValue;
        leftValue = left.evaluate(variables, diffData);
        rightValue = right.evaluate(variables, diffData);
        if (left instanceof Variable leftVar && leftVar.evaluate(variables, diffData).getCorrespondingPattern() instanceof LiteralPattern lit)
            leftValue = lit.getValue();
        if (right instanceof Variable rightVar && rightVar.evaluate(variables, diffData).getCorrespondingPattern() instanceof LiteralPattern lit)
            rightValue = lit.getValue();
        return switch (operator) {
            case EQ -> leftValue.equals(rightValue);
            case NEQ -> !leftValue.equals(rightValue);
            case GT -> ((Comparable<Object>) leftValue).compareTo(rightValue) > 0;
            case LT -> ((Comparable<Object>) leftValue).compareTo(rightValue) < 0;
            case GTE -> ((Comparable<Object>) leftValue).compareTo(rightValue) >= 0;
            case LTE -> ((Comparable<Object>) leftValue).compareTo(rightValue) <= 0;
        };
    }
}

