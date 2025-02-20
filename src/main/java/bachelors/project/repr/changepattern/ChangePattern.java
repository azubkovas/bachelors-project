package bachelors.project.repr.changepattern;

import bachelors.project.repr.nodepattern.VariableContainer;
import com.github.gumtreediff.actions.model.Action;

public abstract class ChangePattern {
    public abstract boolean matchesAction(Action action, VariableContainer variables);
}
