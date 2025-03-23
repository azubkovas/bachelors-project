package org.defdiff.deflang.nodepattern;

import org.defdiff.deflang.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class ReturnPattern extends NodePattern {
    private final NodePattern returnedPattern;

    public ReturnPattern(NodePattern returnedPattern) {
        this.returnedPattern = returnedPattern;
    }

    public ReturnPattern() {
        this.returnedPattern = null;
    }

    public NodeType getNodeType() {
        return NodeType.RETURN;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name) && (returnedPattern == null || returnedPattern.matchesNode(node.getChild(0), variables));
    }
}
