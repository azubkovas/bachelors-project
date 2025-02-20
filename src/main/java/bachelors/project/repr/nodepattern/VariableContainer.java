package bachelors.project.repr.nodepattern;

import java.util.HashMap;
import java.util.Map;

public class VariableContainer {
    private final Map<String, VariablePattern> variables;

    public VariableContainer() {
        this.variables = new HashMap<>();
    }

    public VariableContainer(VariableContainer variables) {
        this.variables = new HashMap<>(variables.variables);
    }

    public void addVariable(VariablePattern variablePattern) {
        this.variables.put(variablePattern.getName(), variablePattern);
    }

    public boolean contains(String name) {
        return this.variables.containsKey(name);
    }

    public VariablePattern get(String name) {
        return this.variables.get(name);
    }
}
