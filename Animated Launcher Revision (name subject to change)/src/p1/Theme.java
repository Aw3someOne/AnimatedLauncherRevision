package p1;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.ini4j.Ini;

public class Theme {

    public enum ThemeName {
        STEINS_GATE,
        YOUR_LIE_IN_APRIL,
        CUSTOM;
    }
    
    private static final GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private ThemeName themeName;
    private String imageDirectory;
    private Map<String, Integer> valueMap;
    private Map<Integer, BufferedImage> imageMap;
    private Map<String, Font> fontMap;
    private Map<String, Color> colorMap;
    
    Theme(ThemeName themeName) {
        this.themeName = themeName;
        valueMap = new HashMap<String, Integer>();
        imageMap = new HashMap<Integer, BufferedImage>();
        fontMap = new HashMap<String, Font>();
        colorMap = new HashMap<String, Color>();
        FileReader fr = null;
        Ini config = null;
        InputStream stream = null;
        switch(themeName) {
        case STEINS_GATE:
            stream = Theme.class.getClassLoader().getResourceAsStream("stein.ini");
            try {
                config = new Ini(stream);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            break;
        case YOUR_LIE_IN_APRIL:
            stream = Theme.class.getClassLoader().getResourceAsStream("april.ini");
            try {
                config = new Ini(stream);
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            break;
        case CUSTOM:
            config = Main.CONFIG;
            break;
        }
        Ini.Section clock = config.get("Clock");
        Ini.Section system = config.get("System");
        int numberOfCategories = Integer.parseInt(system.get("numberOfCategories"));
        Ini.Section[] sections = new Ini.Section[numberOfCategories];
        for (int i = 0; i < numberOfCategories; i++) {
            sections[i] = config.get("Category" + i);
        }
        valueMap.put("headerWidth", Integer.parseInt(system.get("headerWidth")));
        valueMap.put("headerHeight", Integer.parseInt(system.get("headerHeight")));
        valueMap.put("buttonWidth", Integer.parseInt(system.get("buttonWidth")));
        valueMap.put("buttonHeight", Integer.parseInt(system.get("buttonHeight")));
        valueMap.put("imageBound", Integer.parseInt(system.get("imageBound")));
        valueMap.put("buttonSpacing", Integer.parseInt(system.get("buttonSpacing")));
        valueMap.put("headerTextIndent", Integer.parseInt(system.get("headerTextIndent")));
        valueMap.put("textIndent", Integer.parseInt(system.get("textIndent")));
        valueMap.put("textIndentSteps", Integer.parseInt(system.get("textIndentSteps")));
        valueMap.put("textIndentDuration", Integer.parseInt(system.get("textIndentDuration")));
        valueMap.put("expandSteps", Integer.parseInt(system.get("expandSteps")));
        valueMap.put("expandDuration", Integer.parseInt(system.get("expandDuration")));
        valueMap.put("winX", Integer.parseInt(system.get("winX")));
        valueMap.put("winY", Integer.parseInt(system.get("winY")));
        valueMap.put("Category0ForegroundImageXOffset", Integer.parseInt(sections[0].get("ForegroundImageXOffset")));
        valueMap.put("Category1ForegroundImageXOffset", Integer.parseInt(sections[1].get("ForegroundImageXOffset")));
        valueMap.put("Category2ForegroundImageXOffset", Integer.parseInt(sections[2].get("ForegroundImageXOffset")));
        
        valueMap.put("clockWinX", Integer.parseInt(clock.get("winX")));
        valueMap.put("clockWinY", Integer.parseInt(clock.get("winY")));
        BufferedImage[] foregroundImages = new BufferedImage[numberOfCategories];
        Font[] buttonFonts = new Font[numberOfCategories];
        Font[] headerFonts = new Font[numberOfCategories];
        if (themeName == ThemeName.CUSTOM) {
            try {
                for (int i = 0; i < numberOfCategories; i++) {
                    foregroundImages[i] = ImageIO.read(new File(sections[i].get("ForegroundImage")));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                for (int i = 0; i < numberOfCategories; i++) {
                    foregroundImages[i] = ImageIO.read(Theme.class.getClassLoader().getResourceAsStream(sections[i].get("ForegroundImage")));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        for (int i = 0; i < numberOfCategories; i++) {
            buttonFonts[i] = createFont(sections[i].get("buttonFontFace"), Font.PLAIN, Integer.parseInt(sections[i].get("buttonFontSize")));
            headerFonts[i] = createFont(sections[i].get("headerFontFace"), Font.PLAIN, Integer.parseInt(sections[i].get("headerFontSize")));
            imageMap.put(i, foregroundImages[i]);
            fontMap.put("Category" + i + "button", buttonFonts[i]);
            fontMap.put("Category" + i + "header", headerFonts[i]);
            colorMap.put("Category" + i + "headerBackgroundColor_i", createColorARGB(sections[i].get("headerBackgroundColor_i")));
            colorMap.put("Category" + i + "headerBackgroundColor_f", createColorARGB(sections[i].get("headerBackgroundColor_f")));
            colorMap.put("Category" + i + "buttonBackgroundColor_i", createColorARGB(sections[i].get("buttonBackgroundColor_i")));
            colorMap.put("Category" + i + "buttonBackgroundColor_f", createColorARGB(sections[i].get("buttonBackgroundColor_f")));
            colorMap.put("Category" + i + "headerFontColor_i", createColorARGB(sections[i].get("headerFontColor_i")));
            colorMap.put("Category" + i + "headerFontColor_f", createColorARGB(sections[i].get("headerFontColor_f")));
            colorMap.put("Category" + i + "buttonFontColor_i", createColorARGB(sections[i].get("buttonFontColor_i")));
            colorMap.put("Category" + i + "buttonFontColor_f", createColorARGB(sections[i].get("buttonFontColor_f")));

            fontMap.put("clockFont", createFont(clock.get("clockFontFace"), Font.PLAIN, Integer.parseInt(clock.get("clockFontSize"))));
            fontMap.put("smallFont", createFont(clock.get("smallFontFace"), Font.PLAIN, Integer.parseInt(clock.get("smallFontSize"))));
            fontMap.put("textFont", createFont(clock.get("textFontFace"), Font.PLAIN, Integer.parseInt(clock.get("textFontSize"))));
            colorMap.put("clockFontColor", createColorARGB(clock.get("clockFontColor")));
        }
    }
    
    public Font getFont(String font) {
        return fontMap.get(font);
    }
    
    public Font getFont(int category, String buttonType) {
        return getFont("Category" + category + buttonType);
    }
    
    public int getValue(String key) {
        return valueMap.get(key);
    }
    
    public Color getColor(String color) {
        return colorMap.get(color);
    }
    
    public Color getColor(int category, String buttonType, String name) {
        return getColor("Category" + category + buttonType + name);
    }
    
    public BufferedImage getImage(int category) {
        return imageMap.get(category);
    }

    private static Color createColorARGB(String color) {
        return createColorARGB(color, true);
    }
    
    private static Color createColorARGB(String color, boolean hasAlpha) {
        int argb = (int) Long.decode(color).longValue();
        return new Color(argb, hasAlpha);
    }
    
    private static Color createColorRGBA(String color) {
        int rgba = (int) Long.decode(color).longValue();
        int r = (rgba & 0xFF000000) >> 24;
        int g = (rgba & 0x00FF0000) >> 16;
        int b = (rgba & 0x0000FF00) >> 8;
        int a = rgba & 0x000000FF;
        return new Color(r, g, b, a);
    }
    
    private static Font createFont(String fontpath, int style, int size) {
        Font font = null;
        try {
            InputStream fontStream = Theme.class.getClassLoader().getResourceAsStream(fontpath);
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            GE.registerFont(font);
            font = font.deriveFont(style, size);
        } catch (FontFormatException | IOException e) {
            font = new Font(fontpath, style, size);
        }
        return font;
    }
    
//    private static Font createFont(String fontpath) {
//        InputStream fontStream = Theme.class.getClassLoader().getResourceAsStream(fontpath);
//        Font font = null;
//        try {
//            font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
//        } catch (FontFormatException | IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        GE.registerFont(font);
//        return font;
//    }
}
