package bachelors.project;

import bachelors.project.repr.Definition;
import bachelors.project.repr.ParserClient;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        Path prePatchRevisionPath = Path.of("patch_data/40e5880dfc51517334acda5f12beacdec52ca283/pre");
        Path postPatchRevisionPath = Path.of("patch_data/40e5880dfc51517334acda5f12beacdec52ca283/post");
        Path nonEssentialChangeDefinitionsFilePath = Path.of("src/test/data/definitions.txt");

        List<Definition> definitions = ParserClient.parseDefinitions(nonEssentialChangeDefinitionsFilePath);

        DiffData diffData = GumTreeClient.getDiffData(prePatchRevisionPath, postPatchRevisionPath);
        Set<Action> nonEssentialChanges = ChangeFinder.findChanges(diffData, definitions);
        diffData.removeNonEssentialChanges(nonEssentialChanges);
        new MyWebDiff(new String[]{prePatchRevisionPath.toString(), postPatchRevisionPath.toString()}, diffData).run();
    }
}