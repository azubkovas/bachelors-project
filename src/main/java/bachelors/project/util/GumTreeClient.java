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
import java.util.Optional;

public class GumTreeClient {
    // Assumption: before and after files are of the same type.
    public static DiffData getDiffData(String beforeFilePath, String afterFilePath) throws IOException {
        TreeGenerator generator;
        if (beforeFilePath.endsWith(".java")) {
            generator = new SrcmlJavaTreeGenerator();
        }
        else if (beforeFilePath.endsWith(".cpp")) {
            generator = new SrcmlCppTreeGenerator();
        }
        else if (beforeFilePath.endsWith(".c") || beforeFilePath.endsWith(".h")) {
            generator = new SrcmlCTreeGenerator();
        }
        else if (beforeFilePath.endsWith(".cs")) {
            generator = new SrcmlCsTreeGenerator();
        }
        else {
            Run.initGenerators();
            generator = TreeGenerators.getInstance().get(beforeFilePath);
        }
        Tree before = generator.generateFrom().file(beforeFilePath).getRoot();
        Tree after = generator.generateFrom().file(afterFilePath).getRoot();
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
}

