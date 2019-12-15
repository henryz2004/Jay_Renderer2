import jayrender.geometry.Sphere;
import jayrender.scene.Camera;
import jayrender.scene.Projection;
import jayrender.scene.Scene;
import jayrender.scene.lighting.BulbLight;
import jayrender.util.Point3D;
import jayrender.util.Tuple2D;
import jayrender.util.Vector3D;
import jayrender.util.render.Color;
import jayrender.util.render.Image;

public class RenderTest {

    public static void main(String[] args) {

        final int width = 1500;
        final int height = 1800;

        // Create output image file
        Image rendered = new Image("Render.png", width, height);

        // Create scene
        Camera sceneCam = new Camera(
                new Point3D(0,0,0),   // Position of camera viewplane
                new Tuple2D(width, height),    // Camera resolution
                1,                // Size of the viewplane square (will be scaled) (doesn't matter I believe)
                Projection.PERSPECTIVE         // Camera perspective
        );

        Scene scene = new Scene(
                new Color(75, 75, 75),        // Background color: gray
                sceneCam
        );

        Sphere sphere = new Sphere(new Point3D(100, 0, 0), 5, new Color(190, 190, 255));

        //scene.addObject(new Sphere(new Point3D(100, 0,0), 10, new Color(200, 200, 200)));
        scene.addObject(sphere);
        scene.addObject(new Sphere(new Point3D(125, 15, 0), 10, new Color(190, 190, 255)));

        scene.addLight(new BulbLight(new Point3D(65, 15, 0), 15, 12500));

        scene.render(rendered);
    }
}
