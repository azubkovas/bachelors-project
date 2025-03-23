package org.defdiff;

import org.defdiff.deflang.Definition;
import org.defdiff.deflang.ParserClient;
import org.defdiff.util.DiffData;
import org.defdiff.util.GumTreeClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Path prePatchRevisionPath = Path.of("src/test/data/rename_casualties/java/BeforeVariableRename.java");
        Path postPatchRevisionPath = Path.of("src/test/data/rename_casualties/java/AfterVariableRename.java");
        Path nonEssentialChangeDefinitionsFilePath = Path.of("src/test/data/definitions.txt");

        List<Definition> definitions = ParserClient.parseDefinitions(nonEssentialChangeDefinitionsFilePath);

        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
        ChangesContainer nonEssentialChanges = ChangeFinder.findChanges(diffData, definitions);
        System.out.println("Total number of non-essential changes: " + nonEssentialChanges.getAllChanges().size());
        nonEssentialChanges.printChangeCounts();
        System.out.println();
        nonEssentialChanges.printChanges();
//        diffData.removeNonEssentialChanges(nonEssentialChanges.getAllChanges());
//        new MyWebDiff(new String[]{prePatchRevisionPath.toString(), postPatchRevisionPath.toString()}, diffData).run();
    }
}