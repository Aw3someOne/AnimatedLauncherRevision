package p1;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.ini4j.Ini;

import net.miginfocom.swing.MigLayout;

public class ImageButton extends JPanel {
    
    private int category;
    private int buttonNumber;
    private String text;
    private String action;
    private int width;
    private int height;
    
    private int backgroundColorRInitial;
    private int backgroundColorGInitial;
    private int backgroundColorBInitial;
    private int backgroundColorAInitial;
    private Color backgroundColorInitial;
    
    private Color backgroundColorCurrent;
    
    private int backgroundColorRFinal;
    private int backgroundColorGFinal;
    private int backgroundColorBFinal;
    private int backgroundColorAFinal;
    private Color backgroundColorFinal;
   
    JLabel label;
    JLabel shadow;
    
    private int fontColorRInitial;
    private int fontColorGInitial;
    private int fontColorBInitial;
    private int fontColorAInitial;
    private Color fontColorInitial;
    
    private int fontColorRFinal;
    private int fontColorGFinal;
    private int fontColorBFinal;
    private int fontColorAFinal;
    private Color fontColorFinal;
    
    private int indent;
    private int indentSteps;
    private int indentDuration;
    private int indentSleep;
    private Timer indentTimer;
    private Timer unindentTimer;
    
    private Font font;
    
    public ImageButton(int category, int buttonNumber) {
        this.category = category;
        this.buttonNumber = buttonNumber;
        readVariables(category, buttonNumber);
        createButton();
        indentTimer = new Timer(indentSleep, new IndentTimerListener());
        unindentTimer = new Timer(indentSleep, new UnindentTimerListener());
        addMouseListener(new ImageButtonMouseAdapter());
    }
    
    public ImageButton(int category) {
        this.category = category;
        readVariables(category);
        createButton();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D)g;
        gui.setBackground(backgroundColorCurrent);
        gui.clearRect(0, 0, width, height);
    }

    private void createButton() {
        setLayout(new MigLayout("wrap 1, insets 0",
                "[grow, fill," + width + "]",
                "[grow, fill," + height + "]"));
        backgroundColorCurrent = backgroundColorInitial;
        label = new JLabel(text);
        label.setFont(font);
        label.setForeground(fontColorInitial);
        label.setOpaque(false);
        label.setBackground(Main.CLEAR);
        add(label, "id label, pos " + indent + " 0.5al");
        shadow = new JLabel(text);
        shadow.setFont(font);
        shadow.setForeground(Color.black);
        shadow.setOpaque(false);
        add(shadow, "pos (label.x + 2) (label.y + 2)");
        
    }
    
    private void readVariables(int category, int buttonNumber) {
        Ini.Section section = Main.CONFIG.get("Category" + category);
        text = section.get("button" + buttonNumber +"Text");
        action = section.get("button" + buttonNumber +"Action");
        width = Main.BUTTON_WIDTH;
        height = Main.BUTTON_HEIGHT;
        
        String fontFace = section.get("buttonFontFace");
        int fontSize = Integer.parseInt(section.get("buttonFontSize"));
        
        font = new Font(fontFace, Font.PLAIN, fontSize);
        indent = Main.TEXT_INDENT;
        indentSteps = Main.TEXT_INDENT_STEPS;
        indentDuration = Main.TEXT_INDENT_DURATION;
        indentSleep = Main.TEXT_INDENT_SLEEP;
        
        backgroundColorRInitial = Integer.parseInt(section.get("buttonBackgroundColorR_i"));
        backgroundColorGInitial = Integer.parseInt(section.get("buttonBackgroundColorG_i"));
        backgroundColorBInitial = Integer.parseInt(section.get("buttonBackgroundColorB_i"));
        backgroundColorAInitial = Integer.parseInt(section.get("buttonBackgroundColorA_i"));
        backgroundColorInitial = new Color(backgroundColorRInitial, backgroundColorGInitial, backgroundColorBInitial, backgroundColorAInitial);
        
        backgroundColorRFinal = Integer.parseInt(section.get("buttonBackgroundColorR_f"));
        backgroundColorGFinal = Integer.parseInt(section.get("buttonBackgroundColorG_f"));
        backgroundColorBFinal = Integer.parseInt(section.get("buttonBackgroundColorB_f"));
        backgroundColorAFinal = Integer.parseInt(section.get("buttonBackgroundColorA_f"));
        backgroundColorFinal = new Color(backgroundColorRFinal, backgroundColorGFinal, backgroundColorBFinal, backgroundColorAFinal);
        
        fontColorRInitial = Integer.parseInt(section.get("buttonFontColorR_i"));
        fontColorGInitial = Integer.parseInt(section.get("buttonFontColorG_i"));
        fontColorBInitial = Integer.parseInt(section.get("buttonFontColorB_i"));
        fontColorAInitial = Integer.parseInt(section.get("buttonFontColorA_i"));
        fontColorInitial = new Color(fontColorRInitial, fontColorGInitial, fontColorBInitial, fontColorAInitial);
        
        fontColorRFinal = Integer.parseInt(section.get("buttonFontColorR_f"));
        fontColorGFinal = Integer.parseInt(section.get("buttonFontColorG_f"));
        fontColorBFinal = Integer.parseInt(section.get("buttonFontColorB_f"));
        fontColorAFinal = Integer.parseInt(section.get("buttonFontColorA_f"));
        fontColorFinal = new Color(fontColorRFinal, fontColorGFinal, fontColorBFinal, fontColorAFinal);
    }
    
    private void readVariables(int category) {
        Ini.Section section = Main.CONFIG.get("Category" + category);
        text = section.get("headerText");
        width = Main.HEADER_WIDTH;
        
        height = Main.HEADER_HEIGHT;
        
        String fontFace = section.get("headerFontFace");
        int fontSize = Integer.parseInt(section.get("headerFontSize"));
        
        indent = Main.TEXT_INDENT;
        indentSteps = Main.TEXT_INDENT_STEPS;
        indentDuration = Main.TEXT_INDENT_DURATION;
        indentSleep = Main.TEXT_INDENT_SLEEP;
        
        font = new Font(fontFace, Font.PLAIN, fontSize);
        indent = Main.HEADER_TEXT_INDENT;
        backgroundColorRInitial = Integer.parseInt(section.get("headerBackgroundColorR_i"));
        backgroundColorGInitial = Integer.parseInt(section.get("headerBackgroundColorG_i"));
        backgroundColorBInitial = Integer.parseInt(section.get("headerBackgroundColorB_i"));
        backgroundColorAInitial = Integer.parseInt(section.get("headerBackgroundColorA_i"));
        backgroundColorInitial = new Color(backgroundColorRInitial, backgroundColorGInitial, backgroundColorBInitial, backgroundColorAInitial);
        
        backgroundColorRFinal = Integer.parseInt(section.get("headerBackgroundColorR_f"));
        backgroundColorGFinal = Integer.parseInt(section.get("headerBackgroundColorG_f"));
        backgroundColorBFinal = Integer.parseInt(section.get("headerBackgroundColorB_f"));
        backgroundColorAFinal = Integer.parseInt(section.get("headerBackgroundColorA_f"));
        backgroundColorFinal = new Color(backgroundColorRFinal, backgroundColorGFinal, backgroundColorBFinal, backgroundColorAFinal);
        
        fontColorRInitial = Integer.parseInt(section.get("headerFontColorR_i"));
        fontColorGInitial = Integer.parseInt(section.get("headerFontColorG_i"));
        fontColorBInitial = Integer.parseInt(section.get("headerFontColorB_i"));
        fontColorAInitial = Integer.parseInt(section.get("headerFontColorA_i"));
        fontColorInitial = new Color(fontColorRInitial, fontColorGInitial, fontColorBInitial, fontColorAInitial);
        
        fontColorRFinal = Integer.parseInt(section.get("headerFontColorR_f"));
        fontColorGFinal = Integer.parseInt(section.get("headerFontColorG_f"));
        fontColorBFinal = Integer.parseInt(section.get("headerFontColorB_f"));
        fontColorAFinal = Integer.parseInt(section.get("headerFontColorA_f"));
        fontColorFinal = new Color(fontColorRFinal, fontColorGFinal, fontColorBFinal, fontColorAFinal);
    }
    
    private class ImageButtonMouseAdapter extends MouseAdapter {
        public void mouseEntered(MouseEvent e) {
            unindentTimer.stop();
            indentTimer.start();
        }
        
        public void mouseExited(MouseEvent e) {
            indentTimer.stop();
            unindentTimer.start();
        }
        
        public void mouseReleased(MouseEvent e) {
            try {
                run();
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                uri();
                return;
            } catch (URISyntaxException | IOException e1) {
                e1.printStackTrace();
            }
            try {
                System.out.println(action);
                open();
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    
    private void run() throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process process = rt.exec(action);
    }
    
    private void uri() throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        URI uri = new URI(action);
        desktop.browse(uri);
    }
    
    private void open() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(action));
    }
    
    private void buttonTextStepIndent() {
        int xPos = label.getX();
        int yPos = label.getY();
        xPos++;
        label.setLocation(xPos, yPos);
        shadow.setLocation(xPos+2, yPos+2);
    }
    
    private void fadeIn() {
        
    }
    
    private void buttonTextStepUnindent() {
        int xPos = label.getX();
        int yPos = label.getY();
        xPos--;
        label.setLocation(xPos, yPos);
        shadow.setLocation(xPos+2, yPos+2);
    }

    public class IndentTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (label.getX() < indent + indentSteps) {
                buttonTextStepIndent();
            } else {
                indentTimer.stop();
            }
        }
    }
    
    public class UnindentTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (label.getX() > indent) {
                buttonTextStepUnindent();
            } else {
                unindentTimer.stop();
            }
        }
    }
}
