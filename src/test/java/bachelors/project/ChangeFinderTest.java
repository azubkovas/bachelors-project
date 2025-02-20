package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.changepattern.UpdatePattern;
import bachelors.project.repr.cond.*;
import bachelors.project.repr.nodepattern.*;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeFinderTest {

    @Test
    void testFindChangesWithDummyExample() throws IOException {
        // UPDATE LITERAL x -> y | x == ""Hello, World!"" && y == ""Hello, Friends!""
        DiffData diffData = GumTreeClient.getDiffData("src/test/data/pre_patch/SampleProject", "src/test/data/post_patch/SampleProject");
        Definition definition = new Definition(
                new UpdatePattern(
                        new Literal(null),
                        "x",
                        "y"
                ),
                new Binary(
                        new Binary(
                                new Variable("x"),
                                "\"Hello, World!\"",
                                Operator.EQ
                        ),
                        new Binary(
                                new Variable("y"),
                                "\"Hello, Friends!\"",
                                Operator.EQ
                        ),
                        Operator.AND
                ));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.size());
    }

    @Test
    void testFindChangesWithJavaVariableRenameExample() throws IOException {
        // UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i.refersTo(l));
        DiffData diffData = GumTreeClient.getDiffData(
                "src/test/data/rename_casualties/java/BeforeVariableRename.java",
                "src/test/data/rename_casualties/java/AfterVariableRename.java");
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("i", new Identifier()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("l", new Local()),
                                "x",
                                "y"
                        ),
                        new RefCondition(new Variable("i"), new Variable("l"))
                )));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.size());
    }

    @Test
    void testFindChangesWithCppVariableRenameExample() throws IOException {
        //UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i.refersTo(l));
        DiffData diffData = GumTreeClient.getDiffData(
                "src/test/data/rename_casualties/c++/BeforeVariableRename.cpp",
                "src/test/data/rename_casualties/c++/AfterVariableRename.cpp");
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("i", new Identifier()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("l", new Local()),
                                "x",
                                "y"
                        ),
                        new RefCondition(new Variable("i"), new Variable("l"))
                )));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.size());
    }

    @Test
    void testFindChangesWithCVariableRenameExample() throws IOException {
        // UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i.refersTo(l));
        DiffData diffData = GumTreeClient.getDiffData(
                "src/test/data/rename_casualties/c/BeforeVariableRename.c",
                "src/test/data/rename_casualties/c/AfterVariableRename.c");
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("i", new Identifier()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("l", new Local()),
                                "x",
                                "y"
                        ),
                        new RefCondition(new Variable("i"), new Variable("l"))
                )));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(4, changes.size());
    }

    @Test
    void testFindChangesWithCsVariableRenameExample() throws IOException {
        // UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i.refersTo(l));
        DiffData diffData = GumTreeClient.getDiffData(
                "src/test/data/rename_casualties/c#/BeforeVariableRename.cs",
                "src/test/data/rename_casualties/c#/AfterVariableRename.cs");
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("i", new Identifier()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("l", new Local()),
                                "x",
                                "y"
                        ),
                        new RefCondition(new Variable("i"), new Variable("l"))
                )));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.size());
    }

    @Test
    void testFindChangesWithJavaMethodRenameExample() throws IOException {
        // UPDATE CALL c x -> y | ∃(UPDATE METHOD m x -> y | c.refersTo(m));
        DiffData diffData = GumTreeClient.getDiffData(
                "src/test/data/rename_casualties/java/BeforeMethodRename.java",
                "src/test/data/rename_casualties/java/AfterMethodRename.java"
        );
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("c", new Call()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("m", new Method()),
                                "x",
                                "y"
                        ),
                        new RefCondition(new Variable("c"), new Variable("m"))
                )));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.size());
    }

    @Test
    void testFindChangesWithCppMethodRenameExample() throws IOException {
        // UPDATE CALL c x -> y | ∃(UPDATE METHOD m x -> y | c.refersTo(m));
        DiffData diffData = GumTreeClient.getDiffData(
                "src/test/data/rename_casualties/c++/BeforeMethodRename.cpp",
                "src/test/data/rename_casualties/c++/AfterMethodRename.cpp"
        );
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("c", new Call()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("m", new Method()),
                                "x",
                                "y"
                        ),
                        new RefCondition(new Variable("c"), new Variable("m"))
                )));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.size());
    }

    @Test
    void testFindChangesWithCMethodRenameExample() throws IOException {
        // UPDATE CALL c x -> y | ∃(UPDATE METHOD m x -> y | c.refersTo(m));
        DiffData diffData = GumTreeClient.getDiffData(
                "src/test/data/rename_casualties/c/BeforeFunctionRename.c",
                "src/test/data/rename_casualties/c/AfterFunctionRename.c"
        );
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("c", new Call()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("m", new Method()),
                                "x",
                                "y"
                        ),
                        new RefCondition(new Variable("c"), new Variable("m"))
                )));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.size());
    }

//    @Test
//    void testFindChangesWithJavaTrivialKeywordAddition() throws IOException {
//        // REPLACE IDENTIFIER (FIELD x) BY FIELDACCESS (FIELD x)
//        DiffData diffData = GumTreeClient.getDiffData(
//                "src/test/data/trivial_keywords/WithoutThisKeyword.java",
//                "src/test/data/trivial_keywords/WithThisKeyword.java"
//        );
//        Definition definition = new Definition(
//                new UpdatePattern(
//                        new Target(
//                                new Identifier(),
//                                "x"
//                        ),
//                        "x",
//                        "y"
//                ),
//                new ExistentialQuantification(
//                        new UpdatePattern(
//                                new Target(
//                                        new Local(),
//                                        "l"
//                                ),
//                                "x",
//                                "y"
//                        ),
//                        new RefCondition(new Variable("i"), new Variable("l"))
//                ));
////        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
////        assertEquals(1, changes.size());
//    }
}