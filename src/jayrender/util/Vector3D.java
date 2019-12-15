package jayrender.util;

import jayrender.util.base.XYZ;

import java.util.concurrent.ThreadLocalRandom;

public class Vector3D extends XYZ {

    public Vector3D() {

    }   // Calls super() x = y = z = 0
    public Vector3D(double x, double y, double z) {
        super(x, y, z);
    }
    public Vector3D(Point3D start, Point3D to) {

        x = to.x - start.x;
        y = to.y - start.y;
        z = to.z - start.z;
    }
    public Vector3D(Vector3D copy) {
        super(copy);
    }

    // Generate random unit vector
    public static Vector3D randomUnit() {

        Vector3D vector = new Vector3D(
                ThreadLocalRandom.current().nextDouble(-1, 1),
                ThreadLocalRandom.current().nextDouble(-1, 1),
                ThreadLocalRandom.current().nextDouble(-1, 1)
        );
        vector.normalize();

        return vector;
    }

    // Adds noise to this vector
    public Vector3D noiseNormalize() {

        Vector3D noisy = new Vector3D(
                ThreadLocalRandom.current().nextGaussian() + x,
                ThreadLocalRandom.current().nextGaussian() + y,
                ThreadLocalRandom.current().nextGaussian() + z
        );
        noisy.normalize();

        return noisy;
    }

    public Vector3D add(XYZ xyz) {
        return new Vector3D(x+xyz.x, y+xyz.y, z+xyz.z);
    }
    public Vector3D sub(XYZ xyz) {
        return new Vector3D(x-xyz.x, y-xyz.y, z-xyz.z);
    }
    public Vector3D mult(double scalar) {
        return new Vector3D(x*scalar, y*scalar, z*scalar);
    }
    public Vector3D mult(XYZ xyz) {

        return new Vector3D(x*xyz.x, y*xyz.y, z*xyz.z);
    }
    public Vector3D div(double scalar) {
        return new Vector3D(x/scalar, y/scalar, z/scalar);
    }
    public Vector3D div(XYZ xyz) {

        return new Vector3D(x/xyz.x, y/xyz.y, z/xyz.z);
    }

    public String toString() {
        return "Vector3D (" + x + ", " + y + ", " + z + ")";
    }
}
