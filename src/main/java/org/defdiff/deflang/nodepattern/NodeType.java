package org.defdiff.deflang.nodepattern;

public enum NodeType {
    LITERAL, IDENTIFIER, LOCAL, METHOD, CALL, CONTROL_STRUCTURE, RETURN, BLOCK, MEMBER, ASSIGNMENT,
    FIELD_ACCESS, EXPRESSION, TYPE, PARAMETERS, PARAMETER, ARGUMENTS, ARGUMENT;

    public boolean matches(String name) {
        return switch (this) {
            case LITERAL -> name.equals("literal");
            case IDENTIFIER -> name.equals("name");
            case LOCAL, MEMBER -> name.equals("decl");
            case METHOD -> name.equals("function") || name.equals("constructor");
            case CALL -> name.equals("call");
            case CONTROL_STRUCTURE -> name.equals("if") || name.equals("else") || name.equals("while")
                    || name.equals("for") || name.equals("do") || name.equals("switch") || name.equals("try")
                    || name.equals("catch") || name.equals("finally");
            case RETURN -> name.equals("return");
            case BLOCK -> name.equals("block");
            case ASSIGNMENT -> name.equals("assign");
            case FIELD_ACCESS -> name.equals("fieldAccess");
            case EXPRESSION -> name.equals("expr");
            case TYPE -> name.equals("type");
            case PARAMETERS -> name.equals("parameter_list");
            case PARAMETER -> name.equals("parameter");
            case ARGUMENTS -> name.equals("argument_list");
            case ARGUMENT -> name.equals("argument");
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
