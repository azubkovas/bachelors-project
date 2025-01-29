class MyClass {
private:
    int x;
    int y;

    int getX() const {
        return x;
    }

    int getY() const {
        return y;
    }

public:
    MyClass() : x(0), y(0) {}

    int getSum() const {
        return getX() + getY();
    }
};
