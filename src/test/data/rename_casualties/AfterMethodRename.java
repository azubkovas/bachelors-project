public class MyClass {
    private int x, y;

    public MyClass() {
        this.x = 0;
        this.y = 0;
    }

    public int getSum() {
        return readX() + readY();
    }

    private int readX() {
        return this.x;
    }

    private int readY() {
        return this.y;
    }
}