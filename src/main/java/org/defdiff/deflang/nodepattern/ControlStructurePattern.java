package org.defdiff.deflang.nodepattern;

import com.github.gumtreediff.tree.Tree;
import org.defdiff.deflang.VariableContainer;

public class ControlStructurePattern extends NodePattern {
    private final String controlStructureType;

    public ControlStructurePattern(String controlStructureType) {
        this.controlStructureType = controlStructureType;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.CONTROL_STRUCTURE;
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        return getNodeType().matches(node.getType().name) && (controlStructureType == null || controlStructureType.equals(node.getType().name));
    }
}
