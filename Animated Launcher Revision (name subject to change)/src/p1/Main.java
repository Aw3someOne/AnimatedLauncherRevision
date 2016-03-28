package p1;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import net.miginfocom.swing.MigLayout;

public class Main extends JFrame {

    public static final Ini CONFIG;
    public static final Ini.Section SYSTEM;
    public static final int HEADER_WIDTH;
    public static final int HEADER_HEIGHT;
    public static final int BUTTON_WIDTH;
    public static final int BUTTON_HEIGHT;
    public static final int HEADER_TEXT_INDENT;
    public static final int TEXT_INDENT;
    public static final int TEXT_INDENT_STEPS;
    public static final int TEXT_INDENT_DURATION;
    public static final int TEXT_INDENT_SLEEP;
    public static final int EXPAND_STEPS;
    public static final int EXPAND_DURATION;
    public static final int EXPAND_SLEEP;
    public static final Color CLEAR = new Color(0,0,0,0);
    public static Category[] categoryArray;
    static {
        FileReader fr = null;
        Ini ini = null;
            try {
                fr = new FileReader("config.ini");
                ini = new Ini(fr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CONFIG = ini;
            SYSTEM = CONFIG.get("System");
            HEADER_WIDTH = Integer.parseInt(SYSTEM.get("headerWidth"));
            HEADER_HEIGHT = Integer.parseInt(SYSTEM.get("headerHeight"));
            BUTTON_WIDTH = Integer.parseInt(SYSTEM.get("buttonWidth"));
            BUTTON_HEIGHT = Integer.parseInt(SYSTEM.get("buttonHeight"));
            HEADER_TEXT_INDENT = Integer.parseInt(SYSTEM.get("headerTextIndent"));
            TEXT_INDENT = Integer.parseInt(SYSTEM.get("textIndent"));
            TEXT_INDENT_STEPS = Integer.parseInt(SYSTEM.get("textIndentSteps"));
            TEXT_INDENT_DURATION = Integer.parseInt(SYSTEM.get("textIndentDuration"));
            TEXT_INDENT_SLEEP = TEXT_INDENT_DURATION / TEXT_INDENT_STEPS;
            EXPAND_STEPS = Integer.parseInt(SYSTEM.get("expandSteps"));
            EXPAND_DURATION = Integer.parseInt(SYSTEM.get("expandDuration"));
            EXPAND_SLEEP = EXPAND_DURATION / EXPAND_STEPS;
        }
    private JPanel panel;
    private int numberOfCategories;
    
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
        
        numberOfCategories = Integer.parseInt(SYSTEM.get("numberOfCategories"));
        categoryArray = new Category[numberOfCategories];
        for (int i = 0; i < numberOfCategories; i++) {
            Category category = new Category(i);
            categoryArray[i] = category;
            panel.add(category);
        }
        
        getContentPane().add(panel);
        
        pack();
        
        setVisible(true);
        
        for (Category cat : categoryArray) {
            cat.calculateMaxHeight();
        }
        
        for (Category cat : categoryArray) {
            cat.collapse();
        }
//        toBack();
    }
    
    public static void main(String[] args) throws InvalidFileFormatException, IOException {
        new Main();
    }
}
