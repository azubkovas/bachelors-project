package bachelors.project.repr.cond;

import bachelors.project.ChangeFinder;
import bachelors.project.repr.changepattern.ChangePattern;
import bachelors.project.repr.changepattern.UpdatePattern;
import bachelors.project.util.DiffData;
import bachelors.project.util.JoernManager;
import com.github.gumtreediff.actions.model.Action;

import java.util.HashMap;
import java.util.Map;

public class ExistentialQuantification extends Condition {
    private final ChangePattern pattern;
    private final Condition condition;

    public ExistentialQuantification(ChangePattern pattern, Condition condition) {
        this.pattern = pattern;
        this.condition = condition;
    }

    @Override
    public Object evaluate(Map<String, Object> variables, DiffData diffData) {
        for (Action action : diffData.getAllActions()) {
            if (ChangeFinder.actionMatchesChangePattern(action, pattern, variables)) {
                Map<String, Object> innerVariables = new HashMap<>(variables);
                ChangeFinder.populateVariables(innerVariables, pattern, action);
                if ((Boolean) condition.evaluate(innerVariables, diffData)) return true;
            }
        }
        return false;
    }
}
