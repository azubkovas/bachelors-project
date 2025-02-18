package bachelors.project.repr.changetype;

import com.github.gumtreediff.actions.model.Action;

public abstract class ChangeType {
    private final Target target;

    public ChangeType(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }
}
