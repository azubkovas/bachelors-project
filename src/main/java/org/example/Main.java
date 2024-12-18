package org.example;

import com.github.gumtreediff.actions.ChawatheScriptGenerator;
import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        System.out.println("Non-essential changes:");
        for (Action change : findRenameCasualties("./data/Before.java", "./data/After.java")) {
            System.out.println(change);
        }
    }

    private static List<Update> findRenameCasualties(String beforeFilePath, String afterFilePath) throws IOException {
        List<Update> renameCasualtyChanges = new ArrayList<>();
        JdtTreeGenerator generator = new JdtTreeGenerator();
        Tree before = generator.generateFrom().file(beforeFilePath).getRoot();
        Tree after = generator.generateFrom().file(afterFilePath).getRoot();
        Matcher defaultMatcher = Matchers.getInstance().getMatcher();
        MappingStore mappings = defaultMatcher.match(before, after);
//        for (Mapping x : mappings) {
//            System.out.println(x);
//        }
        EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator();
        EditScript actions = editScriptGenerator.computeActions(mappings);
        for (Action action : actions) {
//            System.out.println(action);
            if (action instanceof Update update) {
                if (update.getNode().getParent().getType().name.equals("VariableDeclarationFragment") && update.getNode().getParent().getChildPosition(update.getNode()) == 0) {
                    String prevName = update.getNode().getLabel();
//                    System.out.println(prevName);
                    String newName = update.getValue();
//                    System.out.println(newName);
//                    System.out.println(update.getNode().getParent().getParent().getParent());
                    for (Action act : actions) {
                        if (act instanceof Update upd && upd != update && upd.getNode().getParents()
                                .contains(update.getNode().getParent().getParent().getParent()) &&
                                upd.getNode().getLabel().equals(prevName) &&
                                upd.getValue().equals(newName)) {
//                            System.out.println("Radau gaidi");
                            renameCasualtyChanges.add(upd);
                        }
                    }
                }
            }
        }
        return renameCasualtyChanges;
    }
}