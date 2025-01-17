package org.example;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.utils.Pair;
import org.example.util.DiffData;
import org.example.util.GumtreeClient;
import org.example.util.JoernCient;
import org.example.util.PositionConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Non-essential changes:\n");
//        System.out.println("Variable rename casualties:");
//        printChanges(findVariableRenameCasualties("data/rename_casualties/BeforeVariableRename.java",
//                "data/rename_casualties/AfterVariableRename.java"));
//        System.out.println("\nMethod rename casualties:");
//        printChanges(findMethodRenameCasualties("data/rename_casualties/BeforeMethodRename.java",
//                "data/rename_casualties/AfterMethodRename.java"));
        System.out.println("\nTrivial this keyword removal from field access changes:");
        printChanges(findTrivialThisKeywordRemovalFromFieldAccess("data/trivial_keywords/WithThisKeyword.java",
                "data/trivial_keywords/WithoutThisKeyword.java"));
    }

    private static void printChanges(List<Action> changes) {
        for (Action change : changes) {
            System.out.println(change);
        }
    }

    private static List<Action> findVariableRenameCasualties(String beforeFilePath, String afterFilePath) throws IOException {
        List<Action> renameCasualtyChanges = new ArrayList<>();
        EditScript actions = GumtreeClient.getDiffData(beforeFilePath, afterFilePath).getEditScript();
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
        EditScript actions = GumtreeClient.getDiffData(beforeFilePath, afterFilePath).getEditScript();
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

    private static List<Action> findTrivialThisKeywordRemovalFromFieldAccess(String beforeFilePath, String afterFilePath) throws IOException {
        List<Action> trivialThisKeywordChanges = new ArrayList<>();
        DiffData diffData = GumtreeClient.getDiffData(beforeFilePath, afterFilePath);
        EditScript actions = diffData.getEditScript();
        for (Action action : actions) {
            Optional<Action> parentDelete, siblingMove;
            if (action instanceof Delete delete && delete.getNode().getType().name.equals("ThisExpression") &&
                    delete.getNode().getParent().getType().name.equals("FieldAccess") && (parentDelete = actions.asList().stream().filter(act -> act instanceof Delete del && del.getNode() == delete.getNode().getParent()).findFirst()).isPresent() &&
                    (siblingMove = actions.asList().stream().filter(act -> act instanceof Move mov && mov.getNode().getParent() == delete.getNode().getParent()).findFirst()).isPresent()) {
                Pair<Integer, Integer> positionInBefore = PositionConverter.getLineAndColumn(beforeFilePath, parentDelete.get().getNode().getPos());
                String queryOutputBefore = JoernCient.executeQuery("cpg.fieldAccess.lineNumber(%d).columnNumber(%d).head.referencedMember.head.typeDecl.fullName"
                        .formatted(positionInBefore.first, positionInBefore.second), beforeFilePath);
//                System.out.println(queryOutputBefore);
                Tree equivalentNode = diffData.getMappings().getDstForSrc(siblingMove.get().getNode());
                Pair<Integer, Integer> positionInAfter = PositionConverter.getLineAndColumn(afterFilePath, equivalentNode.getPos());
                String queryOutputAfter = JoernCient.executeQuery("cpg.fieldAccess.lineNumber(%d).columnNumber(%d).head.referencedMember.head.typeDecl.fullName"
                        .formatted(positionInAfter.first, positionInAfter.second), afterFilePath);
//                System.out.println(queryOutputAfter);
                if (queryOutputBefore.equals(queryOutputAfter)) {
                    trivialThisKeywordChanges.addAll(List.of(delete, parentDelete.get(), siblingMove.get()));
                }
            }
        }
        return trivialThisKeywordChanges;
    }
}