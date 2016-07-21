package p1;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.ini4j.Ini;

import net.miginfocom.swing.MigLayout;
import p1.Theme.BackgroundColorMode;

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
    private int buttonNumber;
    /**
     * <p>text</p>
     * Text that is displayed on the button.
     */
    private String text;
    /**
     * <p>action</p>
     * Action that is performed when the button is clicked.
     */
    private String action;
    /**
     * <p>width</p>
     * Button width.
     */
    private int width;
    /**
     * <p>height</p>
     * Button height.
     */
    protected int height;
    /**
     * <p>backgroundColors</p>
     * Stores the background color steps.
     */
    private Color[] backgroundColors;
    /**
     * <p>backgroundColors_2</p>
     * Stores the background color steps
     */
    private Color[] backgroundColors_2;
    /**
     * <p>backgroundColorImages</p>
     */
    private BufferedImage[] backgroundColorImages;
    /**
     * <p>backgroundColorIndex</p>
     * Uses to step through the background color array.
     */
    private int backgroundColorIndex;
    /**
     * <p>foregroundImage</p>
     * Image that is displayed over the background color.
     */
    private BufferedImage foregroundImage;
    private JLabel label;
    private JLabel shadow;
    
    private Color fontColorInitial;
    private Color fontColorFinal;
    private Color[] fontColors;
    
    private int indent;
    private int textIndentSteps;
    private int textIndentDuration;
    private int textIndentSleep;
    private int textIndentIndex;
    private int[] textIndents;
    private Timer indentTimer;
    private Timer unindentTimer;
    private Timer backgroundColorStepUpTimer;
    private Timer backgroundColorStepDownTimer;
    
    protected Timer expandTimer;
    protected Timer collapseTimer;
    
    private Font font;
    protected int buttonWidth;
    private int imageBound;
    private int backgroundColorFadeSteps;
    private int backgroundColorFadeDuration;
    private int backgroundColorFadeSleep;
    private int textIndentInitial;
    private int textIndentFinal;
    private String buttonType;
    private int fontColorIndex;
    private int fontColorFadeSteps;
    private int fontColorFadeDuration;
    private int fontColorFadeSleep;
    private Timer fontColorStepUpTimer;
    private Timer fontColorStepDownTimer;
    private Category category;
    protected Rectangle buttonRect;
    private Color[] upperLeft;
    private Color[] lowerLeft;
    private Color[] upperRight;
    private Color[] lowerRight;
    
    /**
     * <p>ImageButton</p>
     * Constructor.
     * @param categoryNumber category number
     * @param buttonNumber button number
     * @throws IOException e
     */
    public ImageButton(int categoryNumber, int buttonNumber, Category category) throws IOException {
        this.categoryNumber = categoryNumber;
        this.buttonNumber = buttonNumber;
        this.category = category;
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
        if (buttonType.equals("button")) {
            ImageButtonMouseAdapter adapter = new ImageButtonMouseAdapter();
            addMouseListener(adapter);
            addMouseMotionListener(adapter);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D) g;
        gui.setBackground(Main.CLEAR);
        gui.clearRect(0, 0, width, height);
        int backgroundColorMode = Main.THEME.getValue("backgroundColorMode");
        GradientPaint gp = null;
            switch (BackgroundColorMode.values()[backgroundColorMode]) {
            case SOLID:
                gui.setColor(backgroundColors[backgroundColorIndex]);
                gui.fillRect(0, 0, buttonWidth, height);
                break;
            case VERTICAL_GRADIENT:
                gp = new GradientPaint(0, 0, backgroundColors[backgroundColorIndex], 0, height, backgroundColors_2[backgroundColorIndex]);
                gui.setPaint(gp);
                gui.fillRect(0, 0, buttonWidth, height);
                break;
            case HORIZONTAL_GRADIENT:
                gp = new GradientPaint(0, 0, backgroundColors[backgroundColorIndex], buttonWidth, 0, backgroundColors_2[backgroundColorIndex]);
                gui.setPaint(gp);
                gui.fillRect(0, 0, buttonWidth, height);
                break;
            case HORIZONTAL_BANDS:
                gui.setColor(backgroundColors[backgroundColorIndex]);
                gui.fillRect(0, 0, buttonWidth, height);
                break;
            case HORIZONTAL_BANDED_GRADIENT:
                gp = new GradientPaint(0, 0, backgroundColors[backgroundColorIndex], buttonWidth, 0, backgroundColors_2[backgroundColorIndex]);
                gui.setPaint(gp);
                gui.fillRect(0, 0, buttonWidth, height);
                break;
            case FOUR_WAY_GRADIENT:
                gui.drawImage(backgroundColorImages[backgroundColorIndex], 0, 0, null);
//                gui.drawImage(Utility.calculateFourWayGradient(buttonWidth, height, upperLeft[backgroundColorIndex], upperRight[backgroundColorIndex], lowerLeft[backgroundColorIndex], lowerRight[backgroundColorIndex]), 0, 0, null);
            }
        gui.drawImage(foregroundImage, 0, 0, this);
    }
    
    /**
     * <p>createButton</p>
     * Creates the button.
     */
    protected void createButton() {
        setLayout(new MigLayout("wrap 1, insets 0",
                "[fill," + width + "]",
                "[fill," + height + "]"));
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
        buttonRect = new Rectangle(buttonWidth, height);
        textIndentInitial = Main.THEME.getValue(buttonType + "TextIndent_i");
        textIndentFinal = Main.THEME.getValue(buttonType + "TextIndent_f");
        textIndentSteps = Main.THEME.getValue(buttonType + "TextIndentSteps");
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
            try {
                foregroundImage = foregroundImage.getSubimage(0, height * buttonNumber, Math.min(width, foregroundImage.getWidth()), Math.min(height, foregroundImage.getHeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        text = section.get(buttonType + ((buttonNumber == -1) ? "" : buttonNumber) + "Text");
        font = Main.THEME.getFont(categoryNumber, buttonType);
        Color backgroundColorGradientStartInitial = null;
        Color backgroundColorGradientEndInitial = null;
        Color backgroundColorGradientStartFinal = null;
        Color backgroundColorGradientEndFinal = null;
        int backgroundColorMode = Main.THEME.getValue("backgroundColorMode");
        if (buttonType.equals("button")) {
            switch (BackgroundColorMode.values()[backgroundColorMode]) {
            case SOLID:
                backgroundColorGradientStartInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_i");
                backgroundColorGradientStartFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_f");
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                break;
            case VERTICAL_GRADIENT:
                backgroundColorGradientStartInitial = category.getColorArray(buttonType + "BackgroundColorsInitial")[buttonNumber];
                backgroundColorGradientStartFinal = category.getColorArray(buttonType + "BackgroundColorsFinal")[buttonNumber];
                backgroundColorGradientEndInitial = category.getColorArray(buttonType + "BackgroundColorsInitial")[buttonNumber + 1];
                backgroundColorGradientEndFinal = category.getColorArray(buttonType + "BackgroundColorsFinal")[buttonNumber + 1];
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                backgroundColors_2 = Utility.getGradient(backgroundColorGradientEndInitial, backgroundColorGradientEndFinal, backgroundColorFadeSteps);
                break;
            case HORIZONTAL_GRADIENT:
                backgroundColorGradientStartInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientStartInitial");
                backgroundColorGradientEndInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientEndInitial");
                backgroundColorGradientStartFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientStartFinal");
                backgroundColorGradientEndFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientEndFinal");
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                backgroundColors_2 = Utility.getGradient(backgroundColorGradientEndInitial, backgroundColorGradientEndFinal, backgroundColorFadeSteps);
                break;
            case HORIZONTAL_BANDS:
                backgroundColorGradientStartInitial = category.getColorArray(buttonType + "BackgroundColorsInitial")[buttonNumber];
                backgroundColorGradientStartFinal = category.getColorArray(buttonType + "BackgroundColorsFinal")[buttonNumber];
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                break;
            case HORIZONTAL_BANDED_GRADIENT:
                backgroundColorGradientStartInitial = category.getColorArray(buttonType + "BackgroundColorsLeftInitial")[buttonNumber];
                backgroundColorGradientStartFinal = category.getColorArray(buttonType + "BackgroundColorsLeftFinal")[buttonNumber];
                backgroundColorGradientEndInitial = category.getColorArray(buttonType + "BackgroundColorsRightInitial")[buttonNumber];
                backgroundColorGradientEndFinal = category.getColorArray(buttonType + "BackgroundColorsRightFinal")[buttonNumber];
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                backgroundColors_2 = Utility.getGradient(backgroundColorGradientEndInitial, backgroundColorGradientEndFinal, backgroundColorFadeSteps);
                break;
            case FOUR_WAY_GRADIENT:
                backgroundColorImages = new BufferedImage[backgroundColorFadeSteps];
                upperLeft = Utility.getGradient(category.getColorArray(buttonType + "BackgroundColorsLeftInitial")[buttonNumber],
                        category.getColorArray(buttonType + "BackgroundColorsLeftFinal")[buttonNumber],
                        backgroundColorFadeSteps);
                lowerLeft = Utility.getGradient(category.getColorArray(buttonType + "BackgroundColorsLeftInitial")[buttonNumber + 1],
                        category.getColorArray(buttonType + "BackgroundColorsLeftFinal")[buttonNumber + 1],
                        backgroundColorFadeSteps);
                upperRight = Utility.getGradient(category.getColorArray(buttonType + "BackgroundColorsRightInitial")[buttonNumber],
                        category.getColorArray(buttonType + "BackgroundColorsRightFinal")[buttonNumber],
                        backgroundColorFadeSteps);
                lowerRight = Utility.getGradient(category.getColorArray(buttonType + "BackgroundColorsRightInitial")[buttonNumber + 1],
                        category.getColorArray(buttonType + "BackgroundColorsRightFinal")[buttonNumber + 1],
                        backgroundColorFadeSteps);
                for (int i = 0; i < backgroundColorImages.length; i++) {
                    backgroundColorImages[i] = Utility.calculateFourWayGradient(buttonWidth, height, upperLeft[i], upperRight[i], lowerLeft[i], lowerRight[i]);
                }
                break;
            }
        } else if (buttonType.equals("header")) {
            switch (BackgroundColorMode.values()[backgroundColorMode]) {
            case SOLID:
                backgroundColorGradientStartInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_i");
                backgroundColorGradientStartFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_f");
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                break;
            case VERTICAL_GRADIENT:
            case HORIZONTAL_GRADIENT:
                backgroundColorGradientStartInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientStartInitial");
                backgroundColorGradientEndInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientEndInitial");
                backgroundColorGradientStartFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientStartFinal");
                backgroundColorGradientEndFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientEndFinal");
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                backgroundColors_2 = Utility.getGradient(backgroundColorGradientEndInitial, backgroundColorGradientEndFinal, backgroundColorFadeSteps);
                break;
            case HORIZONTAL_BANDS:
                backgroundColorGradientStartInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_i");
                backgroundColorGradientStartFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColor_f");
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                break;
            case HORIZONTAL_BANDED_GRADIENT:
                backgroundColorGradientStartInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientStartInitial");
                backgroundColorGradientEndInitial = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientEndInitial");
                backgroundColorGradientStartFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientStartFinal");
                backgroundColorGradientEndFinal = Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorGradientEndFinal");
                backgroundColors = Utility.getGradient(backgroundColorGradientStartInitial, backgroundColorGradientStartFinal, backgroundColorFadeSteps);
                backgroundColors_2 = Utility.getGradient(backgroundColorGradientEndInitial, backgroundColorGradientEndFinal, backgroundColorFadeSteps);
                break;
            case FOUR_WAY_GRADIENT:
//                TODO make it read off ini
                upperLeft = Utility.getGradient(Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorUpperLeftInitial"),
                        Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorUpperLeftFinal"), backgroundColorFadeSteps);
                lowerLeft = Utility.getGradient(Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorLowerLeftInitial"),
                        Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorLowerLeftFinal"), backgroundColorFadeSteps);
                upperRight = Utility.getGradient(Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorUpperRightInitial"),
                        Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorUpperRightFinal"), backgroundColorFadeSteps);
                lowerRight = Utility.getGradient(Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorLowerRightInitial"),
                        Main.THEME.getColor(categoryNumber, buttonType, "BackgroundColorLowerRightFinal"), backgroundColorFadeSteps);
                backgroundColorImages = new BufferedImage[backgroundColorFadeSteps];
                for (int i = 0; i < backgroundColorImages.length; i++) {
                    backgroundColorImages[i] = Utility.calculateFourWayGradient(buttonWidth, height, upperLeft[i], lowerLeft[i], upperRight[i], lowerRight[i]);
                }
                break;
            }
        }
        
        fontColorInitial = Main.THEME.getColor(categoryNumber, buttonType, "FontColor_i");
        fontColorFinal = Main.THEME.getColor(categoryNumber, buttonType, "FontColor_f");
        fontColors = Utility.getGradient(fontColorInitial, fontColorFinal, backgroundColorFadeSteps);
        textIndents = new int[textIndentSteps];
        textIndents[0] = textIndentInitial;
        double textIndentStep = (double) (textIndentFinal - textIndentInitial) / (textIndentSteps - 1);
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
    protected abstract class BaseButtonMouseAdapter extends MouseAdapter {
        
        @Override
        public void mouseEntered(MouseEvent e) {
            Point mouse = e.getPoint();
            boolean isHovered = buttonRect.contains(mouse);
            if (isHovered) {
                mouseEnteredTimers();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouseExitedTimers();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            Point mouse = e.getPoint();
            boolean isHovered = buttonRect.contains(mouse);
            if (isHovered) {
                mouseEnteredTimers();
            } else {
                mouseExitedTimers();
            }
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
                Point mouse = e.getPoint();
                boolean isHovered = buttonRect.contains(mouse);
                if (isHovered) {
                    launch();
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
    
    public void launch() {
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
            if (backgroundColorIndex != backgroundColorFadeSteps - 1) {
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
    
    public void setBackgroundColorArray(Color[] colors) {
        this.backgroundColors = colors;
    }
    
    public void mouseEnteredTimers() {
        unindentTimer.stop();
        indentTimer.start();
        backgroundColorStepDownTimer.stop();
        backgroundColorStepUpTimer.start();
        fontColorStepDownTimer.stop();
        fontColorStepUpTimer.start();
    }
    
    public void mouseExitedTimers() {
        indentTimer.stop();
        unindentTimer.start();
        backgroundColorStepUpTimer.stop();
        backgroundColorStepDownTimer.start();
        fontColorStepUpTimer.stop();
        fontColorStepDownTimer.start();
    }
    
    public int getButtonNumber() {
        return buttonNumber;
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
