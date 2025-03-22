package bachelors.project;

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
        Path definitionsPath = Path.of("experimental_data/experiment2/definitions_for_exp.txt");
        List<Definition> definitions = ParserClient.parseDefinitions(definitionsPath);
        Path commitHashesPath = Path.of("experimental_data/experiment2/commit_hashes_for_exp.txt");
        List<String> lines = Files.readAllLines(commitHashesPath);
        int numOfChanges = 0;
        int numOfCommitsWorkedOn = 0;
        for (String line : lines) {
            try {
                line = line.trim();
                String[] parts = line.split(" ");
                String cveId = parts[0].substring(0, parts[0].length() - 1);
                System.out.println("Working on " + cveId);
                for (int i = 1; i < parts.length; i++) {
                    String commitSha = parts[i];
                    downloadPreAndPostPatchRevisions(REPO_OWNER, REPO_NAME, commitSha, "experimental_data/experiment2/patch_data/" + cveId);
                    if (Files.exists(Path.of("experimental_data/experiment2/patch_data/%s/%s/pre_patch".formatted(cveId, commitSha))) &&
                    Files.exists(Path.of("experimental_data/experiment2/patch_data/%s/%s/post_patch".formatted(cveId, commitSha)))) {
                        DiffData diffData = GumTreeClient.getDiffData(Path.of("experimental_data/experiment2/patch_data/%s/%s/pre_patch".formatted(cveId, commitSha)),
                                Path.of("experimental_data/experiment2/patch_data/%s/%s/post_patch".formatted(cveId, commitSha)));
                        Set<Action> changes = ChangeFinder.findChanges(diffData, definitions);
                        System.out.println("Number of changes for commit " + commitSha + ": " + changes.size());
                        numOfChanges += changes.size();
                        numOfCommitsWorkedOn++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total number of non-essential changes detected: " + numOfChanges);
        System.out.println("Total number of commits worked on: " + numOfCommitsWorkedOn);
    }

    private static void downloadPreAndPostPatchRevisions(String repoOwner, String repoName, String commitSha, String parentDir) throws IOException {
        System.out.println("Working on commit " + commitSha);
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

                    downloadFile(rawUrlPrePatch, parentDir + '/' + commitSha + "/pre_patch/" + filename);
                    downloadFile(urlPostPatch, parentDir + '/' + commitSha + "/post_patch/" + filename);

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