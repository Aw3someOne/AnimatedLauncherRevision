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
     * <p>BackgroundColorMode.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    public enum BackgroundColorMode {
        SOLID,
        VERTICAL_GRADIENT,
        HORIZONTAL_GRADIENT,
        HORIZONTAL_BANDS,
        HORIZONTAL_BANDED_GRADIENT,
        FOUR_WAY_GRADIENT;
    }
    
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
        
        BackgroundColorMode bgMode = BackgroundColorMode.SOLID;
        String bgModeString = system.get("bgMode");
        for (BackgroundColorMode b : BackgroundColorMode.values()) {
            if (BackgroundColorMode.valueOf(bgModeString).equals(b)) {
                bgMode = b;
                break;
            }
        }
        valueMap.put("backgroundColorMode", bgMode.ordinal());
        
        valueMap.put("expandSteps", Integer.parseInt(system.get("expandSteps")));
        valueMap.put("expandDuration", Integer.parseInt(system.get("expandDuration")));
        valueMap.put("winX", Integer.parseInt(system.get("winX")));
        valueMap.put("winY", Integer.parseInt(system.get("winY")));
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
            buttonFonts[i] = Utility.createFont(sections[i].get("buttonFontFace"), Font.PLAIN, Integer.parseInt(sections[i].get("buttonFontSize")));
            headerFonts[i] = Utility.createFont(sections[i].get("headerFontFace"), Font.PLAIN, Integer.parseInt(sections[i].get("headerFontSize")));
            int xCrop = Integer.parseInt(sections[i].get("ForegroundImageXCrop"));
            int yCrop = Integer.parseInt(sections[i].get("ForegroundImageYCrop"));
            int xOffset = Integer.parseInt(sections[i].get("ForegroundImageXOffset"));
            int yOffset = Integer.parseInt(sections[i].get("ForegroundImageYOffset"));
            foregroundImages[i] = foregroundImages[i].getSubimage(xCrop, yCrop, foregroundImages[i].getWidth() - xCrop, foregroundImages[i].getHeight() - yCrop);
            foregroundImages[i] = Utility.offsetImage(foregroundImages[i], xOffset, yOffset);
            imageMap.put(i, foregroundImages[i]);
            fontMap.put("Category" + i + "button", buttonFonts[i]);
            fontMap.put("Category" + i + "header", headerFonts[i]);
            colorMap.put("Category" + i + "headerBackgroundColor_i", Utility.createColorARGB(sections[i].get("headerBackgroundColor_i")));
            colorMap.put("Category" + i + "headerBackgroundColor_f", Utility.createColorARGB(sections[i].get("headerBackgroundColor_f")));
            colorMap.put("Category" + i + "buttonBackgroundColor_i", Utility.createColorARGB(sections[i].get("buttonBackgroundColor_i")));
            colorMap.put("Category" + i + "buttonBackgroundColor_f", Utility.createColorARGB(sections[i].get("buttonBackgroundColor_f")));
            colorMap.put("Category" + i + "headerFontColor_i", Utility.createColorARGB(sections[i].get("headerFontColor_i")));
            colorMap.put("Category" + i + "headerFontColor_f", Utility.createColorARGB(sections[i].get("headerFontColor_f")));
            colorMap.put("Category" + i + "buttonFontColor_i", Utility.createColorARGB(sections[i].get("buttonFontColor_i")));
            colorMap.put("Category" + i + "buttonFontColor_f", Utility.createColorARGB(sections[i].get("buttonFontColor_f")));
            switch (bgMode) {
            case SOLID:
                break;
            case VERTICAL_GRADIENT:
            case HORIZONTAL_GRADIENT:
            case HORIZONTAL_BANDS:
                colorMap.put("Category" + i + "headerBackgroundColorGradientStartInitial", Utility.createColorARGB(sections[i].get("headerBackgroundColorGradientStartInitial")));
                colorMap.put("Category" + i + "headerBackgroundColorGradientEndInitial", Utility.createColorARGB(sections[i].get("headerBackgroundColorGradientEndInitial")));
                colorMap.put("Category" + i + "headerBackgroundColorGradientStartFinal", Utility.createColorARGB(sections[i].get("headerBackgroundColorGradientStartFinal")));
                colorMap.put("Category" + i + "headerBackgroundColorGradientEndFinal", Utility.createColorARGB(sections[i].get("headerBackgroundColorGradientEndFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorGradientStartInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorGradientStartInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorGradientEndInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorGradientEndInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorGradientStartFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorGradientStartFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorGradientEndFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorGradientEndFinal")));
                break;
            case HORIZONTAL_BANDED_GRADIENT:
                colorMap.put("Category" + i + "headerBackgroundColorGradientStartInitial", Utility.createColorARGB(sections[i].get("headerBackgroundColorGradientStartInitial")));
                colorMap.put("Category" + i + "headerBackgroundColorGradientEndInitial", Utility.createColorARGB(sections[i].get("headerBackgroundColorGradientEndInitial")));
                colorMap.put("Category" + i + "headerBackgroundColorGradientStartFinal", Utility.createColorARGB(sections[i].get("headerBackgroundColorGradientStartFinal")));
                colorMap.put("Category" + i + "headerBackgroundColorGradientEndFinal", Utility.createColorARGB(sections[i].get("headerBackgroundColorGradientEndFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorUpperLeftInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorUpperLeftInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorLowerLeftInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorLowerLeftInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorUpperRightInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorUpperRightInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorLowerRightInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorLowerRightInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorUpperLeftFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorUpperLeftFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorLowerLeftFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorLowerLeftFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorUpperRightFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorUpperRightFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorLowerRightFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorLowerRightFinal")));
                break;
            case FOUR_WAY_GRADIENT:
                colorMap.put("Category" + i + "headerBackgroundColorUpperLeftInitial", Utility.createColorARGB(sections[i].get("headerBackgroundColorUpperLeftInitial")));
                colorMap.put("Category" + i + "headerBackgroundColorLowerLeftInitial", Utility.createColorARGB(sections[i].get("headerBackgroundColorLowerLeftInitial")));
                colorMap.put("Category" + i + "headerBackgroundColorUpperRightInitial", Utility.createColorARGB(sections[i].get("headerBackgroundColorUpperRightInitial")));
                colorMap.put("Category" + i + "headerBackgroundColorLowerRightInitial", Utility.createColorARGB(sections[i].get("headerBackgroundColorLowerRightInitial")));
                colorMap.put("Category" + i + "headerBackgroundColorUpperLeftFinal", Utility.createColorARGB(sections[i].get("headerBackgroundColorUpperLeftFinal")));
                colorMap.put("Category" + i + "headerBackgroundColorLowerLeftFinal", Utility.createColorARGB(sections[i].get("headerBackgroundColorLowerLeftFinal")));
                colorMap.put("Category" + i + "headerBackgroundColorUpperRightFinal", Utility.createColorARGB(sections[i].get("headerBackgroundColorUpperRightFinal")));
                colorMap.put("Category" + i + "headerBackgroundColorLowerRightFinal", Utility.createColorARGB(sections[i].get("headerBackgroundColorLowerRightFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorUpperLeftInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorUpperLeftInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorLowerLeftInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorLowerLeftInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorUpperRightInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorUpperRightInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorLowerRightInitial", Utility.createColorARGB(sections[i].get("buttonBackgroundColorLowerRightInitial")));
                colorMap.put("Category" + i + "buttonBackgroundColorUpperLeftFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorUpperLeftFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorLowerLeftFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorLowerLeftFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorUpperRightFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorUpperRightFinal")));
                colorMap.put("Category" + i + "buttonBackgroundColorLowerRightFinal", Utility.createColorARGB(sections[i].get("buttonBackgroundColorLowerRightFinal")));
                break;
            }
        }
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
    
}
