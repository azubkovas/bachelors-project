package org.defdiff.deflang.cond;

public enum Operator {
    EQ, NEQ, GT, LT, GTE, LTE;

    public static Operator fromString(String operator) {
        return switch (operator) {
            case "==" -> EQ;
            case "!=" -> NEQ;
            case ">" -> GT;
            case "<" -> LT;
            case ">=" -> GTE;
            case "<=" -> LTE;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
