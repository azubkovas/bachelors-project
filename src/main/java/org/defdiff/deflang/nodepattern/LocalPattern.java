package org.defdiff.deflang.nodepattern;

import org.defdiff.deflang.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class LocalPattern extends NodePattern {
    @Override
    public NodeType getNodeType() {
        return NodeType.LOCAL;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name) && checkNodeOfRequiredType(node);
    }
}
