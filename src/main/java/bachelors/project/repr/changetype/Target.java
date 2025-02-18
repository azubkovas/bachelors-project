package bachelors.project.repr.changetype;

import bachelors.project.repr.nodetype.NodeType;

public class Target {
    private final NodeType nodeType;
    private final String label;

    public Target(NodeType nodeType, String label) {
        this.nodeType = nodeType;
        this.label = label;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getLabel() {
        return label;
    }
}
