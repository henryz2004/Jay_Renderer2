package jayrender.util;

public class Interpolation {

    private Interpolation() {

    }

    /**
     * Linear interpolation on one axis from point a to point b
     */
    public static double axialLerp(Point3D a, Point3D b, double step, Axes axes) {

        double aPos = 0;
        double bPos = 0;

        switch (axes) {

            case X:
                aPos = a.x;
                bPos = b.x;
                break;

            case Y:
                aPos = a.y;
                bPos = b.y;
                break;

            case Z:
                aPos = a.z;
                bPos = b.z;
                break;
        }

        double distanceTraveled = step * (bPos - aPos);

        return aPos + distanceTraveled;
    }
    public static Point3D spacialLerp(Point3D a, Point3D b, double step) {

        return new Point3D(
                axialLerp(a, b, step, Axes.X),
                axialLerp(a, b, step, Axes.Y),
                axialLerp(a, b, step, Axes.Z)
        );
    }
}
