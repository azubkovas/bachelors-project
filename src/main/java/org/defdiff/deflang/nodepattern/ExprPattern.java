package org.defdiff.deflang.nodepattern;

import com.github.gumtreediff.tree.Tree;
import org.defdiff.deflang.VariableContainer;

public class ExprPattern extends NodePattern {

    @Override
    public NodeType getNodeType() {
        return NodeType.EXPRESSION;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name);
    }
}
