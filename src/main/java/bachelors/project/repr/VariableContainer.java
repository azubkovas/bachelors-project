package bachelors.project.repr;

import java.util.HashMap;
import java.util.Map;

public class VariableContainer {
    private final Map<String, VariableValue> variables;

    public VariableContainer() {
        this.variables = new HashMap<>();
    }

    public VariableContainer(VariableContainer variables) {
        this.variables = new HashMap<>(variables.variables);
    }

    public void addVariable(String name, VariableValue variableValue) {
        this.variables.put(name, variableValue);
    }

    public boolean contains(String name) {
        return this.variables.containsKey(name);
    }

    public VariableValue get(String name) {
        return this.variables.get(name);
    }
}
