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

/**
 * <p>Theme.</p>
 * @author Stephen Cheng
 * @version 1.0
 */
public class Theme {

    /**
     * <p>ThemeName.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    public enum ThemeName {
        STEINS_GATE,
        YOUR_LIE_IN_APRIL,
        CUSTOM;
    }
    
    /**
     * <p>GE.</p>
     */
    private static final GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
    /**
     * <p>valueMap</p>
     * Holds all the variables from ini file.
     */
    private Map<String, Integer> valueMap;
    /**
     * <p>imageMap</p>
     * Holds all the images.
     */
    private Map<Integer, BufferedImage> imageMap;
    /**
     * <p>fontMap</p>
     * Holds the fonts.
     */
    private Map<String, Font> fontMap;
    /**
     * <p>colorMap</p>
     * Holds the colors.
     */
    private Map<String, Color> colorMap;
    /**
     * <p>imageDirectory</p>
     * internal image directory.
     */
    private String imageDirectory = "images/";
    
    /**
     * <p>Theme</p>
     * Constructor.
     * @param themeName themeName
     */
    Theme(ThemeName themeName) {
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
        
        valueMap.put("headerTextIndent_i", Integer.parseInt(system.get("headerTextIndent_i")));
        valueMap.put("headerTextIndent_f", Integer.parseInt(system.get("headerTextIndent_f")));
        valueMap.put("headerTextIndentSteps", Integer.parseInt(system.get("headerTextIndentSteps")));
        valueMap.put("headerTextIndentDuration", Integer.parseInt(system.get("headerTextIndentDuration")));
        valueMap.put("headerBackgroundColorFadeSteps", Integer.parseInt(system.get("headerBackgroundColorFadeSteps")));
        valueMap.put("headerBackgroundColorFadeDuration", Integer.parseInt(system.get("headerBackgroundColorFadeDuration")));
        valueMap.put("headerFontColorFadeSteps", Integer.parseInt(system.get("headerFontColorFadeSteps")));
        valueMap.put("headerFontColorFadeDuration", Integer.parseInt(system.get("headerFontColorFadeDuration")));
        valueMap.put("buttonTextIndent_i", Integer.parseInt(system.get("buttonTextIndent_i")));
        valueMap.put("buttonTextIndent_f", Integer.parseInt(system.get("buttonTextIndent_f")));
        valueMap.put("buttonTextIndentSteps", Integer.parseInt(system.get("buttonTextIndentSteps")));
        valueMap.put("buttonTextIndentDuration", Integer.parseInt(system.get("buttonTextIndentDuration")));
        valueMap.put("buttonBackgroundColorFadeSteps", Integer.parseInt(system.get("buttonBackgroundColorFadeSteps")));
        valueMap.put("buttonBackgroundColorFadeDuration", Integer.parseInt(system.get("buttonBackgroundColorFadeDuration")));
        valueMap.put("buttonFontColorFadeSteps", Integer.parseInt(system.get("buttonFontColorFadeSteps")));
        valueMap.put("buttonFontColorFadeDuration", Integer.parseInt(system.get("buttonFontColorFadeDuration")));
        
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
            for (int i = 0; i < numberOfCategories; i++) {
                try {
                    foregroundImages[i] = ImageIO.read(new File(sections[i].get("ForegroundImage")));
                } catch (IOException e) {
                    try {
                        foregroundImages[i] = ImageIO.read(Theme.class.getClassLoader().getResourceAsStream(imageDirectory + sections[i].get("ForegroundImage")));
                    } catch (IOException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                }
            }
        } else {
            for (int i = 0; i < numberOfCategories; i++) {
                try {
                    foregroundImages[i] = ImageIO.read(Theme.class.getClassLoader().getResourceAsStream(imageDirectory + sections[i].get("ForegroundImage")));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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
    
    /**
     * <p>getFont</p>
     * Gets a font from the font map.
     * @param font font to get
     * @return font
     */
    public Font getFont(String font) {
        return fontMap.get(font);
    }
    
    /**
     * <p>getFont</p>
     * Gets a font from the font map.
     * @param categoryNumber category number
     * @param buttonType button type
     * @return font
     */
    public Font getFont(int categoryNumber, String buttonType) {
        return getFont("Category" + categoryNumber + buttonType);
    }
    
    /**
     * <p>getValue</p>
     * Gets a value from the value map.
     * @param key key
     * @return value
     */
    public int getValue(String key) {
        return valueMap.get(key);
    }
    
    /**
     * <p>getColor</p>
     * Gets a color from the color map.
     * @param color color
     * @return color
     */
    public Color getColor(String color) {
        return colorMap.get(color);
    }
    
    /**
     * <p>getColor</p>
     * Gets a color from the color map.
     * @param categoryNumber category number
     * @param buttonType button type
     * @param name name
     * @return color
     */
    public Color getColor(int categoryNumber, String buttonType, String name) {
        return getColor("Category" + categoryNumber + buttonType + name);
    }
    
    /**
     * <p>getImage</p>
     * Gets an image from the image map.
     * @param categoryNumber category number
     * @return image
     */
    public BufferedImage getImage(int categoryNumber) {
        return imageMap.get(categoryNumber);
    }

    /**
     * <p>createColorARGB</p>
     * Creates a color from an string formatted in ARGB.
     * @param color color string
     * @return color
     */
    private static Color createColorARGB(String color) {
        return createColorARGB(color, true);
    }
    
    /**
     * <p>createColorARGB</p>
     * Creates a color from an string formatted in ARGB.
     * @param color color string
     * @param hasAlpha alpha
     * @return color
     */
    private static Color createColorARGB(String color, boolean hasAlpha) {
        int argb = (int) Long.decode(color).longValue();
        return new Color(argb, hasAlpha);
    }
    
    /**
     * <p>createColorRGBA</p>
     * Creates a color from a string formatted in RGBA.
     * @param color color string
     * @return color
     */
    private static Color createColorRGBA(String color) {
        int rgba = (int) Long.decode(color).longValue();
        int r = (rgba & 0xFF000000) >> 24;
        int g = (rgba & 0x00FF0000) >> 16;
        int b = (rgba & 0x0000FF00) >> 8;
        int a = rgba & 0x000000FF;
        return new Color(r, g, b, a);
    }
    
    /**
     * <p>createFont</p>
     * Creates a font.
     * @param fontpath name of the font or path of the font
     * @param style font style
     * @param size font size
     * @return font
     */
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
    
}
