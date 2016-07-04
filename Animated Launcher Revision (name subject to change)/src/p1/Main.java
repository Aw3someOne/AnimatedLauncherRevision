package p1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

    public static final Ini CONFIG;
    public static final Ini.Section SYSTEM;
    public static final int HEADER_WIDTH;
    public static final int HEADER_HEIGHT;
    public static final int BUTTON_WIDTH;
    public static final int IMAGE_BOUND;
    public static final int BUTTON_HEIGHT;
    public static final int BUTTON_SPACING;
    public static final int HEADER_TEXT_INDENT;
    public static final int TEXT_INDENT;
    public static final int TEXT_INDENT_STEPS;
    public static final int TEXT_INDENT_DURATION;
    public static final int TEXT_INDENT_SLEEP;
    public static final int EXPAND_STEPS;
    public static final int EXPAND_DURATION;
    public static final int EXPAND_SLEEP;
    public static final int WIN_X;
    public static final int WIN_Y;
    public static final Color CLEAR = new Color(0,0,0,0);
    public static final ThemeName THEME_NAME;
    public static final Theme THEME;
    public static final int NUMBER_OF_CATEGORIES;
    public static Category[] categoryArray;
    public int maxHeight = 0;
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
            // Reads constants from config.ini
            CONFIG = ini;
            SYSTEM = CONFIG.get("System");
            String themeString = SYSTEM.get("theme");
            ThemeName themeName = null;
            for (ThemeName t : ThemeName.values()) {
                if (ThemeName.valueOf(themeString).equals(t)) {
                    themeName = t;
                    break;
                }
            }
            THEME_NAME = themeName;
            THEME = new Theme(THEME_NAME);
            HEADER_WIDTH = THEME.get("headerWidth");
            HEADER_HEIGHT = THEME.get("headerHeight");
            BUTTON_WIDTH = THEME.get("buttonWidth");
            IMAGE_BOUND = THEME.get("imageBound");
            BUTTON_HEIGHT = THEME.get("buttonHeight");
            BUTTON_SPACING = THEME.get("buttonSpacing");
            HEADER_TEXT_INDENT = THEME.get("headerTextIndent");
            TEXT_INDENT = THEME.get("textIndent");
            TEXT_INDENT_STEPS = THEME.get("textIndentSteps");
            TEXT_INDENT_DURATION = THEME.get("textIndentDuration");
            TEXT_INDENT_SLEEP = TEXT_INDENT_DURATION / TEXT_INDENT_STEPS;
            EXPAND_STEPS = THEME.get("expandSteps");
            EXPAND_DURATION = THEME.get("expandDuration");
            EXPAND_SLEEP = EXPAND_DURATION / EXPAND_STEPS;
            WIN_X = THEME.get("winX");
            WIN_Y = THEME.get("winY");
            NUMBER_OF_CATEGORIES = Integer.parseInt(SYSTEM.get("numberOfCategories"));
        }
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
<<<<<<< HEAD
    
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
=======
>>>>>>> branch 'master' of https://SoreAru@bitbucket.org/SoreAru/animated-launcher-revision.git

    public static void main(String[] args) throws InvalidFileFormatException, IOException {
        new Clock();
        new Main();
    }
}
