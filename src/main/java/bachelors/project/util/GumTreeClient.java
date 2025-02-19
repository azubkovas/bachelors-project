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
import com.github.gumtreediff.utils.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GumTreeClient {
    public static DiffData getDiffData(String prePatchRevisionPath, String postPatchRevisionPath) {
        DiffData diffData = new DiffData(prePatchRevisionPath, postPatchRevisionPath);
        List<Pair<String, String>> correspondingFilePairs = getCorrespondingFilePairs(prePatchRevisionPath, postPatchRevisionPath);
        for (Pair<String, String> pair : correspondingFilePairs) {
            diffData.addSingleFileDiffData(getSingleFileDiffData(pair.first, pair.second));
        }
        return diffData;
    }

    private static SingleFileDiffData getSingleFileDiffData(String beforeFilePath, String afterFilePath) {
        try {
            Tree before = getTree(beforeFilePath);
            Tree after = getTree(afterFilePath);
            Matcher defaultMatcher = Matchers.getInstance().getMatcher();
            MappingStore mappings = defaultMatcher.match(before, after);
            EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator();
            return new SingleFileDiffData(before, after, beforeFilePath, afterFilePath, mappings, editScriptGenerator.computeActions(mappings));
        } catch (Exception e) {
            return null;
        }
    }

    private static List<Pair<String, String>> getCorrespondingFilePairs(String prePatchRevisionPath, String postPatchRevisionPath) {
        List<Pair<String, String>> correspondingFilePairs = new ArrayList<>();
        Path beforeRevPath = Paths.get(prePatchRevisionPath);
        Path afterRevPath = Paths.get(postPatchRevisionPath);
        try (Stream<Path> beforePaths = Files.walk(beforeRevPath);
             Stream<Path> afterPaths = Files.walk(afterRevPath)) {
            Set<Path> beforeFiles = beforePaths.filter(Files::isRegularFile).collect(Collectors.toSet());
            Set<Path> afterFiles = afterPaths.filter(Files::isRegularFile).collect(Collectors.toSet());
            for (Path beforeFile : beforeFiles) {
                Path relativePath = beforeRevPath.relativize(beforeFile);
                for (Path afterFile : afterFiles) {
                    if (relativePath.equals(afterRevPath.relativize(afterFile))) {
                        correspondingFilePairs.add(new Pair<String, String>(beforeFile.toString(), afterFile.toString()));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return correspondingFilePairs;
    }

    public static Tree getFirstChildOfType(Tree parent, String type) {
        return parent.getChildren().stream().filter(child -> child.getType().name.equals(type)).findFirst().orElse(null);
    }

    public static Tree getLastChildOfType(Tree parent, String type) {
        List<Tree> listOfRelevantNodes =  parent.getChildren().stream().filter(child -> child.getType().name.equals(type)).toList();
        return listOfRelevantNodes.isEmpty() ? null : listOfRelevantNodes.get(listOfRelevantNodes.size() - 1);
    }

    public static Tree getTree(String filePath) throws IOException {
        TreeGenerator generator;
        if (filePath.endsWith(".java")) {
            generator = new SrcmlJavaTreeGenerator();
        } else if (filePath.endsWith(".cpp")) {
            generator = new SrcmlCppTreeGenerator();
        } else if (filePath.endsWith(".c") || filePath.endsWith(".h")) {
            generator = new SrcmlCTreeGenerator();
        } else if (filePath.endsWith(".cs")) {
            generator = new SrcmlCsTreeGenerator();
        } else {
            Run.initGenerators();
            generator = TreeGenerators.getInstance().get(filePath);
        }
        return generator.generateFrom().file(filePath).getRoot();
        // todo standardize names
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

