package jayrender.util.base;

public abstract class XYZ {

    public double x, y, z;      // X, Y, Z coordinates

    protected XYZ() {

        x = 0;
        y = 0;
        z = 0;
    }
    protected XYZ(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }
    protected XYZ(XYZ copy) {

        x = copy.x;
        y = copy.y;
        z = copy.z;
    }

    public abstract XYZ add(XYZ xyz);
    public abstract XYZ sub(XYZ xyz);
    public abstract XYZ mult(double scalar);
    public abstract XYZ mult(XYZ xyz);
    public abstract XYZ div(double scalar);
    public abstract XYZ div(XYZ xyz);
    public void addIP(double number) {

        x += number;
        y += number;
        z += number;
    }
    public void subIP(double number) {

        x -= number;
        y -= number;
        z -= number;
    }
    public void multIP(double scalar) {

        x *= scalar;
        y *= scalar;
        z *= scalar;
    }
    public void divIP(double scalar) {

        x /= scalar;
        y /= scalar;
        z /= scalar;
    }
    public double dot(XYZ xyz) {
        return x*xyz.x + y*xyz.y + z*xyz.z;
    }
    public double magnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }
    public void normalize() {

        double mag = magnitude();
        x /= mag;
        y /= mag;
        z /= mag;
    }
}
