package org.defdiff.deflang.nodepattern;

import org.defdiff.deflang.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class IdentifierPattern extends NodePattern {
    private NodePattern referee;

    @Override
    public NodeType getNodeType() {
        return NodeType.IDENTIFIER;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name) && checkNodeOfRequiredType(node);
    }
}
