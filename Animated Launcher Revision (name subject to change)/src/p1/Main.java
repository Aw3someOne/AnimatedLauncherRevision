package p1;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import net.miginfocom.swing.MigLayout;
import p1.Theme.ThemeName;

/**
 * <p>Main.</p>
 * @author Stephen Cheng
 * @version 1.0
 */
public class Main extends JFrame {

    /**
     * <p>CLEAR</p>
     * Transparent color.
     */
    public static final Color CLEAR = new Color(0, 0, 0, 0);
    /**
     * <p>CONFIG</p>
     * Ini file.
     */
    public static final Ini CONFIG;
    /**
     * <p>serialVersionUID.</p>
     */
    private static final long serialVersionUID = 8805910311072461857L;
    static {
        FileReader fr = null;
        Ini ini = null;
        try {
            fr = new FileReader("config.ini");
            ini = new Ini(fr);
        } catch (IOException e) {
            // auto-extracts included config.ini if config.ini does not exist
            InputStream stream = Main.class.getClassLoader().getResourceAsStream("config.ini");
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(new File("config.ini"));
                copyStream(stream, os);
                fr = new FileReader("config.ini");
                ini = new Ini(fr);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        CONFIG = ini;
    }
    /**
     * <p>SYSTEM</p>
     * System section of CONFIG.
     */
    public static final Ini.Section SYSTEM = CONFIG.get("System");
    /**
     * THEME_NAME
     * Theme.
     */
    public static final ThemeName THEME_NAME;
    static {
        ThemeName themeName = null;
        String themeString = SYSTEM.get("theme");
        for (ThemeName t : ThemeName.values()) {
            if (ThemeName.valueOf(themeString).equals(t)) {
                themeName = t;
                break;
            }
        }
        THEME_NAME = themeName;
    }
    /**
     * <p>THEME</p>
     * Theme.
     */
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
    
    /**
     * <p>categoryArray</p>
     * Array containing all the category objects.
     */
    public static Category[] categoryArray;
    /**
     * <p>maxHeight</p>
     * Maximum height of the window (is set later).
     */
    private int maxHeight;
    /**
     * <p>panel</p>
     * Panel that contains all the categories.
     */
    private JPanel panel;
    
    /**
     * <p>Main</p>
     * Constructor.
     * @throws InvalidFileFormatException
     * @throws IOException
     */
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
    
    /**
     * <p>repack</p>
     * Resizes the window.
     */
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

    /**
     * <p>copyStream</p>
     * Copies a stream.
     * @param input stream to copy
     * @param output stream to write to
     * @throws IOException IO error
     */
    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.close();
    }
    
    /**
     * <p>main</p>
     * Entry point for the JVM.
     * @param args
     * @throws InvalidFileFormatException error
     * @throws IOException IO error
     */
    public static void main(String[] args) throws InvalidFileFormatException, IOException {
        new Clock();
        new Main();
    }
}
