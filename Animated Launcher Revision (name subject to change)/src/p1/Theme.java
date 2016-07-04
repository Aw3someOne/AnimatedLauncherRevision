package p1;

import java.awt.image.BufferedImage;
import java.io.IOException;
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
    
    private ThemeName themeName;
    private String imageDirectory;
    private Map<String, Integer> valueMap;
    private Map<Integer, BufferedImage> imageMap;
    
    Theme(ThemeName themeName) {
        this.themeName = themeName;
        valueMap = new HashMap<String, Integer>();
        imageMap = new HashMap<Integer, BufferedImage>();
        switch(themeName) {
        case STEINS_GATE:
            break;
        case YOUR_LIE_IN_APRIL:
            valueMap.put("headerWidth", 600);
            valueMap.put("headerHeight", 50);
            valueMap.put("buttonWidth", 600);
            valueMap.put("buttonHeight", 30);
            valueMap.put("buttonSpacing", 0);
            valueMap.put("imageBound", 700);
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
                imageMap.put(0, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/kaori1.png")));
                imageMap.put(1, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/kaori2.png")));
                imageMap.put(2, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/kaori1.png")));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
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
                imageMap.put(0, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/" + section0.get("ForegroundImage"))));
                imageMap.put(1, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/" + section1.get("ForegroundImage"))));
                imageMap.put(2, ImageIO.read(Theme.class.getClassLoader().getResourceAsStream("images/" + section2.get("ForegroundImage"))));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        }
    }
    
    public int get(String key) {
        return valueMap.get(key);
    }
    
    public BufferedImage getImage(int category) {
        return imageMap.get(category);
    }
}
