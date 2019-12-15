# Jay ray-tracing renderer
This is a second attempt at creating a Ray-tracing renderer in Java to render photorealistic scenes. It uses a ray-casting system to cast rays from each pixel to render the scene.

It can only render primitives, such as spheres. I had initially planned on implementing the ability to render complex geometry from .obj files, for example, but insurmountable bugs with the renderer cut those plans short.

You can find some sample renders (each showing various bugs) in the root directory.

## Installation
The entire source code for the rendering package is under [/src/jayrender](/src/jayrender). Once you download that package, you should be able to use it like any other Java package.

## Documentation/Use
Some sample code for using this library is uncluded under [/src](/src).

To use this package, you first import the apppropriate classes. All the essential classes are in `jayrender.geometry`, `jayrender.scene`, and `jayrender.util`.
```java
import jayrender.geometry.*;
import jayrender.scene.*;
import jayrender.util.*;
```

Then, create an `Image` object as an output file.

```java
int width = 1600;
int height = 900;

Image output = new Image("output.png", width, height);
```

Then you initialize objects related to the scene. First, create a `Camera`, then a `Scene`. The `Scene` will hold all the objects in the world.

```java
// Create a camera
Camera sceneCam = new Camera(
    new Point3D(0,0,0),            // Position of camera viewplane
    new Tuple2D(width, height),    // Camera resolution
    1,                             // Size of the viewplane square (will be scaled)
    Projection.PERSPECTIVE         // Camera perspective
);

// Create the scene
Scene scene = new Scene(
    new Color(75, 75, 75),        // Background color: gray
    sceneCam
);
```

Create and add some objects:

```java
Sphere sphere = new Sphere(new Point3D(100, 0, 0), 5, new Color(190, 190, 255));
BulbLight light = new BulbLight(new Point3D(65, 15, 0), 15, 12500));

scene.addObject(sphere);
scene.addLight(light);
```

Finally, render:

```java
scene.render(output);
```

## License
This project is licensed under the MIT License. You can find more information in the [LICENSE.md](LICENSE.md) file.
