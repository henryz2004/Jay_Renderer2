package jayrender.util;

import jayrender.util.base.XYZ;

public class Point3D extends XYZ {

    public Point3D() {

    }   // Calls super() x = y = z = 0
    public Point3D(double x, double y, double z) {
        super(x, y, z);
    }
    public Point3D(Point3D copy) {
        super(copy);
    }

    public Point3D add(XYZ xyz) {
        return new Point3D(x+xyz.x, y+xyz.y, z+xyz.z);
    }
    public Point3D sub(XYZ xyz) {
        return new Point3D(x-xyz.x, y-xyz.y, z-xyz.z);
    }
    public Point3D mult(double scalar) {
        return new Point3D(x*scalar, y*scalar, z*scalar);
    }
    public Point3D mult(XYZ xyz) {

        return new Point3D(x*xyz.x, y*xyz.y, z*xyz.z);
    }
    public Point3D div(double scalar) {
        return new Point3D(x/scalar, y/scalar, z/scalar);
    }
    public Point3D div(XYZ xyz) {

        return new Point3D(x/xyz.x, y/xyz.y, z/xyz.z);
    }

    public String toString() {
        return "Point3D (" + x + ", " + y + ", " + z + ")";
    }
}
