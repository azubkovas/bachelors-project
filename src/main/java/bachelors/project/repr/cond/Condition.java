package bachelors.project.repr.cond;

import java.util.Map;

public abstract class Condition {
    public abstract <T> T evaluate(Map<String, String> variables);
}
