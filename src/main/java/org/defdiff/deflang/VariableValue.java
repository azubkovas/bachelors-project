package org.defdiff.deflang;

import org.defdiff.deflang.nodepattern.NodePattern;
import com.github.gumtreediff.tree.Tree;

public class VariableValue {
    private final NodePattern correspondingPattern;
    private final Tree correspondingNode;

    public VariableValue(NodePattern correspondingPattern, Tree correspondingNode) {
        this.correspondingPattern = correspondingPattern;
        this.correspondingNode = correspondingNode;
    }

    public VariableValue(NodePattern correspondingPattern) {
        this(correspondingPattern, null);
    }

    public NodePattern getCorrespondingPattern() {
        return correspondingPattern;
    }

    public Tree getCorrespondingNode() {
        return correspondingNode;
    }
}
