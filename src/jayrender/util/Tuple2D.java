package jayrender.util;

import jayrender.util.base.XY;

/**
 * Since both points and vectors have very specific uses, I figured a more general
 * class would be nice for just storing two XY values
 */
public class Tuple2D extends XY {

    public Tuple2D(double x, double y) {
        super(x, y);
    }
    public Tuple2D(Tuple2D copy) {
        super(copy);
    }

    public Tuple2D add(XY xy) {
        return new Tuple2D(x+xy.x, y+xy.y);
    }
    public Tuple2D sub(XY xy) {
        return new Tuple2D(x-xy.x, y-xy.y);
    }
    public Tuple2D mult(double scalar) {
        return new Tuple2D(x*scalar, y*scalar);
    }
    public Tuple2D div(double scalar) {
        return new Tuple2D(x/scalar, y/scalar);
    }

    public String toString() {
        return "Tuple2D (" + x + ", " + y + ")";
    }
}
