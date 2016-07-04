package p1;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class HeaderButton extends ImageButton {

    private int expandStepAmount;
    private int collapseStepAmount;
    private int parentMaxHeight;
    private boolean isExpanded = true;
    JPanel buttonPanel;
    
    public HeaderButton(int category) throws IOException {
        super(category, -1);
        addMouseListener(new HeaderButtonMouseAdapter());
        expandTimer = new Timer(Main.EXPAND_SLEEP, new ExpandTimerListener());
        collapseTimer = new Timer(Main.EXPAND_SLEEP, new CollapseTimerListener());
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
    
    private class ExpandTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("expand timer");
            if (buttonPanel.getHeight() < parentMaxHeight) {
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
            if (buttonPanel.getHeight() > 0) {
                collapse();
            } else {
                collapseTimer.stop();
            }
        }
    }
    
    public void setIsExpandedFalse() {
        isExpanded = false;
    }
    
    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
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
    
    private void expand() {
        buttonPanel.setMaximumSize(new Dimension(buttonPanel.getWidth(),
                buttonPanel.getHeight() + expandStepAmount));
        revalidate();
    }
    
    private void collapse() {
        buttonPanel.setMaximumSize(new Dimension(buttonPanel.getWidth(),
                buttonPanel.getHeight() - collapseStepAmount));
        revalidate();
    }
    
}
