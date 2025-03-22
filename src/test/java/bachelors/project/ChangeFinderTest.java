package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.ParserClient;
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
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeFinderTest {

    @Test
    void testFindChangesWithDummyExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(Path.of("src/test/data/pre_patch/SampleProject"), Path.of("src/test/data/post_patch/SampleProject"));
        Definition definition = ParserClient.parseDefinition("UPDATE LITERAL x -> y | x == \"Hello, World!\" AND y == \"Hello, Friends!\"");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithJavaVariableRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/java/BeforeVariableRename.java"),
                Path.of("src/test/data/rename_casualties/java/AfterVariableRename.java"));
        Definition definition = ParserClient.parseDefinition("UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i REFERS TO l)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCppVariableRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c++/BeforeVariableRename.cpp"),
                Path.of("src/test/data/rename_casualties/c++/AfterVariableRename.cpp"));
        Definition definition = ParserClient.parseDefinition("UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i REFERS TO l)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCVariableRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c/BeforeVariableRename.c"),
                Path.of("src/test/data/rename_casualties/c/AfterVariableRename.c"));
        Definition definition = ParserClient.parseDefinition("UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i REFERS TO l)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(4, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCsVariableRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c#/BeforeVariableRename.cs"),
                Path.of("src/test/data/rename_casualties/c#/AfterVariableRename.cs"));
        Definition definition = ParserClient.parseDefinition("UPDATE IDENTIFIER i x -> y | ∃(UPDATE LOCAL l x -> y | i REFERS TO l)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithJavaMethodRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/java/BeforeMethodRename.java"),
                Path.of("src/test/data/rename_casualties/java/AfterMethodRename.java")
        );
        Definition definition = ParserClient.parseDefinition("UPDATE CALL c x -> y | ∃(UPDATE METHOD m x -> y | c REFERS TO m)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCppMethodRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c++/BeforeMethodRename.cpp"),
                Path.of("src/test/data/rename_casualties/c++/AfterMethodRename.cpp")
        );
        Definition definition = ParserClient.parseDefinition("UPDATE CALL c x -> y | ∃(UPDATE METHOD m x -> y | c REFERS TO m)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCMethodRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c/BeforeFunctionRename.c"),
                Path.of("src/test/data/rename_casualties/c/AfterFunctionRename.c")
        );
        Definition definition = ParserClient.parseDefinition("UPDATE CALL c x -> y | ∃(UPDATE METHOD m x -> y | c REFERS TO m)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.getAllChanges().size());
    }

    @Test
    void testWithJavaTest2() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/pre/Test2.java"),
                Path.of("src/test/data/for_tests/post/Test2.java")
        );
        Definition definition = ParserClient.parseDefinition("INSERT RETURN INTO BLOCK");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.getAllChanges().size());
    }

    @Test
    void testWithJavaTest2Inverse() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/post/Test2.java"),
                Path.of("src/test/data/for_tests/pre/Test2.java")
        );
        Definition definition = ParserClient.parseDefinition("DELETE RETURN r | PARENT(r) IS BLOCK");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.getAllChanges().size());
    }

    @Test
    void testWithCppTest3Inverse() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/pre/Test3.cpp"),
                Path.of("src/test/data/for_tests/post/Test3.cpp")
        );
        Definition definition = ParserClient.parseDefinition("MOVE ANY FROM BLOCK b TO b");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.getAllChanges().size());
    }
}