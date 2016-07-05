package p1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;

public class Clock extends JFrame {
    
    private static final Color CLEAR = new Color(0,0,0,0);
    private static final GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static final Font CLOCK_FONT = Main.THEME.getFont("clockFont");
    private static final Font SECONDS_FONT = Main.THEME.getFont("smallFont");
    private static final Font DATE_FONT = Main.THEME.getFont("textFont");
    private static final Color CLOCK_FONT_COLOR = Main.THEME.getColor("clockFontColor");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("h:mm");
    private static final DateFormat MILITARY_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final DateFormat SECONDS_FORMAT = new SimpleDateFormat("ss");
    private static final DateFormat AMPM_FORMAT = new SimpleDateFormat("a");
    private static final DateFormat DAY_OF_THE_YEAR_FORMAT = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
    private static final int WIN_X = Main.THEME.getValue("clockWinX");
    private static final int WIN_Y = Main.THEME.getValue("clockWinY");
    DateFormat currentTimeFormat;
    JLabel time;
    JLabel seconds;
    JLabel ampm;
    JLabel dayOfTheYear;
    Date date = new Date();
    
    public Clock() {
        super("Clock");
        setLayout(new MigLayout("insets 0"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(false);
        setFocusableWindowState(false);
        setUndecorated(true);
        setBackground(CLEAR);
        
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        Dimension clockSize = new Dimension((int) CLOCK_FONT.getStringBounds("00:00", frc).getWidth()+10, (int) getFontMetrics(CLOCK_FONT).getHeight());
        Dimension secondsSize = new Dimension((int) SECONDS_FONT.getStringBounds("00", frc).getWidth()+10, (int) getFontMetrics(SECONDS_FONT).getHeight());
        
        currentTimeFormat = TIME_FORMAT;
        
        JPanel panel = new JPanel(new MigLayout("wrap 2, insets 0",
                "0[fill]10[fill]0",
                "0[fill]-20[fill]"));
        panel.setBackground(CLEAR);
        Timer clockTimer = new Timer(100, new UpdateTimeListener());
        
        JPanel rightPanel = new JPanel(new MigLayout("insets 0",
                "0[fill]0",
                "10[fill]-10[fill]0"));
        rightPanel.setBackground(CLEAR);
        
        time = new JLabel(TIME_FORMAT.format(date), SwingConstants.RIGHT);
        time.setFont(CLOCK_FONT);
        time.setForeground(CLOCK_FONT_COLOR);
        time.addMouseListener(new ClockMouseAdapter());
        time.setMinimumSize(clockSize);
        time.setOpaque(true);
        time.setBackground(new Color(0,0,0,1));
        panel.add(time, "align right top");
        
        seconds = new JLabel(SECONDS_FORMAT.format(date), SwingConstants.RIGHT);
        seconds.setFont(SECONDS_FONT);
        seconds.setForeground(CLOCK_FONT_COLOR);
        seconds.setMinimumSize(secondsSize);
        rightPanel.add(seconds, "align right, wrap");
        
        ampm = new JLabel(AMPM_FORMAT.format(date), SwingConstants.RIGHT);
        ampm.setFont(SECONDS_FONT);
        ampm.setForeground(CLOCK_FONT_COLOR);
        ampm.setMinimumSize(secondsSize);
        rightPanel.add(ampm, "align right");
        
        panel.add(rightPanel);
        
        dayOfTheYear = new JLabel(DAY_OF_THE_YEAR_FORMAT.format(date), SwingConstants.RIGHT);
        dayOfTheYear.setFont(DATE_FONT);
        dayOfTheYear.setForeground(CLOCK_FONT_COLOR);
        panel.add(dayOfTheYear, "span 2, align right");
        
        getContentPane().add(panel);
        pack();
        setLocation(new Point(WIN_X, WIN_Y));
        setVisible(true);
        clockTimer.start();
    }
    
    private class UpdateTimeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            updateTime();
        }
    }
    
    private class ClockMouseAdapter extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent event) {
            currentTimeFormat = MILITARY_TIME_FORMAT;
            updateTime();
        }
        
        @Override
        public void mouseExited(MouseEvent event) {
            currentTimeFormat = TIME_FORMAT;
            updateTime();
        }
    }
    
    private void updateTime() {
        date = new Date();
        time.setText(currentTimeFormat.format(date));
        seconds.setText(SECONDS_FORMAT.format(date));
        ampm.setText(AMPM_FORMAT.format(date));
        dayOfTheYear.setText(DAY_OF_THE_YEAR_FORMAT.format(date));
        repaint();
    }
    
    public static void main(String[] args) {
        new Clock();
    }
}
