package bachelors.project;

import bachelors.project.change.finder.ChangeFinder;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String beforeFilePath = "", afterFilePath = "";
        DiffData diffData = GumTreeClient.getDiffData(beforeFilePath, afterFilePath);
        ChangeFinder changeFinder = null;
        List<Action> changes = changeFinder.findChanges(diffData);
    }

    private static void printChanges(List<Action> changes) {
        changes.forEach(System.out::println);
    }
}