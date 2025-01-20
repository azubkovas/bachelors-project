package bachelors.project.util;

import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.TreeGenerator;
import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class GumtreeClient {
    public static DiffData getDiffData(String beforeFilePath, String afterFilePath) throws IOException {
        Run.initGenerators();
        Tree before = TreeGenerators.getInstance().getTree(beforeFilePath).getRoot();
        Tree after = TreeGenerators.getInstance().getTree(afterFilePath).getRoot();
        Matcher defaultMatcher = Matchers.getInstance().getMatcher();
        MappingStore mappings = defaultMatcher.match(before, after);
        EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator();
        return new DiffData(before, after, beforeFilePath, afterFilePath, mappings, editScriptGenerator.computeActions(mappings));
    }

    public static <T extends TreeGenerator> DiffData getDiffData(String beforeFilePath, String afterFilePath, Class<T> generatorClassToUse) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        TreeGenerator generator = generatorClassToUse.getConstructor().newInstance();
        Tree before = generator.generateFrom().file(beforeFilePath).getRoot();
        Tree after = generator.generateFrom().file(afterFilePath).getRoot();
        Matcher defaultMatcher = Matchers.getInstance().getMatcher();
        MappingStore mappings = defaultMatcher.match(before, after);
        EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator();
        return new DiffData(before, after, beforeFilePath, afterFilePath, mappings, editScriptGenerator.computeActions(mappings));
    }
}

