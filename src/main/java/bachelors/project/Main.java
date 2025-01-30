package bachelors.project;

import bachelors.project.change.finder.ChangeFinder;
import bachelors.project.change.finder.NonEssVariableRenameFinder;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String beforeFilePath = "src/test/data/rename_casualties/BeforeVariableRename.java";
        String afterFilePath = "src/test/data/rename_casualties/AfterVariableRename.java";
        DiffData diffData = GumTreeClient.getDiffData(beforeFilePath, afterFilePath);
        ChangeFinder changeFinder = new NonEssVariableRenameFinder();
        List<Action> nonEssChanges = changeFinder.findChanges(diffData);
        printChanges(nonEssChanges);
    }

    private static void printChanges(List<Action> changes) {
        changes.forEach(System.out::println);
    }
}