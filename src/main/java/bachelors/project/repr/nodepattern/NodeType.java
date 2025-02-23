package bachelors.project.repr.nodepattern;

import java.util.Map;

public enum NodeType {
    LITERAL, IDENTIFIER, LOCAL, METHOD, CALL, CONTROL_STRUCTURE, RETURN, BLOCK;
    private static final Map<NodeType, String> map = Map.of(
            LITERAL, "literal",
            IDENTIFIER, "name",
            LOCAL, "decl",
            METHOD, "function",
            CALL, "call",
            RETURN, "return",
            BLOCK, "block"
    );

    public boolean matches(String name) {
        return map.get(this).equals(name);
    }
}
