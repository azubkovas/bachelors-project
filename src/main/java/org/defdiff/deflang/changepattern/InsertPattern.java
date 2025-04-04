package org.defdiff.deflang.changepattern;

import org.defdiff.deflang.VariableContainer;
import org.defdiff.deflang.VariableValue;
import org.defdiff.deflang.nodepattern.LiteralPattern;
import org.defdiff.deflang.nodepattern.NodePattern;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.TreeInsert;

public class InsertPattern extends ChangePattern {
    private final NodePattern insertedPattern, parentPattern;
    private final String at;

    public InsertPattern(NodePattern insertedPattern, NodePattern parentPattern, String at) {
        this.insertedPattern = insertedPattern;
        this.parentPattern = parentPattern;
        this.at = at;
    }

    @Override
    public boolean matchesAction(Action action, VariableContainer variables) {
        if (action instanceof Insert ins) {
            if (at != null && !at.isEmpty()) {
                if (isNumber(at)) {
                    if (ins.getPosition() != Integer.parseInt(at)) {
                        return false;
                    }
                } else {
                    if (variables.contains(at)) {
                        if (!(variables.get(at).getCorrespondingPattern() instanceof LiteralPattern))
                            throw new RuntimeException();
                        if (!((LiteralPattern) variables.get(at).getCorrespondingPattern()).getValue().equals(String.valueOf(ins.getPosition()))) {
                            return false;
                        }
                    } else {
                        variables.addVariable(at, new VariableValue(new LiteralPattern(String.valueOf(ins.getPosition()))));
                    }
                }
            }
            return insertedPattern.matchesNode(ins.getNode(), variables) &&
                    parentPattern.matchesNode(ins.getParent(), variables);
        }
        else if (action instanceof TreeInsert insTree) {
            if (at != null && !at.isEmpty()) {
                if (isNumber(at)) {
                    if (insTree.getPosition() != Integer.parseInt(at)) {
                        return false;
                    }
                } else {
                    if (variables.contains(at)) {
                        if (!(variables.get(at).getCorrespondingPattern() instanceof LiteralPattern))
                            throw new RuntimeException();
                        if (!((LiteralPattern) variables.get(at).getCorrespondingPattern()).getValue().equals(String.valueOf(insTree.getPosition()))) {
                            return false;
                        }
                    } else {
                        variables.addVariable(at, new VariableValue(new LiteralPattern(String.valueOf(insTree.getPosition()))));
                    }
                }
            }
            return insertedPattern.matchesNode(insTree.getNode(), variables) &&
                    parentPattern.matchesNode(insTree.getParent(), variables);

        }
        else {
            return false;
        }
    }

    private boolean isNumber (String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
