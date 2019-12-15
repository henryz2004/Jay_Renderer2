package jayrender.util.render;

import jayrender.util.Point3D;
import jayrender.util.Vector3D;
import jayrender.util.base.Ray;

public class LightRay extends Ray {

    public Color color;     // The return color of the ray

    public LightRay(Point3D origin, Vector3D direction) {

        super(origin, direction);
    }
    public LightRay(Point3D origin, Point3D through) {

        super(origin, through);
    }
}
