package jayrender.util.base;

import jayrender.util.Point3D;
import jayrender.util.Vector3D;

public class Ray {

    public Point3D origin;      // The endpoint of the ray
    public Vector3D direction;

    public Ray(Point3D origin, Vector3D direction) {

        this.origin    = new Point3D(origin);
        this.direction = new Vector3D(direction);
        this.direction.normalize();                 // Not necessary but direction might not be a unit vector
    }
    public Ray(Point3D origin, Point3D through) {

        this.origin = new Point3D(origin);
        direction   = new Vector3D(origin, through);
        direction.normalize();
    }

    public String toString() {

        return "Ray with origin: " + origin + " and vector: " + direction;
    }
}
