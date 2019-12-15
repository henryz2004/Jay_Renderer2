package jayrender.util.render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

    public BufferedImage buffer;
    public File image;

    public Image(String filename, int width, int height) {

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image  = new File(filename);
    }

    public void bufferWrite(int x, int y, Color color) {

        buffer.setRGB(x, y, color.toInteger());
    }
    public void fileWrite(String filetype) {

        try {
            ImageIO.write(buffer, filetype, image);

        } catch (IOException e) {
            System.out.println("Could not write buffer contents to file");
        }
    }
}
