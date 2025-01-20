package bachelors.project;

import bachelors.project.change.finder.NonEssMethodRenameFinder;
import bachelors.project.change.finder.NonEssThisAdditionToFieldFinder;
import bachelors.project.change.finder.NonEssThisRemovalFromFieldFinder;
import bachelors.project.change.finder.NonEssVariableRenameFinder;
import com.github.gumtreediff.actions.model.Action;
import bachelors.project.util.GumtreeClient;
import com.github.gumtreediff.gen.srcml.SrcmlJavaTreeGenerator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println("Variable rename casualties:");
        System.out.println("Java");
        printChanges(new NonEssVariableRenameFinder().findChanges(GumtreeClient.getDiffData("data/rename_casualties/BeforeVariableRename.java",
                "data/rename_casualties/AfterVariableRename.java", SrcmlJavaTreeGenerator.class)));
        System.out.println("C++");
        printChanges(new NonEssVariableRenameFinder().findChanges(GumtreeClient.getDiffData("data/rename_casualties/BeforeVariableRename.cpp",
                "data/rename_casualties/AfterVariableRename.cpp")));

        System.out.println("\nMethod rename casualties:");
        printChanges(new NonEssMethodRenameFinder().findChanges(GumtreeClient.getDiffData("data/rename_casualties/BeforeMethodRename.java",
                "data/rename_casualties/AfterMethodRename.java")));

        System.out.println("\nTrivial this keyword removal from field access changes:");
        printChanges(new NonEssThisRemovalFromFieldFinder().findChanges(GumtreeClient.getDiffData("data/trivial_keywords/WithThisKeyword.java",
                "data/trivial_keywords/WithoutThisKeyword.java")));

        System.out.println("\nTrivial this keyword addition to field access changes:");
        printChanges(new NonEssThisAdditionToFieldFinder().findChanges(GumtreeClient.getDiffData("data/trivial_keywords/WithoutThisKeyword.java",
                "data/trivial_keywords/WithThisKeyword.java")));

    }

    private static void printChanges(List<Action> changes) {
        for (Action change : changes) {
            System.out.println(change);
        }
    }
}