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

public class Category extends JPanel {
    
    private int category;
    private int numberOfButtons;
    private JPanel buttonPanel;
    private int maxHeight;
    private HeaderButton header;
    private Image foregroundImage;
    private int foregroundImageXOffset;
    private int foregroundImageYOffset;
    private int foregroundImageXCrop;
    private int foregroundImageYCrop;
    private int[] foregroundImageBounds;
    private Ini.Section section;
    private int expandStepAmount;
    private int collapseStepAmount;
    private Main main;
    
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
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gui = (Graphics2D)g;
        gui.setBackground(Main.CLEAR);
        gui.clearRect(0, 0, this.getWidth(), this.getHeight());
        main.repack();
    }
    
    public void collapseInstant() {
        buttonPanel.setMaximumSize(new Dimension (buttonPanel.getWidth(), 0));
        header.setIsExpandedFalse();
        revalidate();
    }
    
    public void collapse() {
        header.expandTimer.stop();
        header.collapseTimer.start();
        header.setIsExpandedFalse();
    }
    
    public int getMaxHeight() {
        return maxHeight;
    }
    
    public int getExpandStepAmount() {
        return expandStepAmount;
    }
    
    public int getCollapseStepAmount() {
        return collapseStepAmount;
    }
    
    public void calculateMaxHeight() {
        maxHeight = buttonPanel.getHeight();
        expandStepAmount = maxHeight / Main.EXPAND_STEPS;
        collapseStepAmount = maxHeight / Main.EXPAND_STEPS;
        header.setParentMaxHeight(maxHeight);
        header.setExpandStepAmount(expandStepAmount);
        header.setCollapseStepAmount(collapseStepAmount);
    }
}
