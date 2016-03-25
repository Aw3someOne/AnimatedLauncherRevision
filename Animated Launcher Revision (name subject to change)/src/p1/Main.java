package p1;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import net.miginfocom.swing.MigLayout;

public class Main extends JFrame {

    public static Ini config;
    private JPanel panel;
    private int numberOfCategories;
    private int headerWidth;
    private int headerHeight;
    private int buttonWidth;
    private int buttonHeight;
    private int textIndent;
    
    public Main() throws InvalidFileFormatException, IOException {
        super("Animated Launcher Revision");
        setLayout(new MigLayout("wrap 1, insets 0",
                "[grow, fill]",
                "[grow, fill]"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(false);
        setFocusableWindowState(false);
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        
        config = new Ini(new FileReader("config.ini"));
        
        panel = new JPanel(new MigLayout("wrap 1, insets 0",
                "[grow, fill]",
                "[grow, fill]"));
        
        readSystemVariables();
        
        pack();
        setVisible(true);
//        toBack();
    }

    private void readSystemVariables() throws NumberFormatException, IOException {
        Ini.Section system = config.get("System");
        numberOfCategories = Integer.parseInt(system.get("numberOfCategories"));
        headerWidth = Integer.parseInt(system.get("headerWidth"));
        headerHeight = Integer.parseInt(system.get("headerHeight"));
        buttonWidth = Integer.parseInt(system.get("buttonWidth"));
        buttonHeight = Integer.parseInt(system.get("buttonHeight"));
        textIndent = Integer.parseInt(system.get("textIndent"));
        String hue = system.get("backgroundColor");
        Integer v1 = 0xFFFFFFEE;
        System.out.println(v1);
        Color color = new Color(v1, true);
    }
    
    public static void main(String[] args) throws InvalidFileFormatException, IOException {
        new Main();
    }

}
