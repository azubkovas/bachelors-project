package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.changepattern.DeletePattern;
import bachelors.project.repr.changepattern.InsertPattern;
import bachelors.project.repr.changepattern.MovePattern;
import bachelors.project.repr.changepattern.UpdatePattern;
import bachelors.project.repr.cond.*;
import bachelors.project.repr.cond.eval.Literal;
import bachelors.project.repr.cond.eval.ParentEval;
import bachelors.project.repr.cond.eval.Variable;
import bachelors.project.repr.nodepattern.*;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeFinderTest {

    @Test
    void testFindChangesWithDummyExample() throws IOException {
        // UPDATE LITERAL x -> y | x == ""Hello, World!"" && y == ""Hello, Friends!""
        DiffData diffData = GumTreeClient.getDiffData(Path.of("src/test/data/pre_patch/SampleProject"), Path.of("src/test/data/post_patch/SampleProject"));
        Definition definition = new Definition(
                new UpdatePattern(
                        new LiteralPattern(null),
                        "x",
                        "y"
                ),
                new AndCondition(
                        new BinaryCondition(
                                new Variable("x"),
                                new Literal("\"Hello, World!\""),
                                Operator.EQ
                        ),
                        new BinaryCondition(
                                new Variable("y"),
                                new Literal("\"Hello, Friends!\""),
                                Operator.EQ
                        )
                ));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.size());
    }

    @Test
    void testFindChangesWithJavaVariableRenameExample() throws IOException {
        // UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i.refersTo(l));
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/java/BeforeVariableRename.java"),
                Path.of("src/test/data/rename_casualties/java/AfterVariableRename.java"));
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("i", new IdentifierPattern()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("l", new LocalPattern()),
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
                Path.of("src/test/data/rename_casualties/c++/BeforeVariableRename.cpp"),
                Path.of("src/test/data/rename_casualties/c++/AfterVariableRename.cpp"));
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("i", new IdentifierPattern()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("l", new LocalPattern()),
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
                Path.of("src/test/data/rename_casualties/c/BeforeVariableRename.c"),
                Path.of("src/test/data/rename_casualties/c/AfterVariableRename.c"));
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("i", new IdentifierPattern()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("l", new LocalPattern()),
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
                Path.of("src/test/data/rename_casualties/c#/BeforeVariableRename.cs"),
                Path.of("src/test/data/rename_casualties/c#/AfterVariableRename.cs"));
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("i", new IdentifierPattern()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("l", new LocalPattern()),
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
                Path.of("src/test/data/rename_casualties/java/BeforeMethodRename.java"),
                Path.of("src/test/data/rename_casualties/java/AfterMethodRename.java")
        );
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("c", new CallPattern()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("m", new MethodPattern()),
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
                Path.of("src/test/data/rename_casualties/c++/BeforeMethodRename.cpp"),
                Path.of("src/test/data/rename_casualties/c++/AfterMethodRename.cpp")
        );
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("c", new CallPattern()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("m", new MethodPattern()),
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
                Path.of("src/test/data/rename_casualties/c/BeforeFunctionRename.c"),
                Path.of("src/test/data/rename_casualties/c/AfterFunctionRename.c")
        );
        Definition definition = new Definition(
                new UpdatePattern(
                        new VariablePattern("c", new CallPattern()),
                        "x",
                        "y"
                ),
                new ExistentialQuantification(new Definition(
                        new UpdatePattern(
                                new VariablePattern("m", new MethodPattern()),
                                "x",
                                "y"
                        ),
                        new RefCondition(new Variable("c"), new Variable("m"))
                )));
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.size());
    }

    @Test
    void testWithJavaTest2() throws IOException {
        // INSERT RETURN INTO BLOCK
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/pre/Test2.java"),
                Path.of("src/test/data/for_tests/post/Test2.java")
        );
        Definition definition = new Definition(new InsertPattern(new ReturnPattern(), new BlockPattern()), null);
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.size());
    }

    @Test
    void testWithJavaTest2Inverse() throws IOException {
        // DELETE RETURN r | PARENT(r) IS BLOCK
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/post/Test2.java"),
                Path.of("src/test/data/for_tests/pre/Test2.java")
        );
        Definition definition = new Definition(
                new DeletePattern(
                        new VariablePattern("r", new ReturnPattern())
                ),
                new NodeTypeCondition(new ParentEval(new Variable("r")), new BlockPattern())
        );
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.size());
    }

    @Test
    void testWithCppTest3Inverse() throws IOException {
        // MOVE ANY FROM BLOCK b TO b
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/pre/Test3.cpp"),
                Path.of("src/test/data/for_tests/post/Test3.cpp")
        );
        Definition definition = new Definition(
                new MovePattern(new AnyPattern(), new VariablePattern("b", new BlockPattern()), new VariablePattern("b")),
                null
        );
        Set<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.size());
    }
}