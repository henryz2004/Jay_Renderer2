package jayrender.util.render;

import jayrender.math.MathUtils;

import java.util.List;

public class Color {

    public float r, g, b;	// OpenGL converts ints to floats, let's just use floats

    public Color() {
        r = 0.0f;
        g = 0.0f;
        b = 0.0f;
    }
    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public Color(int r, int g, int b) {
        this.r = r/255f;
        this.g = g/255f;
        this.b = b/255f;
    }
    public Color(Color color) {
        r = color.r;
        g = color.g;
        b = color.b;
    }

    public Color add(Color color) {

        Color modified = new Color(this);
        modified.addIP(color);

        return modified;
    }
    public Color sub(Color color) {

        Color modified = new Color(this);
        modified.subIP(color);

        return modified;
    }
    public Color mult(double scalar) {

        Color modified = new Color(this);
        modified.multIP(scalar);

        return modified;
    }
    public Color div(double scalar) {

        Color modified = new Color(this);
        modified.divIP(scalar);

        return modified;
    }
    public void addIP(Color color) {

        r += color.r;
        g += color.g;
        b += color.b;

        clamp();
    }
    public void subIP(Color color) {

        r -= color.r;
        g -= color.g;
        b -= color.b;

        clamp();
    }
    public void multIP(double scalar) {

        r *= scalar;
        g *= scalar;
        b *= scalar;

        clamp();
    }
    public void divIP(double scalar) {

        r /= scalar;
        g /= scalar;
        b /= scalar;

        clamp();
    }

    public static Color average(List<Color> colors) {

        Color averageColor = new Color();

        for (Color color : colors) {
            averageColor.addIP(color.div(colors.size()));
        }

        return averageColor;
    }

    public void clamp() {

        r = (float) MathUtils.clamp(0, 1, r);
        g = (float) MathUtils.clamp(0, 1, g);
        b = (float) MathUtils.clamp(0, 1, b);
    }

    public int toInteger() {
        return (int)(r*255)<<16|(int)(g*255)<<8|(int)(b*255);
    }

    public String toString() {
        return "Color (" + r + ", " + g + ", " + b + ")";
    }
}
