package org.example;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.utils.Pair;
import org.example.util.JoernCient;
import org.example.util.PositionConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Non-essential changes:\n");
        System.out.println("Variable rename casualties:");
        printChanges(findVariableRenameCasualties("data/rename_casualties/BeforeVariableRename.java",
                "data/rename_casualties/AfterVariableRename.java"));
        System.out.println("\nMethod rename casualties:");
        printChanges(findMethodRenameCasualties("data/rename_casualties/BeforeMethodRename.java",
                "data/rename_casualties/AfterMethodRename.java"));
    }

    private static void printChanges(List<Action> changes) {
        for (Action change : changes) {
            System.out.println(change);
        }
    }

    private static EditScript getEditScript(String beforeFilePath, String afterFilePath) throws IOException {
        Run.initGenerators();
        Tree before = TreeGenerators.getInstance().getTree(beforeFilePath).getRoot();
        Tree after = TreeGenerators.getInstance().getTree(afterFilePath).getRoot();
        Matcher defaultMatcher = Matchers.getInstance().getMatcher();
        MappingStore mappings = defaultMatcher.match(before, after);
        EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator();
        return editScriptGenerator.computeActions(mappings);
    }

    private static List<Action> findVariableRenameCasualties(String beforeFilePath, String afterFilePath) throws IOException {
        List<Action> renameCasualtyChanges = new ArrayList<>();
        EditScript actions = getEditScript(beforeFilePath, afterFilePath);
        for (Action action : actions) {
            if (action instanceof Update update) {
                if (update.getNode().getParent().getType().name.equals("VariableDeclarationFragment") && update.getNode().getParent().getChildPosition(update.getNode()) == 0) {
                    String prevName = update.getNode().getLabel();
                    String newName = update.getValue();
                    Pair<Integer, Integer> declLocation = PositionConverter.getLineAndColumn(beforeFilePath, update.getNode().getParent().getParent().getPos());
                    for (Action act : actions) {
                        if (act instanceof Update upd && upd != update && upd.getNode().getLabel().equals(prevName) &&
                                upd.getValue().equals(newName)) {
                            Pair<Integer, Integer> usageLocation = PositionConverter.getLineAndColumn(beforeFilePath, upd.getNode().getPos());
                            String queryOutput = JoernCient.executeQuery(("cpg.identifier.lineNumber(%d)." +
                                    "columnNumber(%d).head.refsTo.toSet == cpg.local.lineNumber(%d).columnNumber(%d).toSet").formatted(
                                    usageLocation.first, usageLocation.second, declLocation.first, declLocation.second
                            ), beforeFilePath);
                            if (queryOutput.trim().equals("true")) {
                                renameCasualtyChanges.add(upd);
                            }
                        }
                    }
                }
            }
        }
        return renameCasualtyChanges;
    }

    private static List<Action> findMethodRenameCasualties(String beforeFilePath, String afterFilePath) throws IOException {
        List<Action> renameCasualtyChanges = new ArrayList<>();
        EditScript actions = getEditScript(beforeFilePath, afterFilePath);
        for (Action action : actions) {
            if (action instanceof Update update) {
                if (update.getNode().getParent().getType().name.equals("MethodDeclaration")) {
                    String prevName = update.getNode().getLabel();
                    String newName = update.getValue();
                    String className = update.getNode().getParent().getParent().getChild(2).getLabel();
                    for (Action act : actions) {
                        if (act instanceof Update upd && upd != update &&
                                upd.getNode().getParent().getType().name.equals("MethodInvocation") &&
                                upd.getNode().getLabel().equals(prevName) && upd.getValue().equals(newName)) {
                            Pair<Integer, Integer> pos = PositionConverter.getLineAndColumn(beforeFilePath, upd.getNode().getPos());
                            String queryOutput = JoernCient.executeQuery(("cpg.call.name(\"%s\").lineNumber(%d)." +
                                    "columnNumber(%d).head.callee.head.typeDecl.head.name == \"%s\"").formatted(prevName, pos.first, pos.second, className), beforeFilePath);
                            if (queryOutput.trim().equals("true")) {
                                renameCasualtyChanges.add(upd);
                            }
                        }
                    }
                }
            }
        }
        return renameCasualtyChanges;
    }
}