package jayrender.util.base;

import jayrender.util.Tuple2D;

public abstract class XY {

    public double x, y;
    public boolean placeholder;

    protected XY() {

        x = 0;
        y = 0;
    }
    protected XY(double x, double y) {

        this.x = x;
        this.y = y;
    }
    protected XY(XY copy) {

        x = copy.x;
        y = copy.y;
    }

    public abstract XY add(XY xy);
    public abstract XY sub(XY xy);
    public abstract XY mult(double scalar);
    public abstract XY div(double scalar);
    public void addIP(double number) {

        x += number;
        y += number;
    }
    public void subIP(double number) {

        x -= number;
        y -= number;
    }
    public void multIP(double scalar) {

        x *= scalar;
        y *= scalar;
    }
    public void divIP(double scalar) {

        x /= scalar;
        y /= scalar;
    }
    public Tuple2D scale() {

        double dividend = Math.max(x, y);
        return new Tuple2D(x/dividend, y/dividend);
    }
    public double dot(XYZ xyz) {
        return x*xyz.x + y*xyz.y;
    }
    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }
    public void normalize() {

        double mag = magnitude();
        x /= mag;
        y /= mag;
    }
}
