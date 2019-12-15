package jayrender.scene;

import jayrender.util.*;
import jayrender.util.base.Ray;

public class Camera {

    public Tuple2D res;     // Resolution of the camera
    public double  size;    // The physical size of the square viewplane

    // TODO: Rotation vec
    private Point3D eye;    // Position of the 'eye' of cam. People don't need to know about this
    private Point3D pos;    // Position of the center of the viewplane
    private Projection projection;

    public Camera() {

        res         = new Tuple2D(1000, 1000);      // 1:1 ratio
        size        = 1;                                  // 1x1 viewplane

        eye         = new Point3D(-0.866, 0, 0); // Z-up, so 'behind' the projection. 60 degree FOV
        pos         = new Point3D(0, 0, 0);      // View from the origin
        projection  = Projection.ORTHOGRAPHIC;
    }
    public Camera(Point3D pos, Tuple2D res, double viewplaneSize, Projection projection) {

        this.res        = res;
        this.size       = viewplaneSize;

        this.eye        = new Point3D(pos.x-0.866*viewplaneSize, pos.y, pos.z);
        this.pos        = pos;
        this.projection = projection;
    }

    public Point3D getEye() {
        return eye;
    }
    public Point3D getPos() {
        return pos;
    }

    public void setProjection(Projection projection) {

        this.projection = projection;
    }

    public void moveTo(Point3D newPos) {

        Vector3D translationVector = new Vector3D(pos, newPos);

        pos.add(translationVector);
        eye.add(translationVector);
    }
    public void moveEye(Point3D newPos) {

        eye = new Point3D(newPos);
    }

    /**
     * TODO: Implement anti-aliasing?
     * FIXME: If there isn't a bug in this current implementation then I'm a wizard
     * @return
     */
    public Ray[][] castRays() {

        Tuple2D unitAspectRatio = res.scale();      // Scale the resolution into a vector that has either 1 for x or y

        // Multiply viewplane dim by the scale aspect ratio in order to scale it in 1 direction
        Tuple2D viewDimensions = new Tuple2D(size*unitAspectRatio.x, size*unitAspectRatio.y); // X is width, not axis

        Ray[][] castedRays = new Ray[(int) res.y][(int) res.x];     // Row col

        // TODO: Calculate rotation translations ONCE everytime the camera is moved, not every time rays are casted
        // Calculate starting points x, y, z, and ending points

        // Find theta (XY axis) - if it's already 90 degrees then no translations needed
        // Go to png if unclear
        double focalLengthXY    = Math.sqrt( (pos.x-eye.x)*(pos.x-eye.x) + (pos.y-eye.y)*(pos.y-eye.y) ); // Magnitude from eye to pos
        double thetaXY          = Math.toDegrees(Math.asin((pos.x-eye.x)/focalLengthXY));
        double phiXY            = 90-thetaXY;

        // Now do the same thing for XZ
        double focalLengthXZ    = Math.sqrt( (pos.x-eye.x)*(pos.x-eye.x) + (pos.z-eye.z)*(pos.z-eye.z));
        double thetaXZ          = Math.toDegrees(Math.asin((pos.x-eye.x)/focalLengthXZ));
        double phiXZ            = 90-thetaXZ;

        double dx  = viewDimensions.x*Math.sin(phiXY)/2;
        double dy  = dx*Math.tan(90-(180-phiXY)/2);
        double dx2 = viewDimensions.y*Math.sin(phiXZ)/2;
        double dz  = dx2*Math.tan(90-(180-phiXZ)/2);

        // Points ABCD where relative to the EYE, A is topright, B is topleft, C is bottom left, D is bottom right
        // Calculate original points then translate them
        Point3D A = new Point3D(pos.x, pos.y - viewDimensions.x/2, pos.z + viewDimensions.y/2);
        Point3D B = new Point3D(pos.x, pos.y + viewDimensions.x/2, pos.z + viewDimensions.y/2);
        Point3D C = new Point3D(pos.x, pos.y + viewDimensions.x/2, pos.z - viewDimensions.y/2);
        Point3D D = new Point3D(pos.x, pos.y - viewDimensions.x/2, pos.z - viewDimensions.y/2);

        translate(A, B, C, D, dx, dy, dx2, dz);

        // Calculate what transformation 'half a pixel' is in order to cast rays from the center of pixels (both horiz and vert)
        // This transformation is in percent, so in reality it's just 1/(2*dimension)
        double xHalfPixel = 1/(2*res.x);    // Width
        double yHalfPixel = 1/(2*res.y);    // Height

        // Calculate the orthographic vector (in case the camera is in ortho mode - all rays have to be perpendicular
        // to the viewplane, and the ray that goes from the eye to pos SHOULD (TM) be perpendicular
        Vector3D perpendicularVector = new Vector3D(eye, pos);     // No normalization

        // With the corner points correctly placed now, iterate through all the points inbetween and raycast
        // Rows then columns
        // cast to int in order to avoid some weird epsilon error
        // FIXME: BUGGY
        for (int y = 0; y < (int) res.y; y++) {

            for (int x = 0; x < (int) res.x; x++) {

                // Calculate the percent X and percent Y that is traveled
                // Lerp from B to C and then to lerp to point that is determined from lerping from A to D
                Point3D yLerp1 = Interpolation.spacialLerp(B, C, y/res.y + yHalfPixel);
                Point3D yLerp2 = Interpolation.spacialLerp(A, D, y/res.y + yHalfPixel);

                // Now lerp from 1 to 2
                Point3D pixelPoint = Interpolation.spacialLerp(yLerp1, yLerp2, x/res.x + xHalfPixel);

                // Finally, depending on the projection, either cast a ray from eye to pixel point or pixel point and vector
                if (projection == Projection.ORTHOGRAPHIC) {

                    castedRays[y][x] = new Ray(pixelPoint, perpendicularVector);
                } else {

                    castedRays[y][x] = new Ray(eye, pixelPoint);
                }
            }
        }

        return castedRays;
    }

    // Apply correct transformations in place
    private void translate(Point3D A, Point3D B, Point3D C, Point3D D, double dx, double dy, double dx2, double dz) {

        // AD and BC for top-down transformation (so moving on the XY plane)
        // dx and dy are only calculated for the BC line so we have to invert the transformations for AD

        // BC Line segment
        B.x += dx;
        B.y += dy;
        C.x += dx;
        C.y += dy;

        // AD line segment (note the operator flip)
        A.x -= dx;
        A.y -= dy;
        D.x -= dx;
        D.y -= dy;

        // XZ transformations apply to AB and CD
        // same caveats
        A.x += dx2;
        A.z += dz;
        B.x += dx2;
        B.z += dz;

        // CD line segment
        C.x += dx2;
        C.z += dz;
        D.x += dx2;
        D.z += dz;
    }
}
