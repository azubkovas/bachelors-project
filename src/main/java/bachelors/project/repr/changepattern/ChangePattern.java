package bachelors.project.repr.changepattern;

public abstract class ChangePattern {
    private final Target target;

    public ChangePattern(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }
}
