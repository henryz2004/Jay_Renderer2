package jayrender.scene;

import jayrender.geometry.PhysicalObject;
import jayrender.scene.lighting.Emitter;
import jayrender.util.Normal;
import jayrender.util.Point3D;
import jayrender.util.Tuple2D;
import jayrender.util.Vector3D;
import jayrender.util.base.Ray;
import jayrender.util.render.Color;
import jayrender.util.render.Image;

import java.util.*;

public class Scene {

    public Color backgroundColor;

    private Camera camera;
    private List<PhysicalObject> world;
    private List<Emitter> lights;

    public Scene() {

        backgroundColor = new Color(0, 0, 0);

        camera = new Camera();
        world = new ArrayList<>();
        lights = new ArrayList<>();
    }

    public Scene(Tuple2D res) {

        backgroundColor = new Color(0, 0, 0);

        camera = new Camera(new Point3D(0, 0, 0), res, 10, Projection.ORTHOGRAPHIC);
        world = new ArrayList<>();
        lights = new ArrayList<>();
    }

    public Scene(Color backgroundColor, Camera camera) {

        this.backgroundColor = backgroundColor;

        this.camera = camera;
        this.world = new ArrayList<>();
        this.lights = new ArrayList<>();
    }

    public void addObject(PhysicalObject obj) {

        world.add(obj);
    }

    public void addLight(Emitter light) {

        lights.add(light);
    }

    public boolean removeObject(PhysicalObject obj) {

        return world.remove(obj);
    }   // Take in object

    public boolean removeLight(Emitter light) {

        return lights.remove(light);
    }

    public void setProjection(Projection projection) {

        camera.setProjection(projection);
    }

    public void moveCamera(Point3D to) {

        camera.moveTo(to);
    }

    public void moveCameraEye(Point3D to) {

        camera.moveEye(to);
    }

    public void render(Image outputImage) {

        // Gather the light rays that have been casted from
        Ray[][] rayArray = camera.castRays();

        // TODO: Use stack for ray tracing
        // Assuming that light doesn't bounce off of lights

        // Stack diagram:
        // RAY (that's being cast)
        // INT (the x value of the screen point that will get updated eventually)
        // INT (the y value of the screen point that will get updated eventually)
        // INT (representing the index of the stage of bounces) (as in index to an array representing how many new rays to split into upon intersection with object)
        // All that must be bundled together in a class
        // When a cast is done (no intersection for example), update Map<Integer, Map<Integer, List<Color>>> where the first map contains y values, second contains x, third contains the list of colors

        Map<Integer, Map<Integer, List<Color>>> pixelColorFactors = new HashMap<>();

        Deque<BounceRay> rayStack = new ArrayDeque<>();

        // Push in all the rays cast by the camera into the stack
        // A slight break from protocol - no line break after for
        for (int y = 0; y < rayArray.length; y++) {
            for (int x = 0; x < rayArray[0].length; x++) {
                rayStack.push(new BounceRay(rayArray[y][x], x, y, new Color(0,0,0), 0, 0));
            }
        }

        int[] rayBounces = {20, 1, 1};    // How many rays to split into in each step of the bounce

        // Keep on bouncing rays until there's no more rays to get bounced
        while (!rayStack.isEmpty()) {

            BounceRay bounceRay = rayStack.pop();

            // Intersection variables
            double closestHit     = Double.POSITIVE_INFINITY;
            Point3D closestHitPos = null;

            PhysicalObject hit = null;          // Object intersected by ray

            Emitter hitLight = null;            // Keeps track of which light is closest

            // Physical object intersection test

            for (PhysicalObject object : world) {

                Point3D intersection = object.intersect(bounceRay.ray);
                if (intersection != null) {

                    // Calculate distance of intersection and confirm that the distance is less than closestHit
                    double intersectionDistance = new Vector3D(bounceRay.ray.origin, intersection).magnitude();

                    if (intersectionDistance < closestHit) {

                        closestHit = intersectionDistance;
                        closestHitPos = intersection;

                        hit = object;

                        // Only update the color of the ray if the ray hasn't been bounced yet
                        if (bounceRay.bounces == 0) {

                            bounceRay.colorFactor = object.texture.color;
                        }
                    }
                }
            }

            // Light intersection test

            for (Emitter light : lights) {

                Point3D intersection = light.intersect(bounceRay.ray);
                if (intersection != null) {

                    double intersectionDistance = new Vector3D(bounceRay.ray.origin, intersection).magnitude();

                    if (intersectionDistance < closestHit && bounceRay.bounces > 0) {

                        closestHit    = intersectionDistance;
                        closestHitPos = intersection;

                        hitLight = light;
                    }
                }
            }

            // Update ray color if
            //    a. light was hit
            //    b. object was hit (and no more bounces) -> black
            //    c. nothing was hit at all (direct ray)    -> background color
            if (hitLight != null) {
                //bounceRay.colorFactor = hitLight.getColor();        // TODO: somehow multiply color, strength, and object color

            } else if (/*hit == null && */bounceRay.bounces >= 1) {
                bounceRay.colorFactor = new Color(0,0,0);

            } else if (hit == null && bounceRay.bounces == 0) {
                bounceRay.colorFactor = backgroundColor;

            }

            // If light was hit or end of ray bouncing or nothing hit then update pixel color
            // Otherwise continue to bounce
            if (hitLight != null || bounceRay.bounces >= rayBounces.length || hit == null) {

                // FIXME: Shadows? wtf?

                pixelColorFactors
                        .computeIfAbsent(bounceRay.y, k -> new HashMap<>())
                        .computeIfAbsent(bounceRay.x, k -> new ArrayList<>())
                        .add(bounceRay.colorFactor);
            } else {

                // Calculate normal of hit object
                Normal normal = hit.calculateNormal(closestHitPos); // hm...

                // Cast new rays
                for (int rayIndex = 0; rayIndex < rayBounces[bounceRay.bounces]; rayIndex++) {

                    // https://math.stackexchange.com/questions/13261/how-to-get-a-reflection-vector
                    // r = d - 2(d . n)n

                    Vector3D reflectionVector = bounceRay.ray.direction.sub(
                            normal.mult(2*bounceRay.ray.direction.dot(normal))
                    );

                    Point3D rayOrigin = closestHitPos.add(normal);   // FIXME? start a bit above the hit (use the normal)

                    // FIXME: HALO--- does it hit the light then turn dark?

                    rayStack.push(
                            new BounceRay(
                                    new Ray(rayOrigin, reflectionVector.noiseNormalize()/*Vector3D.randomUnit()*/),
                                    bounceRay.x,
                                    bounceRay.y,
                                    bounceRay.colorFactor,
                                    bounceRay.distTraveled + closestHit,
                                    bounceRay.bounces + 1
                            )
                    );
                }
            }
        }


        // With our nested maps of colors, take average then update buffer
        for (Map.Entry<Integer, Map<Integer, List<Color>>> yEntry : pixelColorFactors.entrySet()) {

            int y = yEntry.getKey();

            for (Map.Entry<Integer, List<Color>> xEntry : yEntry.getValue().entrySet()) {

                int x = xEntry.getKey();

                outputImage.bufferWrite(x, y, Color.average(xEntry.getValue()));
            }
        }

        outputImage.fileWrite("PNG");
    }

    private class BounceRay {

        Ray ray;
        Color colorFactor;    // Color of this ray
        double distTraveled;   // How far this ray had traveled previously
        int bounces;        // Keeps track of the number of bounces
        int x;
        int y;

        BounceRay(Ray ray, int x, int y) {

            this.ray = ray;
            this.colorFactor = new Color(0, 0, 0);
            this.distTraveled = 0;
            this.bounces = 0;
            this.x = x;
            this.y = y;
        }

        BounceRay(Ray ray, int x, int y, Color colorFactor, double distTraveled, int bounces) {

            this.ray = ray;
            this.colorFactor = colorFactor;
            this.distTraveled = distTraveled;
            this.bounces = bounces;
            this.x = x;
            this.y = y;
        }
    }
}

/*for (int y = 0; y < rayArray.length; y++) {

            for (int x = 0; x < rayArray[0].length; x++) {

                Ray ray = rayArray[y][x];

                // Physical object intersection

                // Keep track of the closest object intersected
                double closestHit = Double.POSITIVE_INFINITY;

                Color pixelColor = new Color(backgroundColor);

                // Check if ray collides with any object
                for (PhysicalObject object : world) {

                    Point3D intersection = object.intersect(ray);
                    if (!intersection.placeholder) {

                        // Calculate distance of intersection and confirm that the distance is less than closestHit
                        double intersectionDistance = new Vector3D(ray.origin, intersection).magnitude();

                        if (intersectionDistance < closestHit) {

                            pixelColor = object.texture.color;
                            closestHit = intersectionDistance;
                        }
                    }
                }

                // Emitter (light) intersection TODO: Activate/deactivate this

                // Make sure any intersections are even closer than any object intersections
                // That means keep closestHit
                // no need for inverse square - light doesn't get darker the further away you are (eek)

                // Check if ray collides with light
                for (Emitter light : lights) {

                    Point3D intersection = light.intersect(ray);
                    if (!intersection.placeholder) {

                        double intersectionDistance = new Vector3D(ray.origin, intersection).magnitude();

                        if (intersectionDistance < closestHit) {

                            pixelColor = light.getColor();
                            closestHit = intersectionDistance;
                        }
                    }
                }

                outputImage.bufferWrite(x, y, pixelColor);
            }
        }*/