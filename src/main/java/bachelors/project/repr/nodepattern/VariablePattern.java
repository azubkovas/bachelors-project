package bachelors.project.repr.nodepattern;

import com.github.gumtreediff.tree.Tree;

public class VariablePattern extends NodePattern {
    private final String name;
    private Tree correspondingNode;
    private NodePattern correspondingNodePattern;

    public VariablePattern(String name) {
        this.name = name;
    }

    public VariablePattern(String name, Tree correspondingNode) {
        this.name = name;
        this.correspondingNode = correspondingNode;
    }

    public VariablePattern(String name, NodePattern correspondingNodePattern) {
        this.name = name;
        this.correspondingNodePattern = correspondingNodePattern;
    }

    @Override
    public NodeType getNodeType() {
        return correspondingNodePattern.getNodeType();
    }

    @Override
    public boolean matchesNode(Tree node, VariableContainer variables) {
        if (correspondingNodePattern != null) {
            if (!variables.contains(name)) {
                variables.addVariable(this);
                correspondingNode = node;
            }
            return correspondingNodePattern.matchesNode(node, variables);
        }
        if (variables.contains(name)) {
//            correspondingNode = variables.get(name);
//            return correspondingNode == node;
            throw new RuntimeException("Not implemented yet!");
        } else {
            variables.addVariable(new VariablePattern(name, node));
            correspondingNode = node;
            return true;
        }
    }

    public String getName() {
        return name;
    }

    public Tree getCorrespondingNode() {
        return correspondingNode;
    }

    public NodePattern getCorrespondingNodePattern() {
        return correspondingNodePattern;
    }
}
