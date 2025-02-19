package bachelors.project.repr.changepattern;

import bachelors.project.repr.nodepattern.NodePattern;

public class Target {
    private final NodePattern nodePattern;
    private final String label;

    public Target(NodePattern nodePattern, String label) {
        this.nodePattern = nodePattern;
        this.label = label;
    }

    public NodePattern getNodePattern() {
        return nodePattern;
    }

    public String getLabel() {
        return label;
    }
}
