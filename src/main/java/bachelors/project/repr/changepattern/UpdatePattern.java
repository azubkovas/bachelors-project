package bachelors.project.repr.changepattern;

public class UpdatePattern extends ChangePattern {
    private String old, new_;

    public UpdatePattern(Target target, String old, String new_) {
        super(target);
        this.old = old;
        this.new_ = new_;
    }

    public String getOld() {
        return old;
    }

    public String getNew_() {
        return new_;
    }
}
