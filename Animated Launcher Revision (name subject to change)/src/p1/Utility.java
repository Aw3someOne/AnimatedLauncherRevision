package p1;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Utility {
    
    /**
     * <p>createColorARGB</p>
     * Creates a color from an string formatted in ARGB.
     * @param color color string
     * @return color
     */
    public static Color createColorARGB(String color) {
        return createColorARGB(color, true);
    }
    
    /**
     * <p>createColorARGB</p>
     * Creates a color from an string formatted in ARGB.
     * @param color color string
     * @param hasAlpha alpha
     * @return color
     */
    public static Color createColorARGB(String color, boolean hasAlpha) {
        int argb = (int) Long.decode(color).longValue();
        return new Color(argb, hasAlpha);
    }
    
    /**
     * <p>createColorRGBA</p>
     * Creates a color from a string formatted in RGBA.
     * @param color color string
     * @return color
     */
    public static Color createColorRGBA(String color) {
        int rgba = (int) Long.decode(color).longValue();
        int r = (rgba & 0xFF000000) >> 24;
        int g = (rgba & 0x00FF0000) >> 16;
        int b = (rgba & 0x0000FF00) >> 8;
        int a = rgba & 0x000000FF;
        return new Color(r, g, b, a);
    }
    
    public static BufferedImage offsetImage(BufferedImage image, int x, int y) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] rgbArray = image.getRGB(0, 0, width, height, null, 0, width);
        BufferedImage offset = new BufferedImage(width + x, height + y, BufferedImage.TYPE_INT_ARGB);
        offset.setRGB(x, y, width, height, rgbArray, 0, width);
        return offset;
    }
    
    public static BufferedImage calculateFourWayGradient(int width, int height, Color upperLeft, Color upperRight, Color lowerLeft, Color lowerRight) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        Color[] leftColors = getGradient(upperLeft, lowerLeft, height);
        Color[] rightColors = getGradient(upperRight, lowerRight, height);
        for (int y = 0; y < leftColors.length; y++) {
            GradientPaint gp = new GradientPaint(0, 1, leftColors[y], width, 1, rightColors[y]);
            g2d.setPaint(gp);
            g2d.fillRect(0, y, width, 1);
        }
        g2d.dispose();
        return image;
    }
    
    public static Color[] getGradient(Color colorInitial, Color colorFinal, int numberOfColors) {
        Color[] colors = new Color[numberOfColors];
        double colorRStep = (double) (colorFinal.getRed() - colorInitial.getRed()) / (numberOfColors - 1);
        double colorGStep = (double) (colorFinal.getGreen() - colorInitial.getGreen()) / (numberOfColors - 1);
        double colorBStep = (double) (colorFinal.getBlue() - colorInitial.getBlue()) / (numberOfColors - 1);
        double colorAStep = (double) (colorFinal.getAlpha() - colorInitial.getAlpha()) / (numberOfColors - 1);
        colors[0] = colorInitial;
        colors[numberOfColors - 1] = colorFinal;
        for (int i = 1; i < colors.length - 1; i++) {
            colors[i] = new Color((int) (colorInitial.getRed() + i * colorRStep),
                    (int) (colorInitial.getGreen() + i * colorGStep),
                    (int) (colorInitial.getBlue() + i * colorBStep),
                    (int) (colorInitial.getAlpha() + i * colorAStep));
        }
        return colors;
    }
    
    /**
     * <p>createFont</p>
     * Creates a font.
     * @param fontpath name of the font or path of the font
     * @param style font style
     * @param size font size
     * @return font
     */
    public static Font createFont(String fontpath, int style, int size) {
        Font font = null;
        try {
            InputStream fontStream = Theme.class.getClassLoader().getResourceAsStream(fontpath);
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            font = font.deriveFont(style, size);
        } catch (FontFormatException | IOException e) {
            font = new Font(fontpath, style, size);
        }
        return font;
    }
    
}
