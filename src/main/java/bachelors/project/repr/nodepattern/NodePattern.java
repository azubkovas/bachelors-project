package bachelors.project.repr.nodepattern;

import com.github.gumtreediff.tree.Tree;

public abstract class NodePattern {
    private Tree correspondingNode;

    public void setCorrespondingNode(Tree node) {
        correspondingNode = node;
    }

    public Tree getCorrespondingNode() {
        return correspondingNode;
    }

    public NodePattern() {

    }

    public NodePattern(Tree node) {
        setCorrespondingNode(node);
    }

    public abstract NodeType getNodeType();
}
