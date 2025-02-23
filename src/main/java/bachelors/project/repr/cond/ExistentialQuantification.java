package bachelors.project.repr.cond;

import bachelors.project.repr.Definition;
import bachelors.project.repr.VariableContainer;
import bachelors.project.util.DiffData;
import com.github.gumtreediff.actions.model.Action;

public class ExistentialQuantification extends Condition {
    private final Definition definition;

    public ExistentialQuantification(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Object evaluate(VariableContainer variables, DiffData diffData) {
        for (Action action : diffData.getAllActions()) {
            VariableContainer innerVariables = new VariableContainer(variables);
            if (definition.matchesAction(action, diffData, innerVariables)) return true;
        }
        return false;
    }
}
