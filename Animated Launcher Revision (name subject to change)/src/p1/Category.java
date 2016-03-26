package p1;


import java.awt.Color;

import javax.swing.JPanel;

import org.ini4j.Ini;

import net.miginfocom.swing.MigLayout;

public class Category extends JPanel {
    
    private int category;
    private int numberOfButtons;
    private JPanel buttonPanel;
    
    public Category(int category) {
        this.category = category;
        Ini.Section section = Main.CONFIG.get("Category" + category);
        this.numberOfButtons = Integer.parseInt(section.get("numberOfButtons"));
        setLayout(new MigLayout("wrap 1, insets 0",
                "[grow, fill," + Main.HEADER_WIDTH + "]",
                "[grow, fill," + Main.HEADER_HEIGHT + "]0[grow, fill]0"));
        setBackground(Main.CLEAR);
        ImageButton header = new ImageButton(category);
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
}
