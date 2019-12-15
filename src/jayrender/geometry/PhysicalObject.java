package jayrender.geometry;

import jayrender.util.Normal;
import jayrender.util.Point3D;
import jayrender.util.base.Ray;
import jayrender.util.render.Color;
import jayrender.util.render.Texture;

import java.util.concurrent.ThreadLocalRandom;

public abstract class PhysicalObject {

    public Point3D pos;
    public Texture texture;

    protected static final double EPSILON = 1E-5;     // When checking for double ==, use Math.abs(a - b) < EPSILON

    protected PhysicalObject(double x, double y, double z, Texture texture) {

        this.pos     = new Point3D(x, y, z);
        this.texture = texture;
    }
    protected PhysicalObject(Point3D pos, Texture texture) {

        this.pos     =  new Point3D(pos);
        this.texture = texture;
    }

    // TODO: What should this return? Vector3D? double (distance)?
    // for now just a point3D of intersection
    public abstract Point3D intersect(Ray ray);
    public abstract Normal calculateNormal(Point3D intersection);
}
