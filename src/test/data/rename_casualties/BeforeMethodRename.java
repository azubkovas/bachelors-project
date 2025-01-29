public class MyClass {
    private int x, y;

    public MyClass() {
        this.x = 0;
        this.y = 0;
    }

    public int getSum() {
        return getX() + getY();
    }

    private int getX() {
        return this.x;
    }

    private int getY() {
        return this.y;
    }
}