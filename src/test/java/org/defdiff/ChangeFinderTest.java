package org.defdiff;

import org.defdiff.deflang.Definitions;
import org.defdiff.deflang.Definitions;
import org.defdiff.deflang.ParserClient;
import org.defdiff.util.DiffData;
import org.defdiff.util.GumTreeClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeFinderTest {

    @Test
    void testFindChangesWithDummyExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(Path.of("src/test/data/pre_patch/SampleProject"), Path.of("src/test/data/post_patch/SampleProject"));
        Definitions simpleDefinition = ParserClient.parseDefinitions("UPDATE LITERAL x -> y | x == \"Hello, World!\" AND y == \"Hello, Friends!\"");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(1, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithJavaVariableRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/java/BeforeVariableRename.java"),
                Path.of("src/test/data/rename_casualties/java/AfterVariableRename.java"));
        Definitions simpleDefinition = ParserClient.parseDefinitions("UPDATE IDENTIFIER i x -> y | EXISTS(UPDATE LOCAL l x -> y | i REFERS TO l)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCppVariableRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c++/BeforeVariableRename.cpp"),
                Path.of("src/test/data/rename_casualties/c++/AfterVariableRename.cpp"));
        Definitions simpleDefinition = ParserClient.parseDefinitions("UPDATE IDENTIFIER i x -> y | EXISTS(UPDATE LOCAL l x -> y | i REFERS TO l)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCVariableRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c/BeforeVariableRename.c"),
                Path.of("src/test/data/rename_casualties/c/AfterVariableRename.c"));
        Definitions simpleDefinition = ParserClient.parseDefinitions("UPDATE IDENTIFIER i x -> y | EXISTS(UPDATE LOCAL l x -> y | i REFERS TO l)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(4, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCsVariableRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c#/BeforeVariableRename.cs"),
                Path.of("src/test/data/rename_casualties/c#/AfterVariableRename.cs"));
        Definitions simpleDefinition = ParserClient.parseDefinitions("UPDATE IDENTIFIER i x -> y | EXISTS(UPDATE LOCAL l x -> y | i REFERS TO l)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithJavaMethodRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/java/BeforeMethodRename.java"),
                Path.of("src/test/data/rename_casualties/java/AfterMethodRename.java")
        );
        Definitions simpleDefinition = ParserClient.parseDefinitions("UPDATE CALL c x -> y | EXISTS(UPDATE METHOD m x -> y | c REFERS TO m)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCppMethodRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c++/BeforeMethodRename.cpp"),
                Path.of("src/test/data/rename_casualties/c++/AfterMethodRename.cpp")
        );
        Definitions simpleDefinition = ParserClient.parseDefinitions("UPDATE CALL c x -> y | EXISTS(UPDATE METHOD m x -> y | c REFERS TO m)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(2, changes.getAllChanges().size());
    }

    @Test
    void testFindChangesWithCMethodRenameExample() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/rename_casualties/c/BeforeFunctionRename.c"),
                Path.of("src/test/data/rename_casualties/c/AfterFunctionRename.c")
        );
        Definitions simpleDefinition = ParserClient.parseDefinitions("UPDATE CALL c x -> y | EXISTS(UPDATE METHOD m x -> y | c REFERS TO m)");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(1, changes.getAllChanges().size());
    }

    @Test
    void testWithJavaTest2() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/pre/Test2.java"),
                Path.of("src/test/data/for_tests/post/Test2.java")
        );
        Definitions simpleDefinition = ParserClient.parseDefinitions("INSERT RETURN INTO BLOCK");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(1, changes.getAllChanges().size());
    }

    @Test
    void testWithJavaTest2Inverse() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/post/Test2.java"),
                Path.of("src/test/data/for_tests/pre/Test2.java")
        );
        Definitions simpleDefinition = ParserClient.parseDefinitions("DELETE RETURN r | PARENT(r) IS BLOCK");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(1, changes.getAllChanges().size());
    }

    @Test
    void testWithCppTest3Inverse() throws IOException {
        DiffData diffData = GumTreeClient.getDiffData(
                Path.of("src/test/data/for_tests/pre/Test3.cpp"),
                Path.of("src/test/data/for_tests/post/Test3.cpp")
        );
        Definitions simpleDefinition = ParserClient.parseDefinitions("MOVE ANY FROM BLOCK b TO b");
        ChangesContainer changes = ChangeFinder.findChanges(diffData, simpleDefinition);
        assertEquals(1, changes.getAllChanges().size());
    }
}