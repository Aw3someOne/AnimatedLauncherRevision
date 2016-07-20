package p1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.ini4j.Ini;

import net.miginfocom.swing.MigLayout;
import p1.Theme.BackgroundColorMode;

/**
 * <p>Category.</p>
 * @author Stephen Cheng
 * @version 1.0
 */
public class Category extends JPanel {
    
    /**
     * <p>serialVersionUID.</p>
     */
    private static final long serialVersionUID = 675687574323872635L;
    /**
     * <p>categoryNumber</p>
     * Category number.
     */
    private int categoryNumber;
    /**
     * <p>numberOfButtons</p>
     * Number of buttons in the category.
     */
    private int numberOfButtons;
    /**
     * <p>buttonPanel</p>
     * Holds all the buttons.
     */
    private JPanel buttonPanel;
    /**
     * <p>maxHeight</p>
     * Maximum height of the category.
     */
    private int buttonPanelMaxHeight;
    /**
     * <p>header</p>
     * Header button.
     */
    private HeaderButton header;
    /**
     * <p>section</p>
     * Section of the config file.
     */
    private Ini.Section section;
    /**
     * <p>expandStepAmount</p>
     * Number of pixels that the panel grows by at a time when the category expands.
     */
    private int expandStepAmount;
    /**
     * <p>collapseStepAmount</p>
     * Numbers of pixels that the panel shrinks by at a time when the category collapses.
     */
    private int collapseStepAmount;
    /**
     * <p>isExpanded</p>
     * Checks if the button panel is expanded.
     */
    private boolean isExpanded = true;
    private int backgroundColorMode;
    private Color[] headerBackgroundColorsInitial;
    private Color[] headerBackgroundColorsFinal;
    private Color[] headerBackgroundColorsInitialEndPoint;
    private Color[] headerBackgroundColorsFinalEndPoint;
    private Color[] buttonBackgroundColorsInitial;
    private Color[] buttonBackgroundColorsFinal;
    private Color[] buttonBackgroundColorsInitialEndPoint;
    private Color[] buttonBackgroundColorsFinalEndPoint;
    private Map<String, Color[]> colorMap;
    private ImageButton[] buttons;
    /**
     * <p>main</p>
     * Main object that is used to invoke methods.
     */
    private Main main;
    private Color[] buttonBackgroundColorsLeftInitial;
    private Color[] buttonBackgroundColorsLeftFinal;
    private Color[] buttonBackgroundColorsRightInitial;
    private Color[] buttonBackgroundColorsRightFinal;
    
    /**
     * <p>Category</p>
     * Constructor.
     * @param category category number
     * @param main main object
     * @throws IOException IO error
     */
    public Category(int category, Main main) throws IOException {
        this.categoryNumber = category;
        this.main = main;
        section = Main.CONFIG.get("Category" + this.categoryNumber);
        this.numberOfButtons = Integer.parseInt(section.get("numberOfButtons"));
        setLayout(new MigLayout("wrap 1, insets 0",
                "[fill," + Main.HEADER_WIDTH + "]",
                "[fill," + Main.HEADER_HEIGHT + "]0[fill]0"));
        setBackground(Main.CLEAR);
        
        backgroundColorMode = Main.THEME.getValue("backgroundColorMode");
        colorMap = new HashMap<String, Color[]>();
        switch (BackgroundColorMode.values()[backgroundColorMode]) {
        case SOLID:
            break;
        case VERTICAL_GRADIENT:
            buttonBackgroundColorsInitial = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorGradientStartInitial"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorGradientEndInitial"),
                    numberOfButtons + 1);
            buttonBackgroundColorsFinal = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorGradientStartFinal"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorGradientEndFinal"),
                    numberOfButtons + 1);
            colorMap.put("buttonBackgroundColorsInitial", buttonBackgroundColorsInitial);
            colorMap.put("buttonBackgroundColorsFinal", buttonBackgroundColorsFinal);
            break;
        case HORIZONTAL_GRADIENT:
            break;
        case HORIZONTAL_BANDS:
            buttonBackgroundColorsInitial = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorGradientStartInitial"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorGradientEndInitial"),
                    numberOfButtons);
            buttonBackgroundColorsFinal = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorGradientStartFinal"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorGradientEndFinal"),
                    numberOfButtons);
            colorMap.put("buttonBackgroundColorsInitial", buttonBackgroundColorsInitial);
            colorMap.put("buttonBackgroundColorsFinal", buttonBackgroundColorsFinal);
            break;
        case HORIZONTAL_BANDED_GRADIENT:
            buttonBackgroundColorsLeftInitial = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorUpperLeftInitial"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorLowerLeftInitial"),
                    numberOfButtons);
            buttonBackgroundColorsLeftFinal = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorUpperLeftFinal"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorLowerLeftFinal"),
                    numberOfButtons);
            buttonBackgroundColorsRightInitial = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorUpperRightInitial"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorLowerRightInitial"),
                    numberOfButtons);
            buttonBackgroundColorsRightFinal = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorUpperRightFinal"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorLowerRightFinal"),
                    numberOfButtons);
            colorMap.put("buttonBackgroundColorsLeftInitial", buttonBackgroundColorsLeftInitial);
            colorMap.put("buttonBackgroundColorsLeftFinal", buttonBackgroundColorsLeftFinal);
            colorMap.put("buttonBackgroundColorsRightInitial", buttonBackgroundColorsRightInitial);
            colorMap.put("buttonBackgroundColorsRightFinal", buttonBackgroundColorsRightFinal);
            break;
        case FOUR_WAY_GRADIENT:
            buttonBackgroundColorsLeftInitial = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorUpperLeftInitial"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorLowerLeftInitial"),
                    numberOfButtons + 1);
            buttonBackgroundColorsLeftFinal = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorUpperLeftFinal"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorLowerLeftFinal"),
                    numberOfButtons + 1);
            buttonBackgroundColorsRightInitial = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorUpperRightInitial"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorLowerRightInitial"),
                    numberOfButtons + 1);
            buttonBackgroundColorsRightFinal = Utility.getGradient(Main.THEME.getColor(categoryNumber, "button", "BackgroundColorUpperRightFinal"),
                    Main.THEME.getColor(categoryNumber, "button", "BackgroundColorLowerRightFinal"),
                    numberOfButtons + 1);
            colorMap.put("buttonBackgroundColorsLeftInitial", buttonBackgroundColorsLeftInitial);
            colorMap.put("buttonBackgroundColorsLeftFinal", buttonBackgroundColorsLeftFinal);
            colorMap.put("buttonBackgroundColorsRightInitial", buttonBackgroundColorsRightInitial);
            colorMap.put("buttonBackgroundColorsRightFinal", buttonBackgroundColorsRightFinal);
            break;
        }
        
        header = new HeaderButton(category);
        add(header);
        buttonPanel = new JPanel(new MigLayout("wrap 1, insets 0",
                "[grow, fill," + Main.HEADER_WIDTH + "]",
                "[grow, fill," + Main.BUTTON_HEIGHT + "]" + Main.BUTTON_SPACING + "[grow, fill," + Main.BUTTON_HEIGHT + "]"));
        buttonPanel.setBackground(Main.CLEAR);
//        ButtonMouseAdapter adapter = new ButtonMouseAdapter();
        buttons = new ImageButton[numberOfButtons];
        for (int i = 0; i < numberOfButtons; i++) {
            buttons[i] = new ImageButton(category, i, this);
//            buttons[i].addMouseListener(adapter);
//            buttons[i].addMouseMotionListener(adapter);
            buttonPanel.add(buttons[i]);
        }
        header.setCategory(this);
        add(buttonPanel);
        if (this.categoryNumber + 1 == Main.NUMBER_OF_CATEGORIES) {
            JPanel blackBar = new JPanel();
            blackBar.setBackground(Color.BLACK);
            blackBar.setMinimumSize(new Dimension(Main.HEADER_WIDTH, 10));
            blackBar.setPreferredSize(new Dimension(Main.HEADER_WIDTH, 10));
            blackBar.setMaximumSize(new Dimension(Main.HEADER_WIDTH, 10));
            add(blackBar);
        }

    }
    
//    private class ButtonMouseAdapter extends MouseAdapter {
//        
//        @Override
//        public void mouseReleased(MouseEvent e) {
//            int buttonNumber = ((ImageButton) e.getSource()).getButtonNumber();
//            buttons[buttonNumber].launch();
//        }
//        
//        @Override
//        public void mouseEntered(MouseEvent e) {
//            ((ImageButton) e.getSource()).mouseEnteredTimers();
//        }
//        
//        @Override
//        public void mouseExited(MouseEvent e) {
//            ((ImageButton) e.getSource()).mouseExitedTimers();
//        }
//        
//    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D) g;
        gui.setBackground(Main.CLEAR);
        gui.clearRect(0, 0, this.getWidth(), this.getHeight());
        main.repack();
    }
    
    /**
     * <p>collapseInstant</p>
     * Collapses the button panel instantly without steps.
     */
    public void collapseInstant() {
        buttonPanel.setMaximumSize(new Dimension(buttonPanel.getWidth(), 0));
        isExpanded = false;
        revalidate();
    }
    
    /**
     * <p>expandStep</p>
     * Expands the button panel by 1 step.
     */
    public void expandStep() {
        buttonPanel.setMaximumSize(new Dimension(buttonPanel.getWidth(),
                buttonPanel.getHeight() + expandStepAmount));
        revalidate();
    }
    
    /**
     * <p>collapseStep</p>
     * Collapse the button panel by 1 step.
     */
    public void collapseStep() {
        buttonPanel.setMaximumSize(new Dimension(buttonPanel.getWidth(),
                buttonPanel.getHeight() - collapseStepAmount));
        revalidate();
    }
    
    /**
     * <p>collapse</p>
     * Collapses the button panel.
     */
    public void collapse() {
        header.expandTimer.stop();
        header.collapseTimer.start();
        isExpanded = false;
    }
    
    /**
     * <p>getColorArray</p>
     * Gets a color array from the map.
     * @param key key
     * @return color array
     */
    public Color[] getColorArray(String key) {
        return colorMap.get(key);
    }
    
    /**
     * <p>getButtonPanelMaxHeight</p>
     * Gets categories maxHeight.
     * @return maxHeight
     */
    public int getButtonPanelMaxHeight() {
        return buttonPanelMaxHeight;
    }
    
    /**
     * <p>getExpandStepAmount</p>
     * Gets the expand step amount.
     * @return expandStepAmount;
     */
    public int getExpandStepAmount() {
        return expandStepAmount;
    }
    
    /**
     * <p>getCollapseStepAmount</p>
     * Gets the collapse step amount.
     * @return collapseStepAmount
     */
    public int getCollapseStepAmount() {
        return collapseStepAmount;
    }
    
    /**
     * <p>getIsExpanded</p>
     * Gets button panel expanded state.
     * @return isExpanded
     */
    public boolean getIsExpanded() {
        return isExpanded;
    }
    
    /**
     * <p>getButtonPanel</p>
     * Gets button panel.
     * @return buttonPanel
     */
    public JPanel getButtonPanel() {
        return buttonPanel;
    }
    
    /**
     * <p>setIsExpanded</p>
     * Sets button panel expanded state.
     * @param isExpanded boolean
     */
    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }
    
    /**
     * <p>calculateMaxHeight</p>
     * Calculates the button panel's maximum height.
     */
    public void calculateMaxHeight() {
        buttonPanelMaxHeight = buttonPanel.getHeight();
        expandStepAmount = buttonPanelMaxHeight / Main.EXPAND_STEPS;
        collapseStepAmount = buttonPanelMaxHeight / Main.EXPAND_STEPS;
    }
    
}
