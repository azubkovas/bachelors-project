package bachelors.project.util;

import bachelors.project.repr.nodepattern.NodeType;
import com.github.gumtreediff.tree.Tree;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JoernClient {
    private static JoernClient instance;
    private Process joernProcess;
    private BufferedWriter joernWriter;
    private BufferedReader joernReader;
    private Path currentPath;

    private JoernClient() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("joern");
        joernProcess = builder.start();
        joernWriter = new BufferedWriter(new OutputStreamWriter(joernProcess.getOutputStream()));
        joernReader = new BufferedReader(new InputStreamReader(joernProcess.getInputStream()));
        ignoreUntilPrompt();
    }

    private void ignoreUntilPrompt() throws IOException {
        Character[] buffer = new Character[6];
        do {
            char ch = (char) joernReader.read();
            for (int i = 0; i < 5; i++) {
                buffer[i] = buffer[i + 1];
            }
            buffer[5] = ch;
        } while (buffer[0] == null || buffer[0] != 'j' ||
                buffer[1] == null || buffer[1] != 'o' ||
                buffer[2] == null || buffer[2] != 'e' ||
                buffer[3] == null || buffer[3] != 'r' ||
                buffer[4] == null || buffer[4] != 'n' ||
                buffer[5] != '>'
        );
    }

    public static synchronized JoernClient getInstance() throws IOException {
        if (instance == null) {
            instance = new JoernClient();
        }
        return instance;
    }

    public synchronized String executeQuery(String query) throws IOException {
        joernWriter.write("print(%s)".formatted(query));
        joernWriter.newLine();
        joernWriter.flush();

        StringBuilder result = new StringBuilder();
        do {
            char ch = (char) joernReader.read();
            result.append(ch);
        } while (!result.toString().endsWith("joern>")
        );
        // remove the prompt
        result.delete(result.length() - 6, result.length());
        return result.toString().trim();
    }

    public void close() throws IOException {
        joernWriter.close();
        joernReader.close();
        joernProcess.destroy();
    }

    public static void setProjectToNodePath(Tree node) throws IOException {
        Path pathOfNode = getPathOfNode(node);
        if (!pathOfNode.equals(getInstance().getCurrentPath())) {
            getInstance().setCurrentPath(pathOfNode);
        }
    }

    private static Path getPathOfNode(Tree node) {
        Tree curr = node;
        while (curr.getMetadata("revisionPath") == null && curr.getParent() != null) {
            curr = curr.getParent();
        }
        Path path = (Path) curr.getMetadata("revisionPath");
        if (path == null) {
            throw new RuntimeException("Path not found for node");
        }
        return path;
    }

    public static String getNodeQueryOfRequiredType(Tree node, NodeType requiredType) {
        try {
            setProjectToNodePath(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getNodeQueryOfRequiredType(node, requiredType.getJoernName(), node.getLabel());
    }

    public static String getNodeQueryOfRequiredType(Tree node, String joernTypeName, String nameToFilterOn) {
        try {
            setProjectToNodePath(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "cpg.%s.name(\"%s\").where(%s)".formatted(joernTypeName, nameToFilterOn, getConditionBasedOnParents(node));
    }

    public static String getConditionBasedOnParents(Tree node) {
        List<String> steps = new ArrayList<>();
        List<Tree> parents = node.getParents();
        for (int i = 0; i < parents.size(); i++) {
            Tree parent = parents.get(i);
            String name;
            switch (parent.getType().name) {
                case "block":
                    steps.add(".astParent.isBlock");
                    break;
                case "function":
                    name = parent.getLabel();
                    steps.add(".astParent.isMethod.name(\"%s\")".formatted(name));
                    break;
                case "constructor":
                    steps.add(".astParent.isMethod.name(\"<init>\")");
                    break;
                case "class":
                    if (!steps.isEmpty()) {
                        assert steps.get(steps.size() - 1).equals(".astParent.isBlock");
                        steps.remove(steps.size() - 1);
                    }
                    name = GumTreeClient.getFirstChildOfType(parent, "name").getLabel();
                    steps.add(".astParent.isTypeDecl.name(\"%s\")".formatted(name));
                    break;
                case "call":
                    name = parent.getLabel();
                    steps.add(".astParent.isCall.name(\"%s\")".formatted(name));
                    break;
                case "if":
                    steps.add(".astParent.isControlStructure.controlStructureType(\"IF\")");
                    break;
                case "else":
                    steps.add(".astParent.isControlStructure.controlStructureType(\"ELSE\")");
                    break;
                case "while":
                    steps.add(".astParent.isControlStructure.controlStructureType(\"WHILE\")");
                    break;
                case "for":
                    steps.add(".astParent.isControlStructure.controlStructureType(\"FOR\")");
                    break;
                case "expr":
                    Tree operator = GumTreeClient.getFirstChildOfType(parent, "operator");
                    if (operator != null && Set.of("+", "-", "*", "/", "%", "//", "=").contains(operator.getLabel())) {
                        int pos = parent.getChildPosition((i > 0) ? parents.get(i - 1) : node);
                        assert pos != -1;
                        steps.add(".order(%d).astParent.isCall".formatted((pos == 0) ? 1 : 2));
                    } else if (operator != null && operator.getLabel().equals("!")) {
                        steps.add(".astParent.isCall.name(\"<operator>.logicalNot\")");
                    }
                    break;
                case "return":
                    steps.add(".astParent.isReturn");
                    break;
                case "init":
                    steps.add(".astParent.isCall");
                    break;
            }
        }
        if (steps.isEmpty()) {
            return "x => x";
        }
        return steps.stream().reduce("_", (a, b) -> a + b);
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(Path currentPath) throws IOException {
        getInstance().executeQuery("""
                if (openForInputPath("%s").isEmpty) {
                      importCode("%s")
                   }""".formatted(currentPath.toString(), currentPath.toString()));
        this.currentPath = currentPath;
    }
}
