package org.defdiff;

import org.defdiff.deflang.Definitions;
import org.defdiff.deflang.ParserClient;
import org.defdiff.util.DiffData;
import org.defdiff.util.GumTreeClient;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path prePatchRevisionPath = Path.of(args[0]);
        Path postPatchRevisionPath = Path.of(args[1]);
        Path nonEssentialChangeDefinitionsFilePath = Path.of(args[2]);

        Definitions definitions = ParserClient.parseDefinitions(nonEssentialChangeDefinitionsFilePath);

        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
        ChangesContainer nonEssentialChanges = ChangeFinder.findChanges(diffData, definitions);
        System.out.println("Total number of non-essential changes: " + nonEssentialChanges.getAllChanges().size());
        nonEssentialChanges.printChangeCounts();
        nonEssentialChanges.printChanges();
        diffData.removeNonEssentialChanges(nonEssentialChanges.getAllChanges());
        new MyWebDiff(new String[]{prePatchRevisionPath.toString(), postPatchRevisionPath.toString()}, diffData).run();
    }
}