package p1;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ImageButton extends JPanel {
    public ImageButton() {
        setLayout(new MigLayout("wrap 1, insets 0",
                "[grow, fill]"));
    }
    
    public void readVariables() {
        
    }
}
