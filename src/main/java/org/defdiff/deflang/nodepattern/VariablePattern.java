package org.defdiff.deflang.nodepattern;

import org.defdiff.deflang.NotWellFormedException;
import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import com.github.gumtreediff.tree.Tree;

public class VariablePattern extends NodePattern {
    private final String name;
    private final NodePattern correspondingNodePattern;

    public VariablePattern(String name, NodePattern correspondingNodePattern) {
        this.name = name;
        this.correspondingNodePattern = correspondingNodePattern;
    }

    public VariablePattern(String name) {
        this.name = name;
        this.correspondingNodePattern = null;
    }

    @Override
    public NodeType getNodeType() {
        return correspondingNodePattern.getNodeType();
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        if (correspondingNodePattern != null) {
            if (!variables.contains(name)) {
                variables.addVariable(name, new VariableValue(correspondingNodePattern, node));
            } else {
                throw new NotWellFormedException("Variable %s must have already been defined!".formatted(name));
            }
            return correspondingNodePattern.matchesNode(node, variables);
        } else {
            VariableValue value = variables.get(name);
            assert value != null;
            return value.getCorrespondingNode().equals(node);
        }
    }

    public String getName() {
        return name;
    }

    public NodePattern getCorrespondingNodePattern() {
        return correspondingNodePattern;
    }
}
