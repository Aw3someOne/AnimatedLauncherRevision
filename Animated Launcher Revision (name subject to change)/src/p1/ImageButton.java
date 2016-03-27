package p1;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.filechooser.FileSystemView;

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
   
    private int backgroundColorRStep;
    private int backgroundColorGStep;
    private int backgroundColorBStep;
    private int backgroundColorAStep;
    
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
    private int indentCurrent;
    private int indentSteps;
    private int indentDuration;
    private int indentSleep;
    private Timer indentTimer;
    private Timer unindentTimer;
    private Timer fadeInTimer;
    private Timer fadeOutTimer;
    
    private Timer expandTimer;
    private Timer collapseTimer;
    
    private Font font;
    
    private boolean isExpanded = true;
    
    public ImageButton(int category, int buttonNumber) {
        this.category = category;
        this.buttonNumber = buttonNumber;
        readVariables(category, buttonNumber);
        createButton();
        indentTimer = new Timer(indentSleep, new IndentTimerListener());
        unindentTimer = new Timer(indentSleep, new UnindentTimerListener());
        fadeInTimer = new Timer(indentSleep, new FadeInTimerListener());
        fadeOutTimer = new Timer(indentSleep, new FadeOutTimerListener());
        addMouseListener(new ImageButtonMouseAdapter());
    }
    
    public ImageButton(int category) {
        this.category = category;
        readVariables(category);
        createButton();
        indentTimer = new Timer(indentSleep, new IndentTimerListener());
        unindentTimer = new Timer(indentSleep, new UnindentTimerListener());
        fadeInTimer = new Timer(indentSleep, new FadeInTimerListener());
        fadeOutTimer = new Timer(indentSleep, new FadeOutTimerListener());
        expandTimer = new Timer(Main.EXPAND_SLEEP, new ExpandTimerListener());
        collapseTimer = new Timer(Main.EXPAND_SLEEP, new CollapseTimerListener());
//        expandTimer = new Timer(100, new ExpandTimerListener());
//        collapseTimer = new Timer(1, new CollapseTimerListener());
        addMouseListener(new HeaderButtonMouseAdapter());
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D)g;
        gui.setBackground(backgroundColorCurrent);
        gui.clearRect(0, 0, width, height);
    }

    private void createButton() {
        setLayout(new MigLayout("wrap 1, insets 0",
                "[fill," + width + "]",
                "[fill," + height + "]"));
        backgroundColorCurrent = backgroundColorInitial;
        indentCurrent = indent;
        backgroundColorRStep = (backgroundColorRFinal - backgroundColorRInitial) / indentSteps;
        backgroundColorGStep = (backgroundColorGFinal - backgroundColorGInitial) / indentSteps;
        backgroundColorBStep = (backgroundColorBFinal - backgroundColorBInitial) / indentSteps;
        backgroundColorAStep = (backgroundColorAFinal - backgroundColorAInitial) / indentSteps;
        label = new JLabel(text);
        label.setFont(font);
        label.setForeground(fontColorInitial);
        label.setOpaque(false);
        label.setBackground(Main.CLEAR);
        add(label, "id label, pos " + indentCurrent + " 0.5al");
        shadow = new JLabel(text);
        shadow.setFont(font);
        shadow.setForeground(Color.black);
        shadow.setOpaque(false);
        add(shadow, "pos (label.x + 2) (label.y + 2)");
        setPreferredSize(new Dimension(width,height));
        setMaximumSize(new Dimension(width,height));
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
            fadeOutTimer.stop();
            indentTimer.start();
            fadeInTimer.start();
        }
        
        public void mouseExited(MouseEvent e) {
            indentTimer.stop();
            fadeInTimer.stop();
            unindentTimer.start();
            fadeOutTimer.start();
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
                open();
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    
    private class HeaderButtonMouseAdapter extends ImageButtonMouseAdapter {
        public void mouseReleased(MouseEvent e) {
            if (isExpanded) {
                expandTimer.stop();
                collapseTimer.start();
                isExpanded = false;
            } else {
                collapseTimer.stop();
                expandTimer.start();
                isExpanded = true;
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
        Pattern p = Pattern.compile("%(.*)%");
        Matcher m = p.matcher(action);
        if (m.find()) {
            String sysvar = m.group(1);
            sysvar = System.getenv(sysvar);
            sysvar = sysvar.replace("\\", "/");
            action = action.replaceAll("%(.*)%", sysvar);
            System.out.println(action);
        }
        desktop.open(new File(action));
    }
    
    private void buttonTextStepIndent() {
        int xPos = label.getX();
        int yPos = label.getY();
        indentCurrent = xPos + 1;
        label.setLocation(indentCurrent, yPos);
        shadow.setLocation(indentCurrent+2, yPos+2);
    }
    
    private void buttonTextStepUnindent() {
        int xPos = label.getX();
        int yPos = label.getY();
        indentCurrent = xPos - 1;
        label.setLocation(indentCurrent, yPos);
        shadow.setLocation(indentCurrent+2, yPos+2);
    }
    
    private void fadeIn() {
        int backgroundColorRCurrent = Math.min(255, backgroundColorCurrent.getRed() + backgroundColorRStep);
        int backgroundColorGCurrent = Math.min(255, backgroundColorCurrent.getGreen() + backgroundColorGStep);
        int backgroundColorBCurrent = Math.min(255, backgroundColorCurrent.getBlue() + backgroundColorBStep);
        int backgroundColorACurrent = Math.min(255, backgroundColorCurrent.getAlpha() + backgroundColorAStep);
        backgroundColorCurrent = new Color(backgroundColorRCurrent, backgroundColorGCurrent, backgroundColorBCurrent, backgroundColorACurrent);
        System.out.println(backgroundColorCurrent);
    }
    
    private void fadeOut() {
        int backgroundColorRCurrent = Math.max(0, backgroundColorCurrent.getRed() - backgroundColorRStep);
        int backgroundColorGCurrent = Math.max(0, backgroundColorCurrent.getGreen() - backgroundColorGStep);
        int backgroundColorBCurrent = Math.max(0, backgroundColorCurrent.getBlue() - backgroundColorBStep);
        int backgroundColorACurrent = Math.max(0, backgroundColorCurrent.getAlpha() - backgroundColorAStep);
        backgroundColorCurrent = new Color(backgroundColorRCurrent, backgroundColorGCurrent, backgroundColorBCurrent, backgroundColorACurrent);
    }
    
    private void expand() {
//        getParent().getComponent(1).setMaximumSize(new Dimension(500,500));
        int stepAmount = ((Category) getParent()).getMaxHeight() / Main.EXPAND_STEPS;
        getParent().getComponent(1).setMaximumSize(new Dimension(getParent().getComponent(1).getWidth(),
                getParent().getComponent(1).getHeight() + stepAmount));
        getParent().getComponent(1).revalidate();
        getParent().getParent().revalidate();
        getParent().getParent().repaint();
    }
    
    private void collapse() {
//        getParent().getComponent(1).setMaximumSize(new Dimension(500,0));
        int stepAmount = ((Category) getParent()).getMaxHeight() / Main.EXPAND_STEPS;
        System.out.println(stepAmount);
        getParent().getComponent(1).setMaximumSize(new Dimension(getParent().getComponent(1).getWidth(),
                getParent().getComponent(1).getHeight() - stepAmount));
        getParent().getComponent(1).revalidate();
        getParent().getParent().revalidate();
        getParent().getParent().repaint();
    }
    
    private class IndentTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (label.getX() < indent + indentSteps) {
                buttonTextStepIndent();
                repaint();
            } else {
                indentTimer.stop();
            }
        }
    }
    
    private class UnindentTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (label.getX() > indent) {
                buttonTextStepUnindent();
                repaint();
            } else {
                unindentTimer.stop();
            }
        }
    }
    
    private class FadeInTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (backgroundColorCurrent.getRed() < backgroundColorRFinal
                    || backgroundColorCurrent.getGreen() < backgroundColorGFinal
                    || backgroundColorCurrent.getBlue() < backgroundColorBFinal
                    || backgroundColorCurrent.getAlpha() < backgroundColorAFinal) {
                fadeIn();
                repaint();
            } else {
                fadeInTimer.stop();
            }
        }
    }
    
    private class FadeOutTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (backgroundColorCurrent.getRed() > backgroundColorRInitial
                    || backgroundColorCurrent.getGreen() > backgroundColorGInitial
                    || backgroundColorCurrent.getBlue() > backgroundColorBInitial
                    || backgroundColorCurrent.getAlpha() > backgroundColorAInitial) {
                fadeOut();
                repaint();
            } else {
                fadeOutTimer.stop();
            }
        }
    }
    
    private class ExpandTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getParent().getComponent(1).getHeight() < ((Category) getParent()).getMaxHeight()) {
                expand();
            } else {
                expandTimer.stop();
            }
        }
    }
    
    private class CollapseTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getParent().getComponent(1).getHeight() > 0) {
                collapse();
            } else {
                collapseTimer.stop();
            }
        }
    }
    
}
