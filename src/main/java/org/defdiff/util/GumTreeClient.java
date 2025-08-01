package org.defdiff.util;

import com.github.gumtreediff.actions.Diff;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.matchers.*;
import com.github.gumtreediff.tree.DefaultTree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.utils.Pair;
import com.github.gumtreediff.tree.TypeSet;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GumTreeClient {
    public static DiffData getDiffData(Path prePatchRevisionPath, Path postPatchRevisionPath) {
        Run.initGenerators();
        DiffData diffData = new DiffData(prePatchRevisionPath, postPatchRevisionPath);
        for (Pair<File, File> pair : diffData.getComparator().getModifiedFiles()) {
            Diff fileDiff = getFileDiff(pair.first, pair.second, prePatchRevisionPath, postPatchRevisionPath);
            if (fileDiff != null) {
                diffData.addFileDiff(pair.first.toPath(), fileDiff);
            }
        }
        return diffData;
    }

    private static Diff getFileDiff(File beforeFile, File afterFile, Path prePatchRevisionPath, Path postPatchRevisionPath) {
        try {
            TreeContext before = TreeGenerators.getInstance().getTree(beforeFile.toString());
            preProcessTree(before.getRoot(), prePatchRevisionPath, beforeFile);
            TreeContext after = TreeGenerators.getInstance().getTree(afterFile.toString());
            preProcessTree(after.getRoot(), postPatchRevisionPath, afterFile);
            Matcher matcher = Matchers.getInstance().getMatcher();
//            matcher.setOption(ConfigurationOptions.bu_minsize, 1000000);
            MappingStore mappings = matcher.match(before.getRoot(), after.getRoot());
            EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator();
            return new Diff(before, after, mappings, editScriptGenerator.computeActions(mappings));
        } catch (Exception e) {
            return null;
        }
    }

    private static void preProcessTree(Tree tree, Path revisionPath, File file) {
        // add metadata to the tree
        tree.setMetadata("revisionPath", revisionPath);
        tree.setMetadata("file", file);
        // depth first traversal
        List<Tree> nodesWithNames = new ArrayList<>();
        List<Tree> nodesToRemove = new ArrayList<>();
        List<Tree> nestedIfStmts = new ArrayList<>();
        for (Tree node : tree.breadthFirst()) {
            if (node.getType().name.equals("function") || node.getType().name.equals("constructor") || node.getType().name.equals("class") ||
                    node.getType().name.equals("decl") || node.getType().name.equals("call") || node.getType().name.equals("type")) {
                nodesWithNames.add(node);
            } else if (node.getType().name.equals("block_content") && node.getParent().getType().name.equals("block")) {
                nodesToRemove.add(node);
            } else if (node.getType().name.equals("expr_stmt") && node.getChildren().size() == 1 && node.getChild(0).getType().name.equals("expr")) {
                nodesToRemove.add(node);
            } else if (node.getType().name.equals("expr") && node.getChildren().size() == 1 && node.getChild(0).getType().name.equals("call")) {
                nodesToRemove.add(node);
            } else if (node.getType().name.equals("decl_stmt") && node.getChildren().size() == 1 && node.getChild(0).getType().name.equals("decl")) {
                nodesToRemove.add(node);
            } else if (node.getType().name.equals("comment")) {
                nodesToRemove.add(node);
            } else if (node.getType().name.equals("block") && node.getParent().getType().name.equals("class")) {
                nodesToRemove.add(node);
            } else if (node.getType().name.equals("if_stmt") && node.getChildren().size() > 1) {
                nestedIfStmts.add(node);
            }
        }
        for (Tree node : nodesWithNames) {
            mergeName(node);
        }
        for (Tree node : nodesToRemove) {
            List<Tree> children = node.getChildren();
            List<Tree> siblings = node.getParent().getChildren();
            siblings.remove(node);
            siblings.addAll(children);
            node.getParent().setChildren(siblings);
        }
        for (Tree node : nestedIfStmts) {
            List<Tree> children = node.getChildren();
            node.setChildren(convertToNestedIf(children));
        }
    }

    private static List<Tree> convertToNestedIf(List<Tree> children) {
        if (children.size() == 1) {
            return children;
        }
        Tree topLevelIf = children.get(0);
        Tree elseNode = new DefaultTree(TypeSet.type("else"));
        Tree blockNode = new DefaultTree(TypeSet.type("block"));
        blockNode.setChildren(convertToNestedIf(children.subList(1, children.size())));
        elseNode.addChild(blockNode);
        topLevelIf.addChild(elseNode);
        return List.of(topLevelIf);
    }

    private static void mergeName(Tree node) {
        if (node.getType().name.equals("call")) {
            Tree nameNode = getFirstChildOfType(node, "name");
            if (nameNode != null) {
                StringBuilder label = new StringBuilder(nameNode.getLabel());
                if (!nameNode.getChildren().isEmpty()) {
                    label = new StringBuilder();
                    label.append(getLastChildOfType(nameNode, "name").getLabel());
                }
                node.setLabel(label.toString());
                List<Tree> children = node.getChildren();
                children.remove(nameNode);
                node.setChildren(children);
            }
            return;
        }
        Tree nameNode = getFirstChildOfType(node, "name");
        if (nameNode != null) {
            StringBuilder label = new StringBuilder(nameNode.getLabel());
            if (!nameNode.getChildren().isEmpty()) {
                label = new StringBuilder();
                for (Tree child : nameNode.getChildren()) {
                    if (child.getType().name.equals("parameter_list")) {
                        node.insertChild(child, node.getChildPosition(nameNode));
                    }
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

    public static Tree getFirstParentOfType(Tree child, String type) {
        Tree parent = child.getParent();
        while (parent != null && !type.equals(parent.getType().name)) {
            parent = parent.getParent();
        }
        return parent;
    }

    public static String getSuperClass(Tree classNode) {
        if (classNode == null)
            return null;
        Tree superListNode = getFirstChildOfType(classNode, "super_list");
        if (superListNode == null)
            return null;
        Tree extendsNode = getFirstChildOfType(superListNode, "extends");
        if (extendsNode == null)
            return null;
        Tree superNode = getFirstChildOfType(extendsNode, "super");
        if (superNode == null)
            return null;
        Tree nameNode = getFirstChildOfType(superNode, "name");
        if (nameNode == null)
            return null;
        if (nameNode.hasLabel() && !nameNode.getLabel().isBlank())
            return nameNode.getLabel();
        Tree nameNode2 = getLastChildOfType(nameNode, "name");
        if (nameNode2 != null && nameNode2.hasLabel() && !nameNode2.getLabel().isBlank())
            return nameNode2.getLabel();
        return null;
    }
}

