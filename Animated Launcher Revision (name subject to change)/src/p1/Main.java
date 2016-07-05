package p1;

import java.awt.Color;
import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import net.miginfocom.swing.MigLayout;
import p1.Theme.ThemeName;

public class Main extends JFrame {

    public static final Color CLEAR = new Color(0,0,0,0);
    public static final Ini CONFIG;
    static {
        FileReader fr = null;
        Ini ini = null;
        try {
            fr = new FileReader("config.ini");
            ini = new Ini(fr);
        } catch (IOException e) {
            // loads included default config.ini if config.ini does not exist
            e.printStackTrace();
            InputStream stream = Main.class.getClassLoader().getResourceAsStream("config.ini");
            try {
                ini = new Ini(stream);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        CONFIG = ini;
    }
    public static final Ini.Section SYSTEM = CONFIG.get("System");
    private static String themeString = SYSTEM.get("theme");
    public static final ThemeName THEME_NAME;
    static {
        ThemeName themeName = null;
        for (ThemeName t : ThemeName.values()) {
            if (ThemeName.valueOf(themeString).equals(t)) {
                themeName = t;
                break;
            }
        }
        THEME_NAME = themeName;
    }
    public static final Theme THEME = new Theme(THEME_NAME);
    public static final int HEADER_WIDTH = THEME.getValue("headerWidth");
    public static final int HEADER_HEIGHT = THEME.getValue("headerHeight");
    public static final int BUTTON_WIDTH = THEME.getValue("buttonWidth");
    public static final int IMAGE_BOUND = THEME.getValue("imageBound");
    public static final int BUTTON_HEIGHT = THEME.getValue("buttonHeight");
    public static final int BUTTON_SPACING = THEME.getValue("buttonSpacing");
    public static final int HEADER_TEXT_INDENT = THEME.getValue("headerTextIndent");
    public static final int TEXT_INDENT = THEME.getValue("textIndent");
    public static final int TEXT_INDENT_STEPS = THEME.getValue("textIndentSteps");
    public static final int TEXT_INDENT_DURATION = THEME.getValue("textIndentDuration");
    public static final int TEXT_INDENT_SLEEP = TEXT_INDENT_DURATION / TEXT_INDENT_STEPS;
    public static final int EXPAND_STEPS = THEME.getValue("expandSteps");
    public static final int EXPAND_DURATION = THEME.getValue("expandDuration");
    public static final int EXPAND_SLEEP = EXPAND_DURATION / EXPAND_STEPS;
    public static final int WIN_X = THEME.getValue("winX");
    public static final int WIN_Y = THEME.getValue("winY");
    public static final int NUMBER_OF_CATEGORIES = Integer.parseInt(SYSTEM.get("numberOfCategories"));
    
    public static Category[] categoryArray;
    public int maxHeight = 0;
    private JPanel panel;
    
    public Main() throws InvalidFileFormatException, IOException {
        super("Animated Launcher Revision");
        setLayout(new MigLayout("wrap 1, insets 0",
                "[fill]0",
                "[fill]0"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(false);
        setFocusableWindowState(false);
        setUndecorated(true);
        setBackground(CLEAR);
        
        panel = new JPanel(new MigLayout("wrap 1, insets 0",
                "[fill]0",
                "[fill]0"));
        panel.setBackground(CLEAR);
        
        categoryArray = new Category[NUMBER_OF_CATEGORIES];
        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            Category category = new Category(i, this);
            categoryArray[i] = category;
            panel.add(category);
        }
        
        getContentPane().add(panel);
        pack();
        setVisible(false);
        
        for (Category cat : categoryArray) {
            cat.calculateMaxHeight();
        }
        
        for (Category cat : categoryArray) {
            cat.collapseInstant();
        }
        
        setVisible(true);
//         Places window on the desktop. Commented out to make testing less annoying.
//        toBack();
        setLocation(new Point(WIN_X, WIN_Y));
    }
    
    public void repack() {
        int width = this.getWidth();
        int height = 0;
        for (Category cat : categoryArray) {
            height += cat.getHeight();
        }
        if (height > maxHeight) {
            maxHeight = height;
            setBounds(WIN_X, WIN_Y, width, maxHeight);
        }
    }

    public static void main(String[] args) throws InvalidFileFormatException, IOException {
        new Clock();
        new Main();
    }
}
