package org.defdiff.deflang.nodepattern;

import org.defdiff.deflang.VariableContainer;
import com.github.gumtreediff.tree.Tree;

public class MemberPattern extends NodePattern {
    @Override
    public NodeType getNodeType() {
        return NodeType.MEMBER;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name) && checkNodeOfRequiredType(node);
    }
}
