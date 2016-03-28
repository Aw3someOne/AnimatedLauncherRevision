package p1;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.ini4j.Ini;

import net.miginfocom.swing.MigLayout;

public class Category extends JPanel {
    
    private int category;
    private int numberOfButtons;
    private JPanel buttonPanel;
    private int maxHeight;
    private ImageButton header;
    
    public Category(int category) {
        this.category = category;
        Ini.Section section = Main.CONFIG.get("Category" + category);
        this.numberOfButtons = Integer.parseInt(section.get("numberOfButtons"));
        setLayout(new MigLayout("wrap 1, insets 0",
                "[fill," + Main.HEADER_WIDTH + "]",
                "[fill," + Main.HEADER_HEIGHT + "]0[fill]0"));
        setBackground(Main.CLEAR);
        header = new ImageButton(category);
        add(header);
        buttonPanel = new JPanel(new MigLayout("wrap 1, insets 0",
                "[grow, fill," + Main.HEADER_WIDTH + "]",
                "[grow, fill," + Main.BUTTON_HEIGHT + "]0"));
        for (int i = 0; i < numberOfButtons; i++) {
            ImageButton button = new ImageButton(category, i);
            buttonPanel.add(button);
        }
        add(buttonPanel);
    }
    
    public void collapseInstant() {
        buttonPanel.setMaximumSize(new Dimension (buttonPanel.getWidth(), 0));
        header.isExpanded = false;
        revalidate();
    }
    
    public void collapse() {
        header.expandTimer.stop();
        header.collapseTimer.start();
        header.isExpanded = false;
    }
    
    public int getMaxHeight() {
        return maxHeight;
    }
    
    public void calculateMaxHeight() {
        maxHeight = buttonPanel.getHeight();
    }
}
