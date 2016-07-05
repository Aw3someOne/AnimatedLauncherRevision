package p1;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
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
    
    Theme(ThemeName themeName) {
        this.themeName = themeName;
        valueMap = new HashMap<String, Integer>();
        imageMap = new HashMap<Integer, BufferedImage>();
        fontMap = new HashMap<String, Font>();
        switch(themeName) {
        case STEINS_GATE:
            valueMap.put("headerWidth", 600);
            valueMap.put("headerHeight", 50);
            valueMap.put("buttonWidth", 600);
            valueMap.put("buttonHeight", 30);
            valueMap.put("buttonSpacing", 0);
            valueMap.put("imageBound", 600);
            valueMap.put("headerTextIndent", 15);
            valueMap.put("textIndent", 25);
            valueMap.put("textIndentSteps", 25);
            valueMap.put("textIndentDuration", 100);
            valueMap.put("expandSteps", 25);
            valueMap.put("expandDuration", 200);
            valueMap.put("winX", 20);
            valueMap.put("winY", 200);
            valueMap.put("Category0ForegroundImageXOffset", 200);
            valueMap.put("Category1ForegroundImageXOffset", 260);
            valueMap.put("Category2ForegroundImageXOffset", 300);
            try {
                imageMap.put(0, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/crs_asa01a.png")));
                imageMap.put(1, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/crs_asb02a.png")));
                imageMap.put(2, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/crs_asc02a.png")));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            fontMap.put("Category0button", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 16));
            fontMap.put("Category1button", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 16));
            fontMap.put("Category2button", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 16));
            fontMap.put("Category0header", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 24));
            fontMap.put("Category1header", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 24));
            fontMap.put("Category2header", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 24));
            break;
        case YOUR_LIE_IN_APRIL:
            valueMap.put("headerWidth", 600);
            valueMap.put("headerHeight", 50);
            valueMap.put("buttonWidth", 600);
            valueMap.put("buttonHeight", 30);
            valueMap.put("buttonSpacing", 0);
            valueMap.put("imageBound", 750);
            valueMap.put("headerTextIndent", 15);
            valueMap.put("textIndent", 25);
            valueMap.put("textIndentSteps", 25);
            valueMap.put("textIndentDuration", 100);
            valueMap.put("expandSteps", 25);
            valueMap.put("expandDuration", 200);
            valueMap.put("winX", 20);
            valueMap.put("winY", 200);
            valueMap.put("Category0ForegroundImageXOffset", 275);
            valueMap.put("Category1ForegroundImageXOffset", 275);
            valueMap.put("Category2ForegroundImageXOffset", 275);
            try {
                imageMap.put(0, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/kaori3.png")));
                imageMap.put(1, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/kaori2.png")));
                imageMap.put(2, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/kaori1.png")));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            fontMap.put("Category0button", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 16));
            fontMap.put("Category1button", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 16));
            fontMap.put("Category2button", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 16));
            fontMap.put("Category0header", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 24));
            fontMap.put("Category1header", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 24));
            fontMap.put("Category2header", createFont("Michroma.ttf").deriveFont(Font.PLAIN, 24));
            break;
        case CUSTOM:
            Ini.Section section0 = Main.CONFIG.get("Category" + 0);
            Ini.Section section1 = Main.CONFIG.get("Category" + 1);
            Ini.Section section2 = Main.CONFIG.get("Category" + 2);
            valueMap.put("headerWidth", Integer.parseInt(Main.SYSTEM.get("headerWidth")));
            valueMap.put("headerHeight", Integer.parseInt(Main.SYSTEM.get("headerHeight")));
            valueMap.put("buttonWidth", Integer.parseInt(Main.SYSTEM.get("buttonWidth")));
            valueMap.put("buttonHeight", Integer.parseInt(Main.SYSTEM.get("buttonHeight")));
            valueMap.put("imageBound", Integer.parseInt(Main.SYSTEM.get("imageBound")));
            valueMap.put("buttonSpacing", Integer.parseInt(Main.SYSTEM.get("buttonSpacing")));
            valueMap.put("headerTextIndent", Integer.parseInt(Main.SYSTEM.get("headerTextIndent")));
            valueMap.put("textIndent", Integer.parseInt(Main.SYSTEM.get("textIndent")));
            valueMap.put("textIndentSteps", Integer.parseInt(Main.SYSTEM.get("textIndentSteps")));
            valueMap.put("textIndentDuration", Integer.parseInt(Main.SYSTEM.get("textIndentDuration")));
            valueMap.put("expandSteps", Integer.parseInt(Main.SYSTEM.get("expandSteps")));
            valueMap.put("expandDuration", Integer.parseInt(Main.SYSTEM.get("expandDuration")));
            valueMap.put("winX", Integer.parseInt(Main.SYSTEM.get("winX")));
            valueMap.put("winY", Integer.parseInt(Main.SYSTEM.get("winY")));
            valueMap.put("Category0ForegroundImageXOffset", Integer.parseInt(section0.get("ForegroundImageXOffset")));
            valueMap.put("Category1ForegroundImageXOffset", Integer.parseInt(section1.get("ForegroundImageXOffset")));
            valueMap.put("Category2ForegroundImageXOffset", Integer.parseInt(section2.get("ForegroundImageXOffset")));
            try {
                imageMap.put(0, ImageIO.read(new File(section0.get("ForegroundImage"))));
                imageMap.put(1, ImageIO.read(new File(section1.get("ForegroundImage"))));
                imageMap.put(2, ImageIO.read(new File(section2.get("ForegroundImage"))));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            fontMap.put("Category0button", new Font(section0.get("buttonFontFace"), Font.PLAIN, Integer.parseInt(section0.get("buttonFontSize"))));
            fontMap.put("Category1button", new Font(section1.get("buttonFontFace"), Font.PLAIN, Integer.parseInt(section1.get("buttonFontSize"))));
            fontMap.put("Category2button", new Font(section2.get("buttonFontFace"), Font.PLAIN, Integer.parseInt(section2.get("buttonFontSize"))));
            fontMap.put("Category0header", new Font(section0.get("headerFontFace"), Font.PLAIN, Integer.parseInt(section0.get("headerFontSize"))));
            fontMap.put("Category1header", new Font(section1.get("headerFontFace"), Font.PLAIN, Integer.parseInt(section1.get("headerFontSize"))));
            fontMap.put("Category2header", new Font(section2.get("headerFontFace"), Font.PLAIN, Integer.parseInt(section2.get("headerFontSize"))));
            break;
        }
    }
    
    public Font getFont(int category, int buttonNumber) {
        String buttonType;
        if (buttonNumber == -1) {
            buttonType = "header";
        } else {
            buttonType = "button";
        }
        System.out.println("Category" + category + buttonType);
        return fontMap.get("Category" + category + buttonType);
    }
    
    public int get(String key) {
        return valueMap.get(key);
    }
    
    public BufferedImage getImage(int category) {
        return imageMap.get(category);
    }
    
    private static Font createFont(String fontpath) {
        InputStream fontStream = Clock.class.getClassLoader().getResourceAsStream(fontpath);
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        } catch (FontFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GE.registerFont(font);
        return font;
    }
}
