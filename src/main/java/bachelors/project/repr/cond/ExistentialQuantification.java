package bachelors.project.repr.cond;

import bachelors.project.repr.changetype.ChangeType;

import java.util.Map;

public class ExistentialQuantification extends Condition {
    private final ChangeType pattern;

    public ExistentialQuantification(ChangeType pattern) {
        this.pattern = pattern;
    }

    @Override
    public <T> T evaluate(Map<String, String> variables) {
        return null;
    }
}
