package p1;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
    
    private double backgroundColorRCurrent;
    private double backgroundColorGCurrent;
    private double backgroundColorBCurrent;
    private double backgroundColorACurrent;
    private Color backgroundColorCurrent;
    
    private int backgroundColorRFinal;
    private int backgroundColorGFinal;
    private int backgroundColorBFinal;
    private int backgroundColorAFinal;
    private Color backgroundColorFinal;
   
    private double backgroundColorRStep;
    private double backgroundColorGStep;
    private double backgroundColorBStep;
    private double backgroundColorAStep;

    private BufferedImage foregroundImage;
    private int foregroundImageXOffset;
    private int foregroundImageYOffset;
    private int foregroundImageXCrop;
    private int foregroundImageYCrop;
    
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
    private Timer colorStepUpTimer;
    private Timer colorStepDownTimer;
    
    Timer expandTimer;
    Timer collapseTimer;
    
    private Font font;
    
    boolean isExpanded = true;
    private int expandStepAmount;
    private int collapseStepAmount;
    private int parentMaxHeight;
    
    public ImageButton(int category, int buttonNumber) throws IOException {
        this.category = category;
        this.buttonNumber = buttonNumber;
        readVariables(category, buttonNumber);
        createButton();
        indentTimer = new Timer(indentSleep, new IndentTimerListener());
        unindentTimer = new Timer(indentSleep, new UnindentTimerListener());
        colorStepUpTimer = new Timer(indentSleep, new ColorStepUpTimerListener());
        colorStepDownTimer = new Timer(indentSleep, new ColorStepDownTimerListener());
        addMouseListener(new ImageButtonMouseAdapter());
        
//        Makes the panels slide instead of accordion effect
        setMinimumSize(new Dimension(width, height));
    }
    
    public ImageButton(int category) {
        this.category = category;
        readVariables(category);
        createButton();
        indentTimer = new Timer(indentSleep, new IndentTimerListener());
        unindentTimer = new Timer(indentSleep, new UnindentTimerListener());
        colorStepUpTimer = new Timer(indentSleep, new ColorStepUpTimerListener());
        colorStepDownTimer = new Timer(indentSleep, new ColorStepDownTimerListener());
        expandTimer = new Timer(Main.EXPAND_SLEEP, new ExpandTimerListener());
        collapseTimer = new Timer(Main.EXPAND_SLEEP, new CollapseTimerListener());
        addMouseListener(new HeaderButtonMouseAdapter());
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D)g;
        gui.setBackground(backgroundColorCurrent);
        gui.clearRect(0, 0, width, height);
//        g.drawImage(foregroundImage, 0, 0, this);
//        gui.drawImage(foregroundImage,
//                foregroundImageXOffset, foregroundImageYOffset, width + foregroundImageXOffset, height + foregroundImageYOffset,
//                foregroundImageXCrop,foregroundImageYCrop + buttonNumber * height, width, (buttonNumber + 1) * height, null);
    }
    
//    public void paint(Graphics g) {
//        super.paint(g);
//        g.drawImage(foregroundImage, foregroundImageXOffset, 0, this);
//    }

    private void createButton() {
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
        setMinimumSize(new Dimension(0,0));
        setPreferredSize(new Dimension(width,height));
        setMaximumSize(new Dimension(width,height));
    }
    
    private void readVariables(int category, int buttonNumber) throws IOException {
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
        
        foregroundImage = ImageIO.read(new File(section.get("ForegroundImage")));
        foregroundImageXOffset = Integer.parseInt(section.get("ForegroundImageXOffset"));
        foregroundImageYOffset = Integer.parseInt(section.get("ForegroundImageYOffset"));
        foregroundImageXCrop = Integer.parseInt(section.get("ForegroundImageXCrop"));
        foregroundImageYCrop = Integer.parseInt(section.get("ForegroundImageYCrop"));
        foregroundImage = foregroundImage.getSubimage(foregroundImageXCrop, foregroundImageYCrop + (height * buttonNumber), Math.min(width, foregroundImage.getWidth()), Math.min(height, foregroundImage.getHeight()));
        
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
    
    private class ImageButtonMouseAdapter extends BaseButtonMouseAdapter {
        
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
    
    private class HeaderButtonMouseAdapter extends ImageButtonMouseAdapter {
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                for (int i = 0; i < Main.categoryArray.length; i++) {
                    if (i != category) {
                        Main.categoryArray[i].collapse();
                    }
                }
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
    
    private void expand() {
        getParent().getComponent(1).setMaximumSize(new Dimension(getParent().getComponent(1).getWidth(),
                getParent().getComponent(1).getHeight() + expandStepAmount));
        revalidate();
    }
    
    private void collapse() {
        getParent().getComponent(1).setMaximumSize(new Dimension(getParent().getComponent(1).getWidth(),
                getParent().getComponent(1).getHeight() - collapseStepAmount));
        revalidate();
    }
    
    private class IndentTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("indent timer");
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
//            System.out.println("unindent timer");
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
//            System.out.println("color up timer");
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
//            System.out.println("color down timer");
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
    
    private class ExpandTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("expand timer");
            if (getParent().getComponent(1).getHeight() < parentMaxHeight) {
                expand();
            } else {
                expandTimer.stop();
            }
        }
    }
    
    private class CollapseTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("collapse timer");
            if (getParent().getComponent(1).getHeight() > 0) {
                collapse();
            } else {
                collapseTimer.stop();
            }
        }
    }
    
    private int forceRGB(int i) {
        return Math.min(255, Math.max(0, i));
    }
    
    private double forceRGB(double i) {
        return Math.min(255.0, Math.max(0.0, i));
    }
    
    public void setExpandStepAmount(int step) {
        expandStepAmount = step;
    }
    
    public void setCollapseStepAmount(int step) {
        collapseStepAmount = step;
    }
    
    public void setParentMaxHeight(int height) {
        parentMaxHeight = height;
    }
}
