package jayrender.scene.lighting;

import jayrender.geometry.Sphere;
import jayrender.util.Point3D;
import jayrender.util.base.Ray;
import jayrender.util.render.Color;

/**
 * Basically a point light but because it's not necessarily a point, I felt a bulb is a more appropriate representation
 *
 * Sphere that emits light
 */
public class BulbLight extends Emitter {

    private Sphere bulb;    // Used for ray intersection checking

    public BulbLight(double x, double y, double z, double size, double strength) {

        super(x, y, z, strength);

        bulb = new Sphere(x, y, z, size, new Color(255, 255, 255));
    }
    public BulbLight(Point3D pos, double size, double strength) {

        super(pos, strength);

        bulb = new Sphere(pos, size, new Color(255, 255, 255));
    }

    // Shouldn't really be used, but FINE
    public Color getColor() {

        return bulb.texture.color;
    }

    public Point3D intersect(Ray ray) {

        return bulb.intersect(ray);
    }
}
