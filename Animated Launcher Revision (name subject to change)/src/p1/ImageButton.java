package p1;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.ini4j.Ini;

import net.miginfocom.swing.MigLayout;

public class ImageButton extends JPanel {
    
    protected int category;
    private int buttonNumber;
    protected String text;
    private String action;
    protected int width;
    protected int height;
    
    protected int backgroundColorRInitial;
    protected int backgroundColorGInitial;
    protected int backgroundColorBInitial;
    protected int backgroundColorAInitial;
    protected Color backgroundColorInitial;
    
    protected double backgroundColorRCurrent;
    protected double backgroundColorGCurrent;
    protected double backgroundColorBCurrent;
    protected double backgroundColorACurrent;
    protected Color backgroundColorCurrent;
    
    protected int backgroundColorRFinal;
    protected int backgroundColorGFinal;
    protected int backgroundColorBFinal;
    protected int backgroundColorAFinal;
    protected Color backgroundColorFinal;
   
    protected double backgroundColorRStep;
    protected double backgroundColorGStep;
    protected double backgroundColorBStep;
    protected double backgroundColorAStep;

    protected BufferedImage foregroundImage;
    protected int foregroundImageXOffset;
    private int foregroundImageYOffset;
    private int foregroundImageXCrop;
    private int foregroundImageYCrop;
    
    JLabel label;
    JLabel shadow;
    
    protected int fontColorRInitial;
    protected int fontColorGInitial;
    protected int fontColorBInitial;
    protected int fontColorAInitial;
    protected Color fontColorInitial;
    
    protected int fontColorRFinal;
    protected int fontColorGFinal;
    protected int fontColorBFinal;
    protected int fontColorAFinal;
    protected Color fontColorFinal;
    
    protected int indent;
    protected int indentCurrent;
    protected int indentSteps;
    protected int indentDuration;
    protected int indentSleep;
    private Timer indentTimer;
    private Timer unindentTimer;
    private Timer colorStepUpTimer;
    private Timer colorStepDownTimer;
    
    Timer expandTimer;
    Timer collapseTimer;
    
    protected Font font;
    protected int buttonWidth;
    
    
    public ImageButton(int category, int buttonNumber) throws IOException {
        this.category = category;
        this.buttonNumber = buttonNumber;
        readVariables(category, buttonNumber);
        createButton();
        indentTimer = new Timer(indentSleep, new IndentTimerListener());
        unindentTimer = new Timer(indentSleep, new UnindentTimerListener());
        colorStepUpTimer = new Timer(indentSleep, new ColorStepUpTimerListener());
        colorStepDownTimer = new Timer(indentSleep, new ColorStepDownTimerListener());
        if (buttonNumber != -1) {
            addMouseListener(new ImageButtonMouseAdapter());
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D)g;
        gui.setBackground(Main.CLEAR);
        gui.setColor(backgroundColorCurrent);
        gui.clearRect(0, 0, width, height);
        gui.fillRect(0, 0, buttonWidth, height);
        
//Deprecated code that allows out of range values without breaking, but it runs like crap
        
//        g.drawImage(foregroundImage, 0, 0, this);
//        gui.drawImage(foregroundImage,
//                foregroundImageXOffset, foregroundImageYOffset, width + foregroundImageXOffset, height + foregroundImageYOffset,
//                foregroundImageXCrop,foregroundImageYCrop + buttonNumber * height, width, (buttonNumber + 1) * height, null);
    }
    
//    public void paint(Graphics g) {
//        super.paint(g);
//        g.drawImage(foregroundImage, foregroundImageXOffset, 0, this);
//    }

    protected void createButton() {
        setLayout(new MigLayout("wrap 1, insets 0",
                "[fill," + width + "]",
                "[fill," + height + "]"));
        backgroundColorCurrent = backgroundColorInitial;
        backgroundColorRCurrent = backgroundColorRInitial;
        backgroundColorGCurrent = backgroundColorGInitial;
        backgroundColorBCurrent = backgroundColorBInitial;
        backgroundColorACurrent = backgroundColorAInitial;
        indentCurrent = indent;
        backgroundColorRStep = (double) (backgroundColorRFinal - backgroundColorRInitial) / indentSteps;
        backgroundColorGStep = (double) (backgroundColorGFinal - backgroundColorGInitial) / indentSteps;
        backgroundColorBStep = (double) (backgroundColorBFinal - backgroundColorBInitial) / indentSteps;
        backgroundColorAStep = (double) (backgroundColorAFinal - backgroundColorAInitial) / indentSteps;
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
        try {
            ImageIcon icon = new ImageIcon(foregroundImage);
            JLabel iconLabel = new JLabel(icon);
            add(iconLabel, "pos " + foregroundImageXOffset + " 0");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//      Makes the panels slide instead of accordion effect
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width,height));
        setMaximumSize(new Dimension(width,height));
    }
    
    private void readVariables(int category, int buttonNumber) throws IOException {
        Ini.Section section = Main.CONFIG.get("Category" + category);
        buttonWidth = Main.BUTTON_WIDTH;
        width = Math.max(Main.BUTTON_WIDTH, Main.IMAGE_BOUND);
        height = Main.BUTTON_HEIGHT;
        indent = Main.TEXT_INDENT;
        indentSteps = Main.TEXT_INDENT_STEPS;
        indentDuration = Main.TEXT_INDENT_DURATION;
        indentSleep = Main.TEXT_INDENT_SLEEP;
        
        String buttonType;
        if (buttonNumber == -1) {
            buttonType = "header";
            buttonWidth = Main.HEADER_WIDTH;
            width = Main.HEADER_WIDTH;
            height = Main.HEADER_HEIGHT;
            indent = Main.HEADER_TEXT_INDENT;
            indentSteps = Main.TEXT_INDENT_STEPS;
            indentDuration = Main.TEXT_INDENT_DURATION;
            indentSleep = Main.TEXT_INDENT_SLEEP;
        } else {
            buttonType = "button";
            action = section.get("button" + buttonNumber +"Action");
            foregroundImage = Main.THEME.getImage(category);
            foregroundImageXOffset = Main.THEME.getValue("Category" + category + "ForegroundImageXOffset");
            foregroundImageYOffset = Integer.parseInt(section.get("ForegroundImageYOffset"));
            foregroundImageXCrop = Integer.parseInt(section.get("ForegroundImageXCrop"));
            foregroundImageYCrop = Integer.parseInt(section.get("ForegroundImageYCrop"));
            foregroundImage = foregroundImage.getSubimage(foregroundImageXCrop, foregroundImageYCrop + (height * buttonNumber), Math.min(width, foregroundImage.getWidth()), Math.min(height, foregroundImage.getHeight()));
        }
        
        text = section.get(buttonType + ((buttonNumber == -1) ? "" : buttonNumber) + "Text");
        
        font = Main.THEME.getFont(category, buttonType);
        backgroundColorInitial = Main.THEME.getColor(category, buttonType, "BackgroundColor_i");
        backgroundColorRInitial = backgroundColorInitial.getRed();
        backgroundColorGInitial = backgroundColorInitial.getGreen();
        backgroundColorBInitial = backgroundColorInitial.getBlue();
        backgroundColorAInitial = backgroundColorInitial.getAlpha();
        
        backgroundColorFinal = Main.THEME.getColor(category, buttonType, "BackgroundColor_f");
        backgroundColorRFinal = backgroundColorFinal.getRed();   
        backgroundColorGFinal = backgroundColorFinal.getGreen(); 
        backgroundColorBFinal = backgroundColorFinal.getBlue();  
        backgroundColorAFinal = backgroundColorFinal.getAlpha(); 
        
        fontColorInitial = Main.THEME.getColor(category, buttonType, "FontColor_i");
        fontColorRInitial = fontColorInitial.getRed();   
        fontColorGInitial = fontColorInitial.getGreen(); 
        fontColorBInitial = fontColorInitial.getBlue();  
        fontColorAInitial = fontColorInitial.getAlpha(); 

        fontColorFinal = Main.THEME.getColor(category, buttonType, "FontColor_f");
        fontColorRFinal = fontColorFinal.getRed();   
        fontColorGFinal = fontColorFinal.getGreen(); 
        fontColorBFinal = fontColorFinal.getBlue();  
        fontColorAFinal = fontColorFinal.getAlpha();
    }
    
    private class BaseButtonMouseAdapter extends MouseAdapter {
        
        @Override
        public void mouseEntered(MouseEvent e) {
            unindentTimer.stop();
            colorStepDownTimer.stop();
            indentTimer.start();
            colorStepUpTimer.start();
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            indentTimer.stop();
            colorStepUpTimer.stop();
            unindentTimer.start();
            colorStepDownTimer.start();
        }
    }
    
    protected class ImageButtonMouseAdapter extends BaseButtonMouseAdapter {
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
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
//        int yPos = label.getY();
        indentCurrent = xPos + 1;
        add(label, "id label, pos " + indentCurrent + " 0.5al");
        add(shadow, "pos (label.x + 2) (label.y + 2)");
//        label.setLocation(indentCurrent, yPos);
//        shadow.setLocation(indentCurrent+2, yPos+2);
        revalidate();
//        repaint();
    }
    
    private void buttonTextStepUnindent() {
        int xPos = label.getX();
//        int yPos = label.getY();
        indentCurrent = xPos - 1;
        add(label, "id label, pos " + indentCurrent + " 0.5al");
        add(shadow, "pos (label.x + 2) (label.y + 2)");
//        label.setLocation(indentCurrent, yPos);
//        shadow.setLocation(indentCurrent+2, yPos+2);
        revalidate();
//        repaint();
    }
    
    private void redStepUp() {
        backgroundColorRCurrent = forceRGB(backgroundColorRCurrent + backgroundColorRStep);
    }
    
    private void redStepDown() {
        backgroundColorRCurrent = forceRGB(backgroundColorRCurrent - backgroundColorRStep);
    }
    
    private void greenStepUp() {
        backgroundColorGCurrent = forceRGB(backgroundColorGCurrent + backgroundColorGStep);
    }
    
    private void greenStepDown() {
        backgroundColorGCurrent = forceRGB(backgroundColorGCurrent - backgroundColorGStep);
    }
    
    private void blueStepUp() {
        backgroundColorBCurrent = forceRGB(backgroundColorBCurrent + backgroundColorBStep);
    }
    
    private void blueStepDown() {
        backgroundColorBCurrent = forceRGB(backgroundColorBCurrent - backgroundColorBStep);
    }
    
    private void alphaStepUp() {
        backgroundColorACurrent = forceRGB(backgroundColorACurrent + backgroundColorAStep);
    }
    
    private void alphaStepDown() {
        backgroundColorACurrent = forceRGB(backgroundColorACurrent - backgroundColorAStep);
    }
    
    private class IndentTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (label.getX() < indent + indentSteps) {
                buttonTextStepIndent();
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
            } else {
                unindentTimer.stop();
            }
        }
    }
    
    private class ColorStepUpTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!backgroundColorCurrent.equals(backgroundColorFinal)) {
                if (backgroundColorRStep > 0 && backgroundColorRCurrent < backgroundColorRFinal) {
                    redStepUp();
                } else if (backgroundColorRStep < 0 && backgroundColorRCurrent > backgroundColorRFinal) {
                    redStepUp();
                }
                if (backgroundColorGStep > 0 && backgroundColorGCurrent < backgroundColorGFinal) {
                    greenStepUp();
                } else if (backgroundColorGStep < 0 && backgroundColorGCurrent > backgroundColorGFinal) {
                    greenStepUp();
                }
                if (backgroundColorBStep > 0 && backgroundColorBCurrent < backgroundColorBFinal) {
                    blueStepUp();
                } else if (backgroundColorBStep < 0 && backgroundColorBCurrent > backgroundColorBFinal) {
                    blueStepUp();
                }
                if (backgroundColorAStep > 0 && backgroundColorACurrent < backgroundColorAFinal) {
                    alphaStepUp();
                } else if (backgroundColorAStep < 0 && backgroundColorACurrent > backgroundColorAFinal) {
                    alphaStepUp();
                }
                backgroundColorCurrent = new Color(
                        (int) Math.round(backgroundColorRCurrent),
                        (int) Math.round(backgroundColorGCurrent),
                        (int) Math.round(backgroundColorBCurrent),
                        (int) Math.round(backgroundColorACurrent));
                repaint();
            } else {
                colorStepUpTimer.stop();
            }
        }
    }
    
    private class ColorStepDownTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!backgroundColorCurrent.equals(backgroundColorInitial)) {
                if (backgroundColorRStep > 0 && backgroundColorRCurrent > backgroundColorRInitial) {
                    redStepDown();
                } else if (backgroundColorRStep < 0 && backgroundColorRCurrent < backgroundColorRInitial) {
                    redStepDown();
                }
                if (backgroundColorGStep > 0 && backgroundColorGCurrent > backgroundColorGInitial) {
                    greenStepDown();
                } else if (backgroundColorGStep < 0 && backgroundColorGCurrent < backgroundColorGInitial) {
                    greenStepDown();
                }
                if (backgroundColorBStep > 0 && backgroundColorBCurrent > backgroundColorBInitial) {
                    blueStepDown();
                } else if (backgroundColorBStep < 0 && backgroundColorBCurrent < backgroundColorBInitial) {
                    blueStepDown();
                }
                if (backgroundColorAStep > 0 && backgroundColorACurrent > backgroundColorAInitial) {
                    alphaStepDown();
                } else if (backgroundColorAStep < 0 && backgroundColorACurrent < backgroundColorAInitial) {
                    alphaStepDown();
                }
                backgroundColorCurrent = new Color(
                        (int) Math.round(backgroundColorRCurrent),
                        (int) Math.round(backgroundColorGCurrent),
                        (int) Math.round(backgroundColorBCurrent),
                        (int) Math.round(backgroundColorACurrent));
                repaint();
            } else {
                colorStepDownTimer.stop();
            }
        }
    }
    
    private int forceRGB(int i) {
        return Math.min(255, Math.max(0, i));
    }
    
    private double forceRGB(double i) {
        return Math.min(255.0, Math.max(0.0, i));
    }
    
}
