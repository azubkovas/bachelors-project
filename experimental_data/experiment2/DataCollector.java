package bachelors.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataCollector {
    private static final String GITHUB_API_URL = "https://api.github.com/search/commits";
    private static final String GITHUB_TOKEN = System.getenv("GITHUB_TOKEN");
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int MAX_RETRIES = 5;

    public static void main(String[] args) {
        String groundTruthFile = "/Users/arsenijuszubkovas/Documents/UoE/Year_4/Final Project/CascadeEvaluationData/SecurityDataset/ground_truth.csv";
        String line;
        Set<String> cveIds = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(groundTruthFile))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                if (values.length == 0)
                    continue;
                String cveId = values[0];
                cveId = cveId.split("_")[0];
                cveIds.add(cveId);
            }
            System.out.println(cveIds.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Set<String>> commitHashes = new HashMap<>();
        for (String cveId : cveIds) {
            searchCommitsByCveId(cveId, commitHashes);
        }

        // Process commitHashes as needed
        System.out.println("COMMIT HASHES FOR EXP:");
        for (Map.Entry<String, Set<String>> entry : commitHashes.entrySet()) {
            String cveId = entry.getKey();
            Set<String> hashes = entry.getValue();
            System.out.print(cveId + ":");
            hashes.forEach(x -> System.out.print(" " + x));
            System.out.println();
        }
    }

    private static void searchCommitsByCveId(String cveId, Map<String, Set<String>> commitHashes) {
        int retries = 0;
        boolean success = false;

        while (retries < MAX_RETRIES && !success) {
            try {
                String urlString = GITHUB_API_URL + "?q=repo:apache/tomcat+" + cveId;
                Request request = new Request.Builder()
                        .url(urlString)
                        .header("Authorization", "Bearer " + GITHUB_TOKEN)
                        .header("Accept", "application/vnd.github+json")
                        .header("X-GitHub-Api-Version", "2022-11-28")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        JsonNode responseJson = objectMapper.readTree(response.body().string());
                        JsonNode items = responseJson.get("items");
                        if (items != null) {
                            for (JsonNode item : items) {
                                String commitSha = item.get("sha").asText();
                                if (!commitHashes.containsKey(cveId)) {
                                    commitHashes.put(cveId, new HashSet<>());
                                }
                                commitHashes.get(cveId).add(commitSha);
                            }
                        }
                        success = true;
                        System.out.println("SUCCESSFUL");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!success) {
                retries++;
                try {
                    Thread.sleep((long) Math.pow(2, retries) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}