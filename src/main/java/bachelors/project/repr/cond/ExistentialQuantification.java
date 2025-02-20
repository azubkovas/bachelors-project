package bachelors.project.repr.cond;

import bachelors.project.ChangeFinder;
import bachelors.project.repr.Definition;
import bachelors.project.repr.changepattern.ChangePattern;
import bachelors.project.repr.changepattern.UpdatePattern;
import bachelors.project.repr.nodepattern.VariableContainer;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernManager;
import com.github.gumtreediff.actions.model.Action;

import java.util.HashMap;
import java.util.Map;

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
