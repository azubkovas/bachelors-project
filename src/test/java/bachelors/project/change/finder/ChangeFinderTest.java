package bachelors.project.change.finder;

import bachelors.project.ChangeFinder;
import bachelors.project.repr.Definition;
import bachelors.project.repr.changetype.Target;
import bachelors.project.repr.changetype.Update;
import bachelors.project.repr.cond.Binary;
import bachelors.project.repr.cond.ExistentialQuantification;
import bachelors.project.repr.cond.Operator;
import bachelors.project.repr.cond.Variable;
import bachelors.project.repr.nodetype.Identifier;
import bachelors.project.repr.nodetype.Literal;
import bachelors.project.repr.nodetype.Local;
import bachelors.project.util.DiffData;
import bachelors.project.util.GumTreeClient;
import com.github.gumtreediff.actions.model.Action;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeFinderTest {

    @Test
    void testFindChangesWithDummyExample() {
        // UPDATE LITERAL x -> y | x == "Hello, World!" && y == "Hello, Friends!"
        DiffData diffData = GumTreeClient.getDiffData("src/test/data/pre_patch/SampleProject", "src/test/data/post_patch/SampleProject");
        Definition definition = new Definition(
                new Update(
                        new Target(
                                new Literal(),
                                null
                        ),
                        "x",
                        "y"
                ),
                new Binary(
                        new Binary(
                                new Variable("x"),
                                new bachelors.project.repr.cond.Literal<String>("Hello, World!"),
                                Operator.EQ
                        ),
                        new Binary(
                                new Variable("y"),
                                new bachelors.project.repr.cond.Literal<String>("Hello, Friends!"),
                                Operator.EQ
                        ),
                        Operator.AND
                ));
        List<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(1, changes.size());
    }

    @Test
    void testFindChangesWithJavaVariableRenameExample() {
        // UPDATE IDENTIFIER x -> y | ∃(UPDATE LOCAL l x -> y) && x.ref == l;
        DiffData diffData = GumTreeClient.getDiffData(
                "src/test/data/rename_casualties/java/BeforeVariableRename.java",
                "src/test/data/rename_casualties/java/AfterVariableRename.java");
        Definition definition = new Definition(
                new Update(
                        new Target(
                                new Identifier(),
                                null
                        ),
                        "x",
                        "y"
                ),
                new Binary(
                        new ExistentialQuantification(
                                new Update(
                                        new Target(
                                                new Local(),
                                                "l"
                                        ),
                                        "x",
                                        "y"
                                )
                        ),
                        new Binary(
                                new Variable("x"),
                                new Variable("l"),
                                Operator.EQ
                        ),
                        Operator.AND
                )
        );
        List<Action> changes = ChangeFinder.findChanges(diffData, List.of(definition));
        assertEquals(2, changes.size());
    }
}