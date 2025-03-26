package org.defdiff.deflang.nodepattern;

import java.util.Map;

public enum NodeType {
    LITERAL, IDENTIFIER, LOCAL, METHOD, CALL, CONTROL_STRUCTURE, RETURN, BLOCK, MEMBER, ASSIGNMENT, FIELD_ACCESS, EXPRESSION;
    private static final Map<NodeType, String> map = Map.of(
            LITERAL, "literal",
            IDENTIFIER, "name",
            LOCAL, "decl",
            MEMBER, "decl",
            METHOD, "function",
            CALL, "call",
            RETURN, "return",
            BLOCK, "block",
            EXPRESSION, "expr"
    );

    public boolean matches(String name) {
        return map.get(this).equals(name);
    }

    public String getJoernName() {
        return switch (this) {
            case LITERAL -> "literal";
            case IDENTIFIER -> "identifier";
            case LOCAL -> "local";
            case METHOD -> "method";
            case CALL -> "call";
            case RETURN -> "return";
            case BLOCK -> "block";
            case MEMBER -> "member";
            case CONTROL_STRUCTURE -> "control_structure";
            case FIELD_ACCESS -> "fieldAccess";
            default -> throw new IllegalArgumentException("Invalid node type");
        };
    }
}
