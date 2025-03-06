package bachelors.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

import bachelors.project.repr.Definition;
import bachelors.project.repr.ParserClient;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gumtreediff.actions.model.Action;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangeCounterExperiment {

    private static final String GITHUB_TOKEN = System.getenv("GITHUB_TOKEN");
    private static final String REPO_OWNER = "apache";
    private static final String REPO_NAME = "tomcat";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        // clear the tmp directory
        Path tmpDir = Path.of("tmp");
        if (Files.exists(tmpDir)) {
            Files.walk(tmpDir)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        downloadPreAndPostPatchRevisions(REPO_OWNER, REPO_NAME, "3631adb1342d8bbd8598802a12b63ad02c37d591");
        DiffData diffData = GumTreeClient.getDiffData(Path.of("tmp/pre_patch/"), Path.of("tmp/post_patch/"));
        List<Definition> definitions = ParserClient.parseDefinitions(Path.of("src/test/data/definitions_for_exp.txt"));
        Set<Action> changes = ChangeFinder.findChanges(diffData, definitions);
        System.out.println("Number of changes: " + changes.size());
    }

    private static void downloadPreAndPostPatchRevisions(String repoOwner, String repoName, String commitSha) throws IOException {
        String commitUrl = String.format("https://api.github.com/repos/%s/%s/commits/%s", repoOwner, repoName, commitSha);
        Request request = new Request.Builder()
                .url(commitUrl)
                .header("Authorization", "token " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JsonNode commitData = objectMapper.readTree(response.body().string());
            String parentCommitSha = commitData.get("parents").get(0).get("sha").asText(); // parent commit sha

            for (JsonNode file : commitData.get("files")) {
                if (file.get("status").asText().equals("modified")) {
                    String filename = file.get("filename").asText();
                    String urlPostPatch = file.get("raw_url").asText();
                    String rawUrlPrePatch = String.format("https://raw.githubusercontent.com/%s/%s/%s/%s", repoOwner, repoName, parentCommitSha, filename);

                    downloadFile(rawUrlPrePatch, "tmp/pre_patch/" + filename);
                    downloadFile(urlPostPatch, "tmp/post_patch/" + filename);

                    System.out.println("Downloaded pre-patch and post-patch versions of " + filename);
                }
            }
        }

    }

    private static void downloadFile(String url, String outputPath) throws IOException {
        Path outputPathObj = Path.of(outputPath);
        Files.createDirectories(outputPathObj.getParent()); // Create directories if they do not exist

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "token " + GITHUB_TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            try (InputStream inputStream = response.body().byteStream()) {
                Files.copy(inputStream, outputPathObj, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}