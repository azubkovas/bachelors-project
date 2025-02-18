package bachelors.project.repr.changetype;

public class Update extends ChangeType {
    private String old, new_;

    public Update(Target target, String old, String new_) {
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
