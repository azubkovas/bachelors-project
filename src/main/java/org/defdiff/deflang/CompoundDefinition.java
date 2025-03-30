package org.defdiff.deflang;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.utils.Pair;
import org.defdiff.util.DiffData;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CompoundDefinition extends Definition {
    private final List<SimpleDefinition> simpleDefinitions;

    public CompoundDefinition(List<SimpleDefinition> simpleDefinitions, String definitionStr) {
        super(definitionStr);
        this.simpleDefinitions = simpleDefinitions;
    }

    @Override
    public Set<Action> filterChanges(DiffData diffData, VariableContainer variables) {
        SimpleDefinition firstDef = simpleDefinitions.get(0);
        Queue<Pair<Set<Action>, VariableContainer>> potentialGroups = new ConcurrentLinkedQueue<>();
        for (Action action : diffData.getAllActions()) {
            VariableContainer vars = new VariableContainer(variables);
            if (firstDef.matchesAction(action, diffData, vars)) {
                Set<Action> actions = new HashSet<>();
                actions.add(action);
                potentialGroups.add(new Pair<>(actions, vars));
            }
        }
        int index = 1;
        while (!potentialGroups.isEmpty() && index < simpleDefinitions.size()) {
            Pair<Set<Action>, VariableContainer> potentialGroup = potentialGroups.poll();
            Set<Action> actions = potentialGroup.first;
            VariableContainer vars = potentialGroup.second;
            SimpleDefinition def = simpleDefinitions.get(index);
            for (Action action : diffData.getAllActions()) {
                VariableContainer vars2 = new VariableContainer(vars);
                if (def.matchesAction(action, diffData, vars2)) {
                    Set<Action> newGroup = new HashSet<>(actions);
                    newGroup.add(action);
                    potentialGroups.add(new Pair<>(newGroup, vars2));
                }
            }
            if (!potentialGroups.isEmpty() && potentialGroups.peek().first.size() > index) index++;
        }
        return potentialGroups.stream().map(x -> x.first).reduce(new HashSet<>(), (x, y) -> {
            x.addAll(y);
            return x;
        });
    }
}
