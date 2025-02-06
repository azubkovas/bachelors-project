package bachelors.project.util;

import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.TreeGenerator;
import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.gen.srcml.SrcmlCTreeGenerator;
import com.github.gumtreediff.gen.srcml.SrcmlCppTreeGenerator;
import com.github.gumtreediff.gen.srcml.SrcmlCsTreeGenerator;
import com.github.gumtreediff.gen.srcml.SrcmlJavaTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;
import java.util.function.Predicate;

public class GumTreeClient {
    // Assumption: before and after files are of the same type.
    public static DiffData getDiffData(String beforeFilePath, String afterFilePath) throws IOException {
        Tree before = getTree(beforeFilePath);
        Tree after = getTree(afterFilePath);
        return getDiffData(beforeFilePath, afterFilePath, before, after);
    }

    private static DiffData getDiffData(String beforeFilePath, String afterFilePath, Tree before, Tree after) {
        Matcher defaultMatcher = Matchers.getInstance().getMatcher();
        MappingStore mappings = defaultMatcher.match(before, after);
        EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator();
        return new DiffData(before, after, beforeFilePath, afterFilePath, mappings, editScriptGenerator.computeActions(mappings));
    }

    public static Tree getFirstChildOfType(Tree parent, String type) {
        return parent.getChildren().stream().filter(child -> child.getType().name.equals(type)).findFirst().orElse(null);
    }

    public static Tree getTree(String filePath) throws IOException {
        TreeGenerator generator;
        if (filePath.endsWith(".java")) {
            generator = new SrcmlJavaTreeGenerator();
        }
        else if (filePath.endsWith(".cpp")) {
            generator = new SrcmlCppTreeGenerator();
        }
        else if (filePath.endsWith(".c") || filePath.endsWith(".h")) {
            generator = new SrcmlCTreeGenerator();
        }
        else if (filePath.endsWith(".cs")) {
            generator = new SrcmlCsTreeGenerator();
        }
        else {
            Run.initGenerators();
            generator = TreeGenerators.getInstance().get(filePath);
        }
        return generator.generateFrom().file(filePath).getRoot();
    }

    public static Tree findFirst(Tree tree, Predicate<Tree> predicate) {
        for (Tree next : tree.breadthFirst()) {
            if (predicate.test(next)) {
                return next;
            }
        }
        return null;
    }
}

