package bachelors.project.util;

import com.github.gumtreediff.actions.Diff;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.utils.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GumTreeClient {
    public static DiffData getDiffData(Path prePatchRevisionPath, Path postPatchRevisionPath) {
        Run.initGenerators();
        DiffData diffData = new DiffData(prePatchRevisionPath, postPatchRevisionPath);
        for (Pair<File, File> pair : diffData.getComparator().getModifiedFiles()) {
            Diff fileDiff = getFileDiff(pair.first, pair.second);
            if (fileDiff != null) {
                diffData.addFileDiff(pair.first.toPath(), fileDiff);
            }
        }
        return diffData;
    }

    private static Diff getFileDiff(File beforeFile, File afterFile) {
        try {
            TreeContext before = TreeGenerators.getInstance().getTree(beforeFile.toString());
            preProcessTree(before.getRoot());
            TreeContext after = TreeGenerators.getInstance().getTree(afterFile.toString());
            preProcessTree(after.getRoot());
            Matcher defaultMatcher = Matchers.getInstance().getMatcher();
            MappingStore mappings = defaultMatcher.match(before.getRoot(), after.getRoot());
            EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator();
            return new Diff(before, after, mappings, editScriptGenerator.computeActions(mappings));
        } catch (Exception e) {
            return null;
        }
    }

    private static void preProcessTree(Tree tree) {
        // depth first traversal
        List<Tree> nodesToMerge = new ArrayList<>();
        List<Tree> nodesToRemove = new ArrayList<>();
        for (Tree node : tree.breadthFirst()) {
            if (node.getType().name.equals("function") || node.getType().name.equals("decl") || node.getType().name.equals("call")) {
                nodesToMerge.add(node);
            }
            if (node.getType().name.equals("block_content") && node.getParent().getType().name.equals("block")) {
                nodesToRemove.add(node);
            }
        }
        for (Tree node : nodesToMerge) {
            mergeName(node);
        }
        for (Tree node : nodesToRemove) {
            List<Tree> children = node.getChildren();
            List<Tree> siblings = node.getParent().getChildren();
            siblings.remove(node);
            siblings.addAll(children);
            node.getParent().setChildren(siblings);
        }
    }

    private static void mergeName(Tree node) {
        Tree nameNode = getFirstChildOfType(node, "name");
        if (nameNode != null) {
            StringBuilder label = new StringBuilder(nameNode.getLabel());
            if (!nameNode.getChildren().isEmpty()) {
                label = new StringBuilder();
                for (Tree child : nameNode.getChildren()) {
                    label.append(child.getLabel());
                }
            }
            node.setLabel(label.toString());
            List<Tree> children = node.getChildren();
            children.remove(nameNode);
            node.setChildren(children);
        }
    }

    public static Tree getFirstChildOfType(Tree parent, String type) {
        return parent.getChildren().stream().filter(child -> child.getType().name.equals(type)).findFirst().orElse(null);
    }

    public static Tree getLastChildOfType(Tree parent, String type) {
        List<Tree> listOfRelevantNodes = parent.getChildren().stream().filter(child -> child.getType().name.equals(type)).toList();
        return listOfRelevantNodes.isEmpty() ? null : listOfRelevantNodes.get(listOfRelevantNodes.size() - 1);
    }
}

