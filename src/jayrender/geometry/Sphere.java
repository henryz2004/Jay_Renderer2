package jayrender.geometry;

import jayrender.util.Normal;
import jayrender.util.Point3D;
import jayrender.util.Vector3D;
import jayrender.util.base.Ray;
import jayrender.util.render.Color;
import jayrender.util.render.Texture;

public class Sphere extends PhysicalObject {

    private double radius;

    public Sphere(double x, double y, double z, double radius, Color color) {

        super(x, y, z, new Texture(color));

        this.radius = radius;
    }
    public Sphere(Point3D center, double radius, Color color) {

        super(center, new Texture(color));

        this.radius = radius;
    }

    public Point3D intersect(Ray ray) {

        // Make some vectors (i.e. pos to ray origin, etc.), points, and doubles
        Vector3D rayToCenter      = new Vector3D(ray.origin, pos);                   // Vector going from the ray's origin to center of sphere
        Vector3D centerProjection = ray.direction.mult(ray.direction.dot(rayToCenter)); // Vector rayToCenter projected onto ray.direction

        Point3D projectionPoint   = ray.origin.add(centerProjection);   // Projected center point on ray direction

        double sphProjDistance    = new Vector3D(pos, projectionPoint).magnitude();  // Distance from center to projectionPoint
        double rayProjDistance    = centerProjection.magnitude();                       // Distance from ray origin to projectionPoint

        // Case 1: check if ray is in front or behind of object
        // Do dot product (ray direction and rayToCenter). If positive then angles are acute, circle can be 'seen'
        if (ray.direction.dot(rayToCenter) <= 0) {

            return null;   // No intersection
        }

        // Case 2: if radius is smaller than sphProjDistance, no intersection
        if (radius < sphProjDistance) {

            return null;
        }

        // Case 3: if radius is the same as sphProjDistance, tangent- intersection is projectionPoint
        if (Math.abs(radius - sphProjDistance) < EPSILON) {

            return projectionPoint;
        }

        // Case 4: ray hits sphere directly - two intersection points
        // Calculate distance of ray to intersection point by pythagorean therom and then intersection point
        double projCollisionMagnitude   = radius*radius - sphProjDistance*sphProjDistance;  // Distance from projection point to collision point
        double originCollisionMagnitude = rayProjDistance - projCollisionMagnitude;         // Distance from ray origin to collision point

        // Find intersection point by extending the ray, basically
        return ray.origin.add(ray.direction.mult(originCollisionMagnitude));
    }
    public Normal calculateNormal(Point3D intersection) {

        // Make vector from center to intersection, normalize. That is the normal
        Vector3D normalVector = new Vector3D(pos, intersection);

        Normal normal = new Normal(normalVector);
        normal.normalize();

        return normal;
    }
}
