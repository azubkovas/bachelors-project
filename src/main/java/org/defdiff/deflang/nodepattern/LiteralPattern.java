package org.defdiff.deflang.nodepattern;

import org.defdiff.deflang.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class LiteralPattern extends NodePattern {
    private final String value;

    public LiteralPattern(String value) {
        this.value = value;
    }

    public LiteralPattern() {
        this.value = null;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.LITERAL;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name) && (value == null || value.equals(node.getLabel()));
    }

    public String getValue() {
        return value;
    }
}
