package p1;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * <p>HeaderButton.</p>
 * @author Stephen Cheng
 * @version 1.0
 */
public class HeaderButton extends ImageButton {

    /**
     * <p>serialVersionUID.</p>
     */
    private static final long serialVersionUID = 8369307409097067065L;
    /**
     * <p>category</p>
     * Parent category.
     */
    private Category category;
    /**
     * <p>buttonPanel</p>
     * Parent's button panel.
     */
    private JPanel buttonPanel;
    
    /**
     * <p>HeaderButton</p>
     * Constructor.
     * @param categoryNumber categoryNumber
     * @throws IOException e
     */
    public HeaderButton(int categoryNumber) throws IOException {
        super(categoryNumber, -1, null);
        addMouseListener(new HeaderButtonMouseAdapter());
        expandTimer = new Timer(Main.EXPAND_SLEEP, new ExpandTimerListener());
        collapseTimer = new Timer(Main.EXPAND_SLEEP, new CollapseTimerListener());
    }
    
    /**
     * <p>HeaderButtonMouseAdapter.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    private class HeaderButtonMouseAdapter extends BaseButtonMouseAdapter {
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                Point mouse = e.getPoint();
                boolean isHovered = buttonRect.contains(mouse);
                if (isHovered) {
                    for (int i = 0; i < Main.categoryArray.length; i++) {
                        if (i != categoryNumber) {
                            Main.categoryArray[i].collapse();
                        }
                    }
                    if (category.getIsExpanded()) {
                        expandTimer.stop();
                        collapseTimer.start();
                        category.setIsExpanded(false);
                    } else {
                        collapseTimer.stop();
                        expandTimer.start();
                        category.setIsExpanded(true);
                    }
                }
            }
        }
    }
    
    /**
     * <p>ExpandTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    private class ExpandTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonPanel.getHeight() < category.getButtonPanelMaxHeight()) {
                category.expandStep();
            } else {
                expandTimer.stop();
            }
        }
    }
    
    /**
     * <p>CollapseTimerListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    private class CollapseTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonPanel.getHeight() > 0) {
                category.collapseStep();
            } else {
                collapseTimer.stop();
            }
        }
    }
    
    /**
     * <p>setCategory</p>
     * Sets the category and button panel.
     * @param category category
     */
    public void setCategory(Category category) {
        this.category = category;
        this.buttonPanel = category.getButtonPanel();
    }
    
}
