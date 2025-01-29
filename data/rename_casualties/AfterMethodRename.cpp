class MyClass {
private:
    int x;
    int y;

    int readX() const {
        return x;
    }

    int readY() const {
        return y;
    }

public:
    MyClass() : x(0), y(0) {}

    int getSum() const {
        return readX() + readY();
    }
};
