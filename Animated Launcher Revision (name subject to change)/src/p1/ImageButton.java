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
     * <p>serialVersionUID.</p>
     */
    private static final long serialVersionUID = 6716927459100501280L;
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
    /**
     * <p>backgroundColorInitial</p>
     * Initial background color.
     */
    protected Color backgroundColorInitial;
    /**
     * <p>backgroundColorCurrent</p>
     * Current background color.
     */
    protected Color backgroundColorCurrent;
    /**
     * <p>backgroundColorFinal</p>
     * Final background Color.
     */
    protected Color backgroundColorFinal;
    /**
     * <p>backgroundColors</p>
     * Stores the background color steps.
     */
    protected Color[] backgroundColors;
    /**
     * <p>backgroundColorIndex</p>
     * Uses to step through the background color array.
     */
    protected int backgroundColorIndex;
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
    
    protected Color fontColorInitial;
    protected Color fontColorFinal;
    protected Color[] fontColors;
    
    protected int indent;
    protected int indentCurrent;
    protected int textIndentSteps;
    protected int textIndentDuration;
    protected int textIndentSleep;
    protected int textIndentIndex;
    protected int[] textIndents;
    protected Timer indentTimer;
    protected Timer unindentTimer;
    protected Timer backgroundColorStepUpTimer;
    protected Timer backgroundColorStepDownTimer;
    
    protected Timer expandTimer;
    protected Timer collapseTimer;
    
    protected Font font;
    protected int buttonWidth;
    protected int imageBound;
    protected int backgroundColorFadeSteps;
    protected int backgroundColorFadeDuration;
    protected int backgroundColorFadeSleep;
    protected int textIndentInitial;
    protected int textIndentFinal;
    private double textIndentStep;
    private String buttonType;
    protected int fontColorIndex;
    private int fontColorFadeSteps;
    private int fontColorFadeDuration;
    private int fontColorFadeSleep;
    private Timer fontColorStepUpTimer;
    private Timer fontColorStepDownTimer;
    
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
        if (buttonNumber == -1) {
            buttonType = "header";
        } else {
            buttonType = "button";
        }
        readVariables();
        createButton();
        indentTimer = new Timer(textIndentSleep, new IndentTimerListener());
        unindentTimer = new Timer(textIndentSleep, new UnindentTimerListener());
        backgroundColorStepUpTimer = new Timer(backgroundColorFadeSleep, new BackgroundColorStepUpTimerListener());
        backgroundColorStepDownTimer = new Timer(backgroundColorFadeSleep, new BackgroundColorStepDownTimerListener());
        fontColorStepUpTimer = new Timer(fontColorFadeSleep, new FontColorStepUpTimerListener());
        fontColorStepDownTimer = new Timer(fontColorFadeSleep, new FontColorStepDownTimerListener());
        if (buttonNumber != -1) {
            addMouseListener(new ImageButtonMouseAdapter());
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D) g;
        gui.setBackground(Main.CLEAR);
        gui.setColor(backgroundColors[backgroundColorIndex]);
        gui.clearRect(0, 0, width, height);
        gui.fillRect(0, 0, buttonWidth, height);
        gui.drawImage(foregroundImage, foregroundImageXOffset, 0, this);
    }
    
    /**
     * <p>createButton</p>
     * Creates the button.
     */
    protected void createButton() {
        setLayout(new MigLayout("wrap 1, insets 0",
                "[fill," + width + "]",
                "[fill," + height + "]"));
        backgroundColorCurrent = backgroundColorInitial;
        indentCurrent = indent;
        label = new JLabel(text);
        label.setFont(font);
        label.setForeground(fontColorInitial);
        label.setOpaque(false);
        label.setBackground(Main.CLEAR);
        add(label, "id label, pos " + textIndents[textIndentIndex] + " 0.5al");
        shadow = new JLabel(text);
        shadow.setFont(font);
        shadow.setForeground(Color.black);
        shadow.setOpaque(false);
        add(shadow, "pos (label.x + 2) (label.y + 2)");
//      Makes the panels slide instead of accordion effect
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
    }
    
    /**
     * <p>readVariables</p>
     * Reads the config files and stores variables.
     * @throws IOException e
     */
    private void readVariables() throws IOException {
        Ini.Section section = Main.CONFIG.get("Category" + categoryNumber);
        buttonWidth = Main.THEME.getValue(buttonType + "Width");
        imageBound = Main.THEME.getValue("imageBound");
        width = Math.max(buttonWidth, imageBound);
        height = Main.THEME.getValue(buttonType + "Height");
        textIndentInitial = Main.THEME.getValue(buttonType + "TextIndent_i");
        textIndentFinal = Main.THEME.getValue(buttonType + "TextIndent_f");
        textIndentSteps = Main.THEME.getValue(buttonType + "TextIndentSteps");
        textIndentStep = (double) (textIndentFinal - textIndentInitial) / textIndentSteps;
        textIndentDuration = Main.THEME.getValue(buttonType + "TextIndentDuration");
        textIndentSleep = textIndentDuration / textIndentSteps;
        backgroundColorFadeSteps = Main.THEME.getValue(buttonType + "BackgroundColorFadeSteps");
        backgroundColorFadeDuration = Main.THEME.getValue(buttonType + "BackgroundColorFadeDuration");
        if (backgroundColorFadeSteps > 0) {
            backgroundColorFadeSleep = backgroundColorFadeDuration / backgroundColorFadeSteps;
        }
        fontColorFadeSteps = Main.THEME.getValue(buttonType + "FontColorFadeSteps");
        fontColorFadeDuration = Main.THEME.getValue(buttonType + "FontColorFadeDuration");
        if (fontColorFadeSteps > 0) {
            fontColorFadeSleep = fontColorFadeDuration / fontColorFadeSteps;
        }
        if (buttonType.equals("button")) {
            action = section.get("button" + buttonNumber + "Action");
            foregroundImage = Main.THEME.getImage(categoryNumber);
            foregroundImageXOffset = Main.THEME.getValue("Category" + categoryNumber + "ForegroundImageXOffset");
            foregroundImageYOffset = Integer.parseInt(section.get("ForegroundImageYOffset"));
            foregroundImageXCrop = Integer.parseInt(section.get("ForegroundImageXCrop"));
            foregroundImageYCrop = Integer.parseInt(section.get("ForegroundImageYCrop"));
            try {
                foregroundImage = foregroundImage.getSubimage(foregroundImageXCrop, foregroundImageYCrop + (height * buttonNumber), Math.min(width, foregroundImage.getWidth()), Math.min(height, foregroundImage.getHeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        text = section.get(buttonType + ((buttonNumber == -1) ? "" : buttonNumber) + "Text");
        font = Main.THEME.getFont(categoryNumber, buttonType);
        backgroundColorInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_i");
        backgroundColorFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_f");
        
        backgroundColorRStep = (double) (backgroundColorFinal.getRed() - backgroundColorInitial.getRed()) / backgroundColorFadeSteps;
        backgroundColorGStep = (double) (backgroundColorFinal.getGreen() - backgroundColorInitial.getGreen()) / backgroundColorFadeSteps;
        backgroundColorBStep = (double) (backgroundColorFinal.getBlue() - backgroundColorInitial.getBlue()) / backgroundColorFadeSteps;
        backgroundColorAStep = (double) (backgroundColorFinal.getAlpha() - backgroundColorInitial.getAlpha()) / backgroundColorFadeSteps;
        backgroundColors = new Color[backgroundColorFadeSteps + 1];
        backgroundColors[0] = backgroundColorInitial;
        for (int i = 1; i < backgroundColors.length - 1; i++) {
            backgroundColors[i] = new Color((int) (backgroundColorInitial.getRed() + i * backgroundColorRStep),
                    (int) (backgroundColorInitial.getGreen() + i * backgroundColorGStep),
                    (int) (backgroundColorInitial.getBlue() + i * backgroundColorBStep),
                    (int) (backgroundColorInitial.getAlpha() + i * backgroundColorAStep));
        }
        backgroundColors[backgroundColors.length - 1] = backgroundColorFinal;
        fontColorInitial = Main.THEME.getColor(categoryNumber, buttonType, "FontColor_i");
        fontColorFinal = Main.THEME.getColor(categoryNumber, buttonType, "FontColor_f");
        double fontColorRStep = (double) (fontColorFinal.getRed() - fontColorInitial.getRed()) / backgroundColorFadeSteps;
        double fontColorGStep = (double) (fontColorFinal.getGreen() - fontColorInitial.getGreen()) / backgroundColorFadeSteps;
        double fontColorBStep = (double) (fontColorFinal.getBlue() - fontColorInitial.getBlue()) / backgroundColorFadeSteps;
        double fontColorAStep = (double) (fontColorFinal.getAlpha() - fontColorInitial.getAlpha()) / backgroundColorFadeSteps;
        fontColors = new Color[fontColorFadeSteps + 1];
        fontColors[0] = fontColorInitial;
        for (int i = 1; i < fontColors.length - 1; i++) {
            fontColors[i] = new Color((int) (fontColorInitial.getRed() + i * fontColorRStep),
                    (int) (fontColorInitial.getGreen() + i * fontColorGStep),
                    (int) (fontColorInitial.getBlue() + i * fontColorBStep),
                    (int) (fontColorInitial.getAlpha() + i * fontColorAStep));
        }
        fontColors[fontColors.length - 1] = fontColorFinal;
        textIndents = new int[textIndentSteps + 1];
        textIndents[0] = textIndentInitial;
        for (int i = 1; i < textIndents.length - 1; i++) {
            textIndents[i] = (int) (textIndentInitial + i * textIndentStep);
        }
        textIndents[textIndents.length - 1] = textIndentFinal;
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
            indentTimer.start();
            backgroundColorStepDownTimer.stop();
            backgroundColorStepUpTimer.start();
            fontColorStepDownTimer.stop();
            fontColorStepUpTimer.start();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            indentTimer.stop();
            unindentTimer.start();
            backgroundColorStepUpTimer.stop();
            backgroundColorStepDownTimer.start();
            fontColorStepUpTimer.stop();
            fontColorStepDownTimer.start();
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
    
    private void repositionText() {
        add(label, "id label, pos " + textIndents[textIndentIndex] + " 0.5al");
        add(shadow, "pos (label.x + 2) (label.y + 2)");
        revalidate();
        repaint();
    }
    
    private void recolorText() {
        label.setForeground(fontColors[fontColorIndex]);
        repaint();
    }
    
    /**
     * <p>IndentTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    private class IndentTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (textIndentIndex != textIndents.length - 1) {
                textIndentIndex++;
                repositionText();
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
            if (textIndentIndex != 0) {
                textIndentIndex--;
                repositionText();
            } else {
                unindentTimer.stop();
            }
        }
    }
    
    /**
     * <p>BackgroundColorStepUpTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    private class BackgroundColorStepUpTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (backgroundColorIndex != backgroundColors.length - 1) {
                backgroundColorIndex++;
                repaint();
            } else {
                backgroundColorStepUpTimer.stop();
            }
        }
    }
    
    /**
     * <p>BackgroundColorStepDownTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    private class BackgroundColorStepDownTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (backgroundColorIndex != 0) {
                backgroundColorIndex--;
                repaint();
            } else {
                backgroundColorStepDownTimer.stop();
            }
        }
    }
    
    private class FontColorStepUpTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fontColorIndex != fontColors.length - 1) {
                fontColorIndex++;
                recolorText();
            } else {
                fontColorStepUpTimer.stop();
            }
        }
    }
    
    private class FontColorStepDownTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fontColorIndex != 0) {
                fontColorIndex--;
                recolorText();
            } else {
                fontColorStepDownTimer.stop();
            }
        }
    }
    
    /**
     * <p>forceRGB</p>
     * Forces a value into interval [0, 255].
     * @param i value
     * @return int
     */
    private int forceRGB(int i) {
        return Math.min(255, Math.max(0, i));
    }
    
    /**
     * <p>forceRGB</p>
     * Forces a value into interval [0.0, 255.0].
     * @param i value
     * @return double
     */
    private double forceRGB(double i) {
        return Math.min(255.0, Math.max(0.0, i));
    }
    
}
