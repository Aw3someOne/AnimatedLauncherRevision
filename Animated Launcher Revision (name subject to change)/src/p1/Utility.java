package p1;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Utility {
    
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
    
    public static Color[] getGradient(Color color_i, Color color_f, int numberOfColors) {
        Color[] colors = new Color[numberOfColors];
        double colorRStep = (double) (color_f.getRed() - color_i.getRed()) / (numberOfColors - 1);
        double colorGStep = (double) (color_f.getGreen() - color_i.getGreen()) / (numberOfColors - 1);
        double colorBStep = (double) (color_f.getBlue() - color_i.getBlue()) / (numberOfColors - 1);
        double colorAStep = (double) (color_f.getAlpha() - color_i.getAlpha()) / (numberOfColors - 1);
        colors[0] = color_i;
        colors[numberOfColors - 1] = color_f;
        for (int i = 1; i < colors.length - 1; i++) {
            colors[i] = new Color((int) (color_i.getRed() + i * colorRStep),
                    (int) (color_i.getGreen() + i * colorGStep),
                    (int) (color_i.getBlue() + i * colorBStep),
                    (int) (color_i.getAlpha() + i * colorAStep));
        }
        return colors;
    }
    
    public static void main(String[] args) {
        Color[] colors = getGradient(new Color(255,255,255,255), new Color(0,0,0,0), 4);
        for (Color c : colors) {
            System.out.println(c);
        }
    }
    
}
