package bachelors.project.repr.cond;

import bachelors.project.repr.NotWellFormedException;

import java.util.Map;

public class Binary extends Condition {
    private final Condition left;
    private final Condition right;
    private final Operator operator;

    public Binary(Condition left, Condition right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Condition getLeft() {
        return left;
    }

    public Condition getRight() {
        return right;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public <T> T evaluate(Map<String, String> variables) throws NotWellFormedException {
        switch (operator) {
            case AND:
                if (left.evaluate(variables) instanceof Boolean leftValue && right.evaluate(variables) instanceof Boolean rightValue) {
                    return (T) (Boolean) (leftValue && rightValue);
                }
                throw new NotWellFormedException("AND operator can only be applied to boolean values");
            case OR:
                if (left.evaluate(variables) instanceof Boolean leftValue && right.evaluate(variables) instanceof Boolean rightValue) {
                    return (T) (Boolean) (leftValue || rightValue);
                }
                throw new NotWellFormedException("OR operator can only be applied to boolean values");

            case EQ:
                return (T) (Boolean) left.evaluate(variables).equals(right.evaluate(variables));
            case NEQ:
                return (T) (Boolean) !left.evaluate(variables).equals(right.evaluate(variables));
            case GT:
                try {
                    if (left.evaluate(variables) instanceof Comparable<?> leftValue
                            && right.evaluate(variables) instanceof Comparable<?> rightValue) {
                        return (T) (Boolean) ((((Comparable<Object>) leftValue)).compareTo(rightValue) > 0);
                    }
                    throw new NotWellFormedException("OR operator can only be applied to boolean values");
                } catch (Exception e) {
                    throw new NotWellFormedException("GT operator can only be applied to comparable values");
                }
            case LT:
                try {
                    if (left.evaluate(variables) instanceof Comparable<?> leftValue
                            && right.evaluate(variables) instanceof Comparable<?> rightValue) {
                        return (T) (Boolean) ((((Comparable<Object>) leftValue)).compareTo(rightValue) < 0);
                    }
                    throw new NotWellFormedException("OR operator can only be applied to boolean values");
                } catch (Exception e) {
                    throw new NotWellFormedException("GT operator can only be applied to comparable values");
                }
            case GTE:
                try {
                    if (left.evaluate(variables) instanceof Comparable<?> leftValue
                            && right.evaluate(variables) instanceof Comparable<?> rightValue) {
                        return (T) (Boolean) ((((Comparable<Object>) leftValue)).compareTo(rightValue) >= 0);
                    }
                    throw new NotWellFormedException("OR operator can only be applied to boolean values");
                } catch (Exception e) {
                    throw new NotWellFormedException("GT operator can only be applied to comparable values");
                }
            case LTE:
                try {
                    if (left.evaluate(variables) instanceof Comparable<?> leftValue
                            && right.evaluate(variables) instanceof Comparable<?> rightValue) {
                        return (T) (Boolean) ((((Comparable<Object>) leftValue)).compareTo(rightValue) <= 0);
                    }
                    throw new NotWellFormedException("OR operator can only be applied to boolean values");
                } catch (Exception e) {
                    throw new NotWellFormedException("GT operator can only be applied to comparable values");
                }
        }
        throw new NotWellFormedException("Unknown operator");
    }
}

