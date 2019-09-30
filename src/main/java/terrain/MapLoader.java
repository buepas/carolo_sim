package terrain;

import org.joml.Vector2f;
import org.joml.Vector2i;
import util.IoUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;

public class MapLoader {

    private BufferedImage mapImg;
    private BufferedImage scaledImg;
    private JFrame jFrame;

    private int width;
    private int height;
    private boolean hasAlphaChannel;
    private int pixelLength;
    private byte[] pixels;

    public MapLoader(String filename) {
        loadImage(filename);
    }

    public void loadImage(String fileName) {
        String filePath = "/assets/textures/" + fileName + ".png";
        try {
            //load image
            InputStream source = IoUtil.class.getResourceAsStream(filePath);
            mapImg = ImageIO.read(source);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pixels = ((DataBufferByte) mapImg.getRaster().getDataBuffer()).getData();
        width = mapImg.getWidth();
        height = mapImg.getHeight();
        hasAlphaChannel = mapImg.getAlphaRaster() != null;
        pixelLength = 3;
        if (hasAlphaChannel)
        {
            pixelLength = 4;
        }
    }

    public int getRGB(int x, int y)
    {
        int pos = (y * pixelLength * width) + (x * pixelLength);

        int argb = -16777216; // 255 alpha
        if (hasAlphaChannel)
        {
            argb = (((int) pixels[pos++] & 0xff) << 24); // alpha
        }

        argb += ((int) pixels[pos++] & 0xff); // blue
        argb += (((int) pixels[pos++] & 0xff) << 8); // green
        argb += (((int) pixels[pos++] & 0xff) << 16); // red

        return argb;
    }

    public int getBlue(Vector2i mapCoords) {
        return getColor(mapCoords, 0);
    }

    public int getGreen(Vector2i mapCoords) {
        return getColor(mapCoords, 1);
    }

    public int getRed(Vector2i mapCoords) {
        return getColor(mapCoords, 2);
    }

    private int getColor(Vector2i mapCoords, int offset) {
        if (hasAlphaChannel) {
            offset++;
        }
        int pos;
        try {
            pos = (mapCoords.y * pixelLength * width) + (mapCoords.x * pixelLength) + offset;
        } catch (ArrayIndexOutOfBoundsException e) {
            pos = 255;
        }
        return (int) pixels[pos] & 0xff;
    }

//    //corner coordinates of the pixel in world coords
//    public float[] mapToWorld(int x, int y) {
//        return new float[]{
//                (x+1) * Terrain.getSize() / (float) width,
//                (y+1) * Terrain.getSize() / (float) height,
//                x * Terrain.getSize() / (float) width,
//                y * Terrain.getSize() / (float) height
//        };
//    }

    public Vector2i worldToMap(Vector2f world) {
        return new Vector2i(
                (int) (world.x / Terrain.getSize() * width),
                (int) (world.y / Terrain.getSize() * height)
        );
    }

    public void showImage(int dim) {

        Image tmp = mapImg.getScaledInstance(dim, dim, Image.SCALE_SMOOTH);
        scaledImg = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaledImg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();


        if(jFrame != null) {
            jFrame.dispose();
        }
        jFrame = new JFrame();
        jFrame.getContentPane().setLayout(new FlowLayout());
        jFrame.getContentPane().add(new JLabel(new ImageIcon(scaledImg)));
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        MapLoader mapLoader = new MapLoader("red");
        mapLoader.showImage(400);
    }

    public int getWidth() {
        return width;
    }
}
