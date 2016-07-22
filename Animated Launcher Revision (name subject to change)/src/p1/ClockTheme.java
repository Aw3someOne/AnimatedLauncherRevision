package p1;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Ini;

import net.miginfocom.swing.MigLayout;

public class ClockTheme {

    public enum ThemeName {
        CLASSIC_DIGITAL,
        MICHROMA,
        CUSTOM;
    }
    
    /**
     * <p>valueMap</p>
     * Holds all the variables from ini file.
     */
    private Map<String, Integer> valueMap;
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
    private MigLayout migLayout;
    private MigLayout rightMigLayout;
    private String textCase;
    
    ClockTheme(ThemeName themeName) {
        valueMap = new HashMap<String, Integer>();
        fontMap = new HashMap<String, Font>();
        colorMap = new HashMap<String, Color>();
        FileReader fr = null;
        Ini config = null;
        InputStream stream = null;
        Ini.Section clock = null;
        switch(themeName) {
        case CLASSIC_DIGITAL:
            stream = ClockTheme.class.getClassLoader().getResourceAsStream("digital.ini");
            try {
                config = new Ini(stream);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            clock = config.get("Clock");
            migLayout = new MigLayout("wrap 2, insets 0",
                    "0[fill]10[fill]0",
                    "0[fill]-20[fill]");
            rightMigLayout = new MigLayout("insets 0",
                    "0[fill]0",
                    "10[fill]-10[fill]0");
            fontMap.put("clockFont", Utility.createFont(clock.get("clockFontFace"), Font.PLAIN, Integer.parseInt(clock.get("clockFontSize"))));
            fontMap.put("smallFont", Utility.createFont(clock.get("smallFontFace"), Font.PLAIN, Integer.parseInt(clock.get("smallFontSize"))));
            fontMap.put("textFont", Utility.createFont(clock.get("textFontFace"), Font.PLAIN, Integer.parseInt(clock.get("textFontSize"))));
            break;
        case MICHROMA:
            stream = ClockTheme.class.getClassLoader().getResourceAsStream("michroma.ini");
            try {
                config = new Ini(stream);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            clock = config.get("Clock");
            migLayout = new MigLayout("wrap 2, insets 0",
                    "0[fill]" + clock.get("horizontalSpacing") + "[fill]0",
                    "0[fill]" + clock.get("verticalSpacing") + "[fill]");
            fontMap.put("font", Utility.createFont(clock.get("fontFace"), Font.PLAIN, Integer.parseInt(clock.get("fontSize"))));
            textCase = clock.get("case");
            break;
        case CUSTOM:
            config = Main.CONFIG;
            clock = config.get("Clock");
            break;
        }
        valueMap.put("clockWinX", Integer.parseInt(clock.get("winX")));
        valueMap.put("clockWinY", Integer.parseInt(clock.get("winY")));
        colorMap.put("clockFontColor", Utility.createColorARGB(clock.get("clockFontColor")));
    }
    
    public MigLayout getLayout() {
        return migLayout;
    }
    
    public MigLayout getRightPanelLayout() {
        return rightMigLayout;
    }
    
    public String getCase() {
        return textCase;
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
     * <p>getColor</p>
     * Gets a color from the color map.
     * @param color color
     * @return color
     */
    public Color getColor(String color) {
        return colorMap.get(color);
    }
    
}
