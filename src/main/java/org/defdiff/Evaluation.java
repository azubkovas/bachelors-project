//package org.defdiff;
//
//import com.github.gumtreediff.actions.model.Action;
//import com.github.gumtreediff.tree.Tree;
//import com.github.gumtreediff.utils.Pair;
//import org.defdiff.deflang.SimpleDefinition;
//import org.defdiff.deflang.ParserClient;
//import org.defdiff.util.DiffData;
//import org.defdiff.util.GumTreeClient;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//public class Evaluation {
//    public static void main(String[] args) throws IOException {
//        int definitionIndex = 0;
//        List<String> casualtyMethodNames = List.of("InternalNioInputBuffer", "setSocket", "parseRequestLine");
//        Path prePatchRevisionPath = Path.of("experimental_data/experiment2/patch_data/CVE-2011-1088/5c8560f3054982abaa476d87ec031c439d58d66e/pre_patch");
//        Path postPatchRevisionPath = Path.of("experimental_data/experiment2/patch_data/CVE-2011-1088/5c8560f3054982abaa476d87ec031c439d58d66e/post_patch");
//        Path nonEssentialChangeDefinitionsFilePath = Path.of("experimental_data/experiment2/definitions_for_exp.txt");
//
//        List<SimpleDefinition> simpleDefinitions = ParserClient.parseDefinitions(nonEssentialChangeDefinitionsFilePath);
//
//        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
//        ChangesContainer nonEssentialChanges = ChangeFinder.findChanges(diffData, simpleDefinitions);
//
//        System.out.println(calculateMatchingFraction(nonEssentialChanges.getChanges(simpleDefinitions.get(definitionIndex)), casualtyMethodNames));
////        System.out.println("Total number of non-essential changes: " + nonEssentialChanges.getAllChanges().size());
////        nonEssentialChanges.printChangeCounts();
////        System.out.println();
////        nonEssentialChanges.printChanges();
//    }
//
//    private static Pair<Integer, Integer> calculateMatchingFraction(Set<Action> changes, List<String> methodNames) {
//        List<String> changedMethods = new ArrayList<>();
//        for (Action change : changes) {
//            String changeMethodName = getChangeMethodName(change);
//            if (changeMethodName != null) {
//                changedMethods.add(changeMethodName);
//            }
//        }
//        int matches = 0;
//        for (String methodName : methodNames) {
//            if (changedMethods.contains(methodName)) {
//                matches++;
//            }
//        }
//        return new Pair<>(matches, methodNames.size());
//    }
//
//    private static String getChangeMethodName(Action change) {
//        Tree node = change.getNode();
//        while (node != null && !(node.getType().name.equals("function") || node.getType().name.equals("constructor"))) {
//            node = node.getParent();
//        }
//        if (node == null) {
//            return null;
//        } else return node.getLabel();
//    }
//}
