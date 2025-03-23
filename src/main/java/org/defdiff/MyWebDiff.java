package org.defdiff;

import org.defdiff.util.DiffData;
import com.github.gumtreediff.actions.Diff;
import com.github.gumtreediff.client.Option;
import com.github.gumtreediff.client.Register;
import com.github.gumtreediff.client.diff.AbstractDiffClient;
import com.github.gumtreediff.client.diff.webdiff.*;
import com.github.gumtreediff.gen.Registry;
import com.github.gumtreediff.io.DirectoryComparator;
import com.github.gumtreediff.utils.Pair;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import spark.Spark;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.*;

@Register(description = "My web diff client", options = MyWebDiff.MyWebDiffOptions.class, priority = Registry.Priority.HIGH)
public class MyWebDiff extends AbstractDiffClient<MyWebDiff.MyWebDiffOptions> {
    public static final String JQUERY_JS_URL = "https://code.jquery.com/jquery-3.4.1.min.js";
    public static final String BOOTSTRAP_CSS_URL = "https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css";
    public static final String BOOTSTRAP_JS_URL = "https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js";
    public static final String POPPER_JS_URL = " https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js";
    private final DiffData diffData;

    public MyWebDiff(String[] args, DiffData diffData) {
        super(args);
        this.diffData = diffData;
    }

    public static class MyWebDiffOptions extends AbstractDiffClient.DiffOptions {
        public static final int DEFAULT_PORT = 4567;
        public int port = DEFAULT_PORT;

        @Override
        public Option[] values() {
            return Option.Context.addValue(super.values(),
                    new Option("--port", String.format("Set server port (default to %d).", DEFAULT_PORT), 1) {
                        @Override
                        protected void process(String name, String[] args) {
                            int p = Integer.parseInt(args[0]);
                            if (p > 0)
                                port = p;
                            else
                                System.err.printf("Invalid port number (%s), using %d.\n", args[0], port);
                        }
                    }
            );
        }
    }

    @Override
    protected MyWebDiffOptions newOptions() {
        return new MyWebDiffOptions();
    }

    @Override
    public void run() {
        configureSpark();
        Spark.awaitInitialization();
        System.out.println(String.format("Starting server: %s:%d.", "http://127.0.0.1", opts.port));
    }

    public void configureSpark() {
        port(opts.port);
        DirectoryComparator comparator = diffData.getComparator();
        staticFiles.location("/web/");
        get("/", (request, response) -> {
            if (comparator.isDirMode())
                response.redirect("/list");
            else
                response.redirect("/monaco-diff/0");
            return "";
        });
        get("/list", (request, response) -> {
            Renderable view = new DirectoryDiffView(comparator);
            return render(view);
        });
        get("/vanilla-diff/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Pair<File, File> pair = comparator.getModifiedFiles().get(id);
            Diff diff = getDiff(pair.first.getAbsolutePath(), pair.second.getAbsolutePath());
            Renderable view = new VanillaDiffView(pair.first, pair.second, diff, false);
            return render(view);
        });
        get("/monaco-diff/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Pair<File, File> pair = comparator.getModifiedFiles().get(id);
            Renderable view = new MonacoDiffView(pair.first, pair.second, diffData.getFileDiff(pair.first.toPath()), id);
            return render(view);
        });
        get("/monaco-native-diff/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Pair<File, File> pair = comparator.getModifiedFiles().get(id);
            Renderable view = new MonacoNativeDiffView(pair.first, pair.second, id);
            return render(view);
        });
        get("/mergely-diff/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Renderable view = new MergelyDiffView(id);
            return render(view);
        });
        get("/raw-diff/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Pair<File, File> pair = comparator.getModifiedFiles().get(id);
            Diff diff = getDiff(pair.first.getAbsolutePath(), pair.second.getAbsolutePath());
            Renderable view = new TextDiffView(pair.first, pair.second, diff);
            return render(view);
        });
        get("/left/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Pair<File, File> pair = comparator.getModifiedFiles().get(id);
            return readFile(pair.first.getAbsolutePath(), Charset.defaultCharset());
        });
        get("/right/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Pair<File, File> pair = comparator.getModifiedFiles().get(id);
            return readFile(pair.second.getAbsolutePath(), Charset.defaultCharset());
        });
        get("/quit", (request, response) -> {
            System.exit(0);
            return "";
        });
    }

    private static String render(Renderable r) throws IOException {
        HtmlCanvas c = new HtmlCanvas();
        r.renderOn(c);
        return c.toHtml();
    }

    private static String readFile(String path, Charset encoding)  throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
