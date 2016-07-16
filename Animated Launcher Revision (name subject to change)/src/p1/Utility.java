package p1;

import java.awt.Color;

public class Utility {
    
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
