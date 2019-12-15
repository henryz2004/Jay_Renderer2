package jayrender.util;

import jayrender.util.base.XYZ;

public class Normal extends XYZ {

    public Normal() {

    }
    public Normal(double x, double y, double z) {
        super(x, y, z);
    }
    public Normal(Point3D copy) {
        super(copy);
    }
    public Normal(Vector3D vector) {

        x = vector.x;
        y = vector.y;
        z = vector.z;
    }

    public Normal add(XYZ xyz) { return new Normal(x+xyz.x, y+xyz.y, z+xyz.z); }
    public Normal sub(XYZ xyz) {
        return new Normal(x-xyz.x, y-xyz.y, z-xyz.z);
    }
    public Normal mult(double scalar) {
        return new Normal(x*scalar, y*scalar, z*scalar);
    };
    public Normal mult(XYZ xyz) {

        return new Normal(x*xyz.x, y*xyz.y, z*xyz.z);
    }
    public Normal div(double scalar) {
        return new Normal(x/scalar, y/scalar, z/scalar);
    }
    public Normal div(XYZ xyz) {

        return new Normal(x/xyz.x, y/xyz.y, z/xyz.z);
    }

    public String toString() {
        return "Normal (" + x + ", " + y + ", " + z + ")";
    }
}