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

/**
 * <p>ImageButton.</p>
 * @author Stephen Cheng
 * @version 1.0
 */
public class ImageButton extends JPanel {
    
    /**
     * <p>categoryNumber</p>
     * Category Number.
     */
    protected int categoryNumber;
    /**
     * <p>buttonNumber</p>
     * Button Number.
     */
    protected int buttonNumber;
    /**
     * <p>text</p>
     * Text that is displayed on the button.
     */
    protected String text;
    /**
     * <p>action</p>
     * Action that is performed when the button is clicked.
     */
    protected String action;
    /**
     * <p>width</p>
     * Button width.
     */
    protected int width;
    /**
     * <p>height</p>
     * Button height.
     */
    protected int height;
    
    protected int backgroundColorRInitial;
    protected int backgroundColorGInitial;
    protected int backgroundColorBInitial;
    protected int backgroundColorAInitial;
    /**
     * <p>backgroundColorInitial</p>
     * Initial background color.
     */
    protected Color backgroundColorInitial;
    
    protected double backgroundColorRCurrent;
    protected double backgroundColorGCurrent;
    protected double backgroundColorBCurrent;
    protected double backgroundColorACurrent;
    /**
     * <p>backgroundColorCurrent</p>
     * Current background color.
     */
    protected Color backgroundColorCurrent;
    
    protected int backgroundColorRFinal;
    protected int backgroundColorGFinal;
    protected int backgroundColorBFinal;
    protected int backgroundColorAFinal;
    /**
     * <p>backgroundColorFinal</p>
     * Final background Color.
     */
    protected Color backgroundColorFinal;
   
    /**
     * <p>backgroundColorRStep</p>
     * Amount that the background color (Red) changes when mouseover.
     */
    protected double backgroundColorRStep;
    /**
     * <p>backgroundColorGStep</p>
     * Amount that the background color (Green) changes when mouseover.
     */
    protected double backgroundColorGStep;
    /**
     * <p>backgroundColorBStep</p>
     * Amount that the background color (Blue) changes when mouseover.
     */
    protected double backgroundColorBStep;
    /**
     * <p>backgroundColorAStep</p>
     * Amount that the background color (Alpha) changes when mouseover.
     */
    protected double backgroundColorAStep;

    /**
     * <p>foregroundImage</p>
     * Image that is displayed over the background color.
     */
    protected BufferedImage foregroundImage;
    /**
     * <p>foregroundImageXOffset</p>
     * Distance from left edge of button to display image.
     */
    protected int foregroundImageXOffset;
    protected int foregroundImageYOffset;
    protected int foregroundImageXCrop;
    protected int foregroundImageYCrop;
    
    protected JLabel label;
    protected JLabel shadow;
    
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
    protected Timer indentTimer;
    protected Timer unindentTimer;
    protected Timer colorStepUpTimer;
    protected Timer colorStepDownTimer;
    
    protected Timer expandTimer;
    protected Timer collapseTimer;
    
    protected Font font;
    protected int buttonWidth;
    
    /**
     * <p>ImageButton</p>
     * Constructor.
     * @param categoryNumber category number
     * @param buttonNumber button number
     * @throws IOException e
     */
    public ImageButton(int categoryNumber, int buttonNumber) throws IOException {
        this.categoryNumber = categoryNumber;
        this.buttonNumber = buttonNumber;
        readVariables(categoryNumber, buttonNumber);
        createButton();
        indentTimer = new Timer(indentSleep, new IndentTimerListener());
        unindentTimer = new Timer(indentSleep, new UnindentTimerListener());
        colorStepUpTimer = new Timer(indentSleep, new ColorStepUpTimerListener());
        colorStepDownTimer = new Timer(indentSleep, new ColorStepDownTimerListener());
        if (buttonNumber != -1) {
            addMouseListener(new ImageButtonMouseAdapter());
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D) g;
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

    /**
     * <p>createButton</p>
     * Creates the button.
     */
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
    
    /**
     * <p>readVariables</p>
     * Reads the config files and stores variables.
     * @param categoryNumber category number
     * @param buttonNumber button number
     * @throws IOException e
     */
    private void readVariables(int categoryNumber, int buttonNumber) throws IOException {
        Ini.Section section = Main.CONFIG.get("Category" + categoryNumber);
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
            action = section.get("button" + buttonNumber + "Action");
            foregroundImage = Main.THEME.getImage(categoryNumber);
            foregroundImageXOffset = Main.THEME.getValue("Category" + categoryNumber + "ForegroundImageXOffset");
            foregroundImageYOffset = Integer.parseInt(section.get("ForegroundImageYOffset"));
            foregroundImageXCrop = Integer.parseInt(section.get("ForegroundImageXCrop"));
            foregroundImageYCrop = Integer.parseInt(section.get("ForegroundImageYCrop"));
            foregroundImage = foregroundImage.getSubimage(foregroundImageXCrop, foregroundImageYCrop + (height * buttonNumber), Math.min(width, foregroundImage.getWidth()), Math.min(height, foregroundImage.getHeight()));
        }
        
        text = section.get(buttonType + ((buttonNumber == -1) ? "" : buttonNumber) + "Text");
        
        font = Main.THEME.getFont(categoryNumber, buttonType);
        backgroundColorInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_i");
        backgroundColorRInitial = backgroundColorInitial.getRed();
        backgroundColorGInitial = backgroundColorInitial.getGreen();
        backgroundColorBInitial = backgroundColorInitial.getBlue();
        backgroundColorAInitial = backgroundColorInitial.getAlpha();
        
        backgroundColorFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_f");
        backgroundColorRFinal = backgroundColorFinal.getRed();   
        backgroundColorGFinal = backgroundColorFinal.getGreen(); 
        backgroundColorBFinal = backgroundColorFinal.getBlue();  
        backgroundColorAFinal = backgroundColorFinal.getAlpha(); 
        
        fontColorInitial = Main.THEME.getColor(categoryNumber, buttonType, "FontColor_i");
        fontColorRInitial = fontColorInitial.getRed();   
        fontColorGInitial = fontColorInitial.getGreen(); 
        fontColorBInitial = fontColorInitial.getBlue();  
        fontColorAInitial = fontColorInitial.getAlpha(); 

        fontColorFinal = Main.THEME.getColor(categoryNumber, buttonType, "FontColor_f");
        fontColorRFinal = fontColorFinal.getRed();   
        fontColorGFinal = fontColorFinal.getGreen(); 
        fontColorBFinal = fontColorFinal.getBlue();  
        fontColorAFinal = fontColorFinal.getAlpha();
    }
    
    /**
     * <p>BaseButtonMouseAdapter.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
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
    
    /**
     * <p>ImageButtonMouseAdapter.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
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
   
    /**
     * <p>run</p>
     * Executes the action as a program.
     * @throws IOException e
     */
    private void run() throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process process = rt.exec(action);
    }
    
    /**
     * <p>uri</p>
     * Opens the action as a URI.
     * @throws URISyntaxException e
     * @throws IOException e
     */
    private void uri() throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        URI uri = new URI(action);
        desktop.browse(uri);
    }
    
    /**
     * <p>open</p>
     * Opens the action as a folder.
     * @throws IOException e
     */
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
    
    /**
     * <p>buttonTextStepIndent</p>
     * Indents the text by 1 step.
     */
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
    
    /**
     * <p>buttonTextStepUnindent</p>
     * Unindents the text by 1 step.
     */
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
    
    /**
     * <p>redStepUp</p>
     * Increases red value by 1 step.
     */
    private void redStepUp() {
        backgroundColorRCurrent = forceRGB(backgroundColorRCurrent + backgroundColorRStep);
    }
    
    /**
     * <p>redStepDown</p>
     * Decreases red value by 1 step.
     */
    private void redStepDown() {
        backgroundColorRCurrent = forceRGB(backgroundColorRCurrent - backgroundColorRStep);
    }
    
    /**
     * <p>greenStepUp</p>
     * Increases green value by 1 step.
     */
    private void greenStepUp() {
        backgroundColorGCurrent = forceRGB(backgroundColorGCurrent + backgroundColorGStep);
    }
    
    /**
     * <p>greenStepDown</p>
     * Decreases green value by 1 step.
     */
    private void greenStepDown() {
        backgroundColorGCurrent = forceRGB(backgroundColorGCurrent - backgroundColorGStep);
    }
    
    /**
     * <p>blueStepUp</p>
     * Increases blue value by 1 step.
     */
    private void blueStepUp() {
        backgroundColorBCurrent = forceRGB(backgroundColorBCurrent + backgroundColorBStep);
    }
    
    /**
     * <p>blueStepDown</p>
     * Decreases blue value by 1 step.
     */
    private void blueStepDown() {
        backgroundColorBCurrent = forceRGB(backgroundColorBCurrent - backgroundColorBStep);
    }
    
    /**
     * <p>alphaStepUp</p>
     * Increases alpha value by 1 step.
     */
    private void alphaStepUp() {
        backgroundColorACurrent = forceRGB(backgroundColorACurrent + backgroundColorAStep);
    }
    
    /**
     * <p>alphaStepDown</p>
     * Decreases alpha value by 1 step.
     */
    private void alphaStepDown() {
        backgroundColorACurrent = forceRGB(backgroundColorACurrent - backgroundColorAStep);
    }
    
    /**
     * <p>IndentTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
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
    
    /**
     * <p>UnindentTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
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
    
    /**
     * <p>ColorStepUpTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
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
    
    /**
     * <p>ColorStepDownTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
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
    
    /**
     * <p>forceRGB</p>
     * Forces a value into range [0, 255].
     * @param i value
     * @return int
     */
    private int forceRGB(int i) {
        return Math.min(255, Math.max(0, i));
    }
    
    /**
     * <p>forceRGB</p>
     * Forces a value into range [0.0, 255.0].
     * @param i value
     * @return double
     */
    private double forceRGB(double i) {
        return Math.min(255.0, Math.max(0.0, i));
    }
    
}
