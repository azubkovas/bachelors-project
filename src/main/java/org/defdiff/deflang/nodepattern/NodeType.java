package org.defdiff.deflang.nodepattern;

import java.util.Map;

public enum NodeType {
    LITERAL, IDENTIFIER, LOCAL, METHOD, CALL, CONTROL_STRUCTURE, RETURN, BLOCK, MEMBER, ASSIGNMENT,
    FIELD_ACCESS, EXPRESSION, TYPE, PARAMETERS;

    public boolean matches(String name) {
        return switch (this) {
            case LITERAL -> name.equals("literal");
            case IDENTIFIER -> name.equals("name");
            case LOCAL, MEMBER -> name.equals("decl");
            case METHOD -> name.equals("function");
            case CALL -> name.equals("call");
            case CONTROL_STRUCTURE -> name.equals("if_stmt") || name.equals("while_stmt") || name.equals("for_stmt");
            case RETURN -> name.equals("return");
            case BLOCK -> name.equals("block");
            case ASSIGNMENT -> name.equals("assign");
            case FIELD_ACCESS -> name.equals("fieldAccess");
            case EXPRESSION -> name.equals("expr");
            case TYPE -> name.equals("type");
            case PARAMETERS -> name.equals("parameter_list");
        };
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
