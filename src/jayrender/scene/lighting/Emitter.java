package jayrender.scene.lighting;

import jayrender.util.Point3D;
import jayrender.util.base.Ray;
import jayrender.util.render.Color;

/**
 * Light source
 *
 * Similar base class to PhysicalObject, but there are differences
 */
public abstract class Emitter {

    public Point3D pos;
    public double strength;     // Strength of light

    // TODO: COLOR

    protected static final double EPSILON = 1E-5;

    protected Emitter(double x, double y, double z, double strength) {

        this.pos      = new Point3D(x, y, z);
        this.strength = strength;
    }
    protected Emitter(Point3D pos, double strength) {

        this.pos      = new Point3D(pos);
        this.strength = strength;
    }

    /**
     * No clamping
     *
     * @param strength - strength of light
     * @param distance - distance from light
     * @return         - strength/distance^2
     */
    public static double calculateIntensity(double strength, double distance) {

        return strength/(distance*distance);
    }

    // TODO: Although color isn't explicitly defined in this class, all children must have some sort of color, so here's the getter method
    public abstract Color getColor();

    public abstract Point3D intersect(Ray ray);
}
