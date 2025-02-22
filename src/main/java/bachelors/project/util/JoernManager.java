package bachelors.project.util;

import bachelors.project.repr.nodepattern.NodeType;
import com.github.gumtreediff.tree.Tree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JoernManager {
    private static JoernManager instance;
    private Process joernProcess;
    private BufferedWriter joernWriter;
    private BufferedReader joernReader;

    private JoernManager() throws IOException {
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

    public static synchronized JoernManager getInstance() throws IOException {
        if (instance == null) {
            instance = new JoernManager();
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

    // Returns query string that can be used to obtain the equivalent node in Joern todo: deal with possible multiple outputs, add support for multiple files
    public static String getLocalQuery(Tree node, String relativeFilePath, String name) {
        String condition = buildConditionBasedOnParents(node);
        return "cpg.local.name(\"%s\").where(%s)".formatted(name, condition);
    }

    public static boolean checkNodeOfRequiredType(Tree node, NodeType requiredType) {
        return switch (requiredType) {
            case LOCAL -> checkNodeLocal(node);
            case IDENTIFIER -> checkNodeIdentifier(node);
            case LITERAL -> checkNodeLiteral(node);
            case CALL -> checkNodeCall(node);
            case METHOD -> checkNodeMethod(node);
            default -> throw new RuntimeException("Unsupported node type: %s".formatted(requiredType));
        };
    }

    private static boolean checkNodeMethod(Tree node) {
        try {
            String name = node.getLabel();
            String methodQuery = getMethodQuery(node, "", name);
            String result = getInstance().executeQuery("""
                    %s.hasNext""".formatted(methodQuery));
            return result.equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private static boolean checkNodeCall(Tree node) {
        try {
            String name = node.getLabel();
            String callQuery = getCallQuery(node, "", name);
            String result = getInstance().executeQuery("""
                    %s.hasNext""".formatted(callQuery));
            return result.equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private static boolean checkNodeLiteral(Tree node) {
        try {
            String identifierQuery = getLiteralQuery(node, "", node.getLabel());
            String result = getInstance().executeQuery("""
                    %s.hasNext""".formatted(identifierQuery));
            return result.equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getLiteralQuery(Tree node, String relativeFilePath, String value) {
        String condition = buildConditionBasedOnParents(node);
        return "cpg.literal.where(%s)".formatted(condition);

    }

    public static String getNodeQueryOfRequiredType(Tree node, NodeType requiredType) {
        return switch (requiredType) {
            case LOCAL -> getLocalQuery(node, "", node.getLabel());
            case IDENTIFIER -> getIdentifierQuery(node, "", node.getLabel());
            case LITERAL -> getLiteralQuery(node, "", node.getLabel());
            case CALL -> getCallQuery(node, "", node.getLabel());
            case METHOD -> getMethodQuery(node, "", node.getLabel());
            default -> throw new RuntimeException("Unsupported node type: %s".formatted(requiredType));
        };
    }

    private static boolean checkNodeIdentifier(Tree node) {
        try {
            String identifierQuery = getIdentifierQuery(node, "", node.getLabel());
            String result = getInstance().executeQuery("""
                    %s.hasNext""".formatted(identifierQuery));
            return result.equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkNodeLocal(Tree node) {
        try {
            String localQuery = getLocalQuery(node, "", node.getLabel());
            String result = getInstance().executeQuery("""
                    %s.hasNext""".formatted(localQuery));
            return result.equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getIdentifierQuery(Tree node, String relativeFilePath, String name) {
        String condition = buildConditionBasedOnParents(node);
        return "cpg.identifier.name(\"%s\").where(%s)".formatted(name, condition);
    }

    public static String getMethodQuery(Tree node, String relativeFilePath, String name) {
        String condition = buildConditionBasedOnParents(node);
        return "cpg.method.name(\"%s\").where(%s)".formatted(name, condition);
    }

    public static String getCallQuery(Tree node, String relativeFilePath, String name) {
        String condition = buildConditionBasedOnParents(node);
        return "cpg.call.name(\"%s\").where(%s)".formatted(name, condition);
    }

    private static String buildConditionBasedOnParents(Tree node) {
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
                case "class":
                    assert steps.get(steps.size() - 1).equals(".astParent.isBlock");
                    steps.remove(steps.size() - 1);
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
                    if (operator != null && Set.of("+", "-", "*", "/", "%", "//").contains(operator.getLabel())) {
                        int pos = parent.getChildPosition((i > 0) ? parents.get(i - 1) : node);
                        assert pos != -1;
                        steps.add(".order(%d).astParent.isCall".formatted((pos == 0) ? 1 : 2));
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
}
