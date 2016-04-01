package p1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    
    public static final Color CLEAR = new Color(0,0,0,0);
    JLabel time;
    JLabel seconds;
    JLabel ampm;
    JLabel dayOfTheYear;
    int clockFontSize = 120;
    int smallFontSize = 54;
    Font clockFont = new Font("Digital-7 Mono", Font.ITALIC, clockFontSize);
    Font secondsFont = new Font("Digital-7 Mono", Font.ITALIC, smallFontSize);
    Font dateFont = new Font("Michroma", Font.PLAIN, 16);
    DateFormat currentTimeFormat;
    DateFormat timeFormat = new SimpleDateFormat("h:mm");
    DateFormat militaryTimeFormat = new SimpleDateFormat("HH:mm");
    DateFormat secondsFormat = new SimpleDateFormat("ss");
    DateFormat ampmFormat = new SimpleDateFormat("a");
    DateFormat dayOfTheYearFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
    Date date = new Date();
    
    public Clock() {
        super("Clock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(false);
        setFocusableWindowState(false);
        setUndecorated(true);
        setBackground(CLEAR);
        
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        Dimension clockSize = new Dimension((int) clockFont.getStringBounds("00:00", frc).getWidth()+10, clockFontSize);
        Dimension secondsSize = new Dimension((int) secondsFont.getStringBounds("00", frc).getWidth(), smallFontSize);
        
        currentTimeFormat = timeFormat;
        
        JPanel panel = new JPanel(new MigLayout("wrap 2, insets 0",
                "[fill]10[fill]0",
                "[fill]-24[fill]0"));
        panel.setBackground(CLEAR);
        Timer clockTimer = new Timer(100, new UpdateTimeListener());
        
        time = new JLabel(timeFormat.format(date), SwingConstants.RIGHT);
        time.setFont(clockFont);
        time.addMouseListener(new ClockMouseAdapter());
        time.setMinimumSize(clockSize);
        time.setOpaque(true);
        time.setBackground(new Color(0,0,0,1));
        panel.add(time, "span 1 2, align right top, push");
        
        seconds = new JLabel(secondsFormat.format(date), SwingConstants.RIGHT);
        seconds.setFont(secondsFont);
        seconds.setMinimumSize(secondsSize);
        panel.add(seconds, "align right");
        
        ampm = new JLabel(ampmFormat.format(date), SwingConstants.RIGHT);
        ampm.setFont(secondsFont);
        ampm.setMinimumSize(secondsSize);
        panel.add(ampm, "align right");
        
        dayOfTheYear = new JLabel(dayOfTheYearFormat.format(date), SwingConstants.RIGHT);
        dayOfTheYear.setFont(dateFont);
        panel.add(dayOfTheYear, "span 2, align right");
        
        getContentPane().add(panel);
        pack();
        setLocation(new Point(Main.WIN_X, 50));
        setVisible(true);
        clockTimer.start();
    }
    
    private class UpdateTimeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            date = new Date();
            time.setText(currentTimeFormat.format(date));
            seconds.setText(secondsFormat.format(date));
            ampm.setText(ampmFormat.format(date));
            dayOfTheYear.setText(dayOfTheYearFormat.format(date));
            repaint();
        }
    }
    
    private class ClockMouseAdapter extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent event) {
            currentTimeFormat = militaryTimeFormat;
        }
        
        @Override
        public void mouseExited(MouseEvent event) {
            currentTimeFormat = timeFormat;
        }
    }
    
    public static void main(String[] args) {
        new Clock();
    }
}
