package p1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JPanel;

import org.ini4j.Ini;

import net.miginfocom.swing.MigLayout;

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
     * <p>category</p>
     * Category number.
     */
    private int category;
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
    private int maxHeight;
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
     * <p>main</p>
     * Main object that is used to invoke methods.
     */
    private Main main;
    
    /**
     * <p>Category</p>
     * Constructor.
     * @param category category number
     * @param main main object
     * @throws IOException IO error
     */
    public Category(int category, Main main) throws IOException {
        this.category = category;
        this.main = main;
        section = Main.CONFIG.get("Category" + this.category);
        this.numberOfButtons = Integer.parseInt(section.get("numberOfButtons"));
        setLayout(new MigLayout("wrap 1, insets 0",
                "[fill," + Main.HEADER_WIDTH + "]",
                "[fill," + Main.HEADER_HEIGHT + "]0[fill]0"));
        setBackground(Main.CLEAR);
        
        header = new HeaderButton(category);
        add(header);
        buttonPanel = new JPanel(new MigLayout("wrap 1, insets 0",
                "[grow, fill," + Main.HEADER_WIDTH + "]",
                "[grow, fill," + Main.BUTTON_HEIGHT + "]" + Main.BUTTON_SPACING + "[grow, fill," + Main.BUTTON_HEIGHT + "]"));
        buttonPanel.setBackground(Main.CLEAR);
        for (int i = 0; i < numberOfButtons; i++) {
            ImageButton button = new ImageButton(category, i);
            buttonPanel.add(button);
        }
        header.setButtonPanel(buttonPanel);
        add(buttonPanel);
        if (this.category + 1 == Main.NUMBER_OF_CATEGORIES) {
            JPanel blackBar = new JPanel();
            blackBar.setBackground(Color.BLACK);
            blackBar.setMinimumSize(new Dimension(Main.HEADER_WIDTH, 10));
            blackBar.setPreferredSize(new Dimension(Main.HEADER_WIDTH, 10));
            blackBar.setMaximumSize(new Dimension(Main.HEADER_WIDTH, 10));
            add(blackBar);
        }
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D)g;
        gui.setBackground(Main.CLEAR);
        gui.clearRect(0, 0, this.getWidth(), this.getHeight());
        main.repack();
    }
    
    /**
     * <p>collapseInstant</p>
     * Collapses the button panel instantly without steps.
     */
    public void collapseInstant() {
        buttonPanel.setMaximumSize(new Dimension (buttonPanel.getWidth(), 0));
        header.setIsExpandedFalse();
        revalidate();
    }
    
    /**
     * <p>collapse</p>
     * Collapses the button panel.
     */
    public void collapse() {
        header.expandTimer.stop();
        header.collapseTimer.start();
        header.setIsExpandedFalse();
    }
    
    /**
     * <p>getMaxHeight</p>
     * Gets categories maxHeight.
     * @return maxHeight
     */
    public int getMaxHeight() {
        return maxHeight;
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
     * <p>calculateMaxHeight</p>
     * Calculates the button panel's maximum height.
     */
    public void calculateMaxHeight() {
        maxHeight = buttonPanel.getHeight();
        expandStepAmount = maxHeight / Main.EXPAND_STEPS;
        collapseStepAmount = maxHeight / Main.EXPAND_STEPS;
        header.setParentMaxHeight(maxHeight);
        header.setExpandStepAmount(expandStepAmount);
        header.setCollapseStepAmount(collapseStepAmount);
    }
}
