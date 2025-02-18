package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String prePatchRevisionPath = "";
        String postPatchRevisionPath = "";
        String nonEssentialChangeDefinitionsFilePath = "";
        List<Definition> definitions = Definition.getDefinitions(nonEssentialChangeDefinitionsFilePath);
        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
        List<Action> nonEssChanges = ChangeFinder.findChanges(diffData, definitions);
        printChanges(nonEssChanges);
    }

    private static void printChanges(List<Action> changes) {
        changes.forEach(System.out::println);
    }
}