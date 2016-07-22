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
import p1.ClockTheme.ThemeName;

public class Clock extends JFrame {
    
    /**
     * <p>CLEAR</p>
     * Transparent color.
     */
    private static final Color CLEAR = new Color(0, 0, 0, 0);
    /**
     * <p>GE</p>
     * Graphics Environment.
     */
    private static final GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static final ThemeName THEME_NAME;
    static {
        ThemeName themeName = null;
        String themeString = Main.SYSTEM.get("clockTheme");
        for (ThemeName t : ThemeName.values()) {
            if (ThemeName.valueOf(themeString).equals(t)) {
                themeName = t;
                break;
            }
        }
        THEME_NAME = themeName;
    }
    private static final ClockTheme THEME = new ClockTheme(THEME_NAME);
    /**
     * <p>CLOCK_FONT_COLOR</p>
     * Font Color.
     */
    private static final Color CLOCK_FONT_COLOR = THEME.getColor("clockFontColor");
    /**
     * <p>TIME_FORMAT</p>
     * Used to format time.
     */
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("h:mm");
    /**
     * <p>MILITARY_TIME_FORMAT</p>
     * Used to format time.
     */
    private static final DateFormat MILITARY_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    /**
     * <p>SECONDS_FORMAT</p>
     * Used to display seconds.
     */
    private static final DateFormat SECONDS_FORMAT = new SimpleDateFormat("ss");
    /**
     * <p>AMPM_FORMAT</p>
     * am/pm.
     */
    private static final DateFormat AMPM_FORMAT = new SimpleDateFormat("a");
    /**
     * <p>DAY_OF_THE_YEAR_FORMAT</p>
     * Day of the week, Month, date, year.
     */
    private static final DateFormat DAY_OF_THE_YEAR_FORMAT = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
    /**
     * <p>DAY_NUMBER</p>
     * Day of the month.
     */
    private static final DateFormat DAY_NUMBER = new SimpleDateFormat("dd");
    /**
     * <p>DAY_NAME</p>
     * Day of the week.
     */
    private static final DateFormat DAY_NAME = new SimpleDateFormat("EEEE");
    /**
     * <p>MONTH</p>
     * Month.
     */
    private static final DateFormat MONTH = new SimpleDateFormat("MMMM");
    /**
     * <p>YEAR</p>
     * Year.
     */
    private static final DateFormat YEAR = new SimpleDateFormat("yyyy");
    /**
     * <p>WIN_X</p>
     * How far the window is positioned from the left side of the screen.
     */
    private static final int WIN_X = THEME.getValue("clockWinX");
    /**
     * <p>WIN_Y</p>
     * How far the window is positioned from the top of the screen.
     */
    private static final int WIN_Y = THEME.getValue("clockWinY");
    /**
     * <p>TEXT_CASE</p>
     * Case to display information with.
     */
    private static final String TEXT_CASE = THEME.getCase();
    /**
     * <p>currentTimeFormat</p>
     * Used to format time.
     */
    DateFormat currentTimeFormat;
    /**
     * <p>time</p>
     * Displays current time.
     */
    JLabel timeLabel;
    /**
     * <p>seconds</p>
     * Displays seconds.
     */
    JLabel seconds;
    /**
     * <p>ampm</p>
     * Displays am/pm.
     */
    JLabel ampmLabel;
    /**
     * <p>dayOfTheYear</p>
     * Displays day of the year.
     */
    JLabel dayOfTheYear;
    /**
     * <p>date</p>
     * Current date.
     */
    Date date = new Date();
    private JLabel yearLabel;
    private JLabel monthLabel;
    private JLabel dayNumberLabel;
    private JLabel dayLabel;
    
    /**
     * <p>Clock</p>
     * Constructor.
     */
    public Clock() {
        super("Clock");
        setLayout(new MigLayout("insets 0"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(false);
        setFocusableWindowState(false);
        setUndecorated(true);
        setBackground(CLEAR);
        
        currentTimeFormat = TIME_FORMAT;
        
        JPanel panel = new JPanel(THEME.getLayout());
        panel.setBackground(CLEAR);
        Timer clockTimer = new Timer(100, new UpdateTimeListener());
        
        switch (THEME_NAME) {
        case CLASSIC_DIGITAL:
            final Font CLOCK_FONT = THEME.getFont("clockFont");
            final Font SECONDS_FONT = THEME.getFont("smallFont");
            final Font DATE_FONT = THEME.getFont("textFont");
            AffineTransform affinetransform = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
            Dimension clockSize = new Dimension((int) CLOCK_FONT.getStringBounds("00:00", frc).getWidth() + 10, (int) getFontMetrics(CLOCK_FONT).getHeight());
            Dimension secondsSize = new Dimension((int) SECONDS_FONT.getStringBounds("00", frc).getWidth() + 10, (int) getFontMetrics(SECONDS_FONT).getHeight());
            timeLabel = new JLabel(TIME_FORMAT.format(date), SwingConstants.RIGHT);
            timeLabel.setFont(CLOCK_FONT);
            timeLabel.setForeground(CLOCK_FONT_COLOR);
            timeLabel.addMouseListener(new ClockMouseAdapter());
            timeLabel.setMinimumSize(clockSize);
            timeLabel.setOpaque(true);
            timeLabel.setBackground(new Color(0, 0, 0, 1));
            
            seconds = new JLabel(SECONDS_FORMAT.format(date), SwingConstants.RIGHT);
            seconds.setFont(SECONDS_FONT);
            seconds.setForeground(CLOCK_FONT_COLOR);
            seconds.setMinimumSize(secondsSize);
            
            ampmLabel = new JLabel(AMPM_FORMAT.format(date), SwingConstants.RIGHT);
            ampmLabel.setFont(SECONDS_FONT);
            ampmLabel.setForeground(CLOCK_FONT_COLOR);
            ampmLabel.setMinimumSize(secondsSize);
            
            dayOfTheYear = new JLabel(DAY_OF_THE_YEAR_FORMAT.format(date), SwingConstants.RIGHT);
            dayOfTheYear.setFont(DATE_FONT);
            dayOfTheYear.setForeground(CLOCK_FONT_COLOR);
            
            JPanel rightPanel = new JPanel(THEME.getRightPanelLayout());
            rightPanel.setBackground(CLEAR);
            
            panel.add(timeLabel, "align right top");
            rightPanel.add(seconds, "align right, wrap");
            rightPanel.add(ampmLabel, "align right");
            panel.add(rightPanel);
            panel.add(dayOfTheYear, "span 2, align right");
            break;
        case MICHROMA:
            dayLabel = new JLabel("", SwingConstants.RIGHT);
            dayNumberLabel = new JLabel("", SwingConstants.LEFT);
            monthLabel = new JLabel("", SwingConstants.RIGHT);
            yearLabel = new JLabel("", SwingConstants.LEFT);
            timeLabel = new JLabel("", SwingConstants.RIGHT);
            ampmLabel = new JLabel("", SwingConstants.LEFT);
            updateTime();
            dayLabel.setFont(THEME.getFont("font"));
            dayNumberLabel.setFont(THEME.getFont("font"));
            monthLabel.setFont(THEME.getFont("font"));
            yearLabel.setFont(THEME.getFont("font"));
            timeLabel.setFont(THEME.getFont("font"));
            ampmLabel.setFont(THEME.getFont("font"));
            dayLabel.setForeground(CLOCK_FONT_COLOR);
            dayNumberLabel.setForeground(CLOCK_FONT_COLOR);
            monthLabel.setForeground(CLOCK_FONT_COLOR);
            yearLabel.setForeground(CLOCK_FONT_COLOR);
            timeLabel.setForeground(CLOCK_FONT_COLOR);
            ampmLabel.setForeground(CLOCK_FONT_COLOR);
            panel.add(dayLabel);
            panel.add(dayNumberLabel);
            panel.add(monthLabel);
            panel.add(yearLabel);
            panel.add(timeLabel);
            panel.add(ampmLabel);
            break;
        }
        
        getContentPane().add(panel);
        pack();
        setLocation(new Point(WIN_X, WIN_Y));
        setVisible(true);
        clockTimer.start();
    }
    
    /**
     * <p>UpdateTimeListener.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
    private class UpdateTimeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            updateTime();
        }
    }
    
    /**
     * <p>ClockMouseAdapter.</p>
     * @author Stephen Cheng
     * @version 1.0
     */
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
    
    /**
     * <p>updateTime</p>
     * Updates the time.
     */
    private void updateTime() {
        date = new Date();
        switch (THEME_NAME) {
        case CLASSIC_DIGITAL:
            timeLabel.setText(currentTimeFormat.format(date));
            seconds.setText(SECONDS_FORMAT.format(date));
            ampmLabel.setText(AMPM_FORMAT.format(date));
            dayOfTheYear.setText(DAY_OF_THE_YEAR_FORMAT.format(date));
            break;
        case MICHROMA:
            String day = DAY_NAME.format(date);
            String dayNumber = DAY_NUMBER.format(date);
            String month = MONTH.format(date);
            String year = YEAR.format(date);
            String time = MILITARY_TIME_FORMAT.format(date);
            String ampm = AMPM_FORMAT.format(date);
            if (TEXT_CASE.equalsIgnoreCase("UPPER")) {
                day = day.toUpperCase();
                dayNumber = dayNumber.toUpperCase();
                month = month.toUpperCase();
                year = year.toUpperCase();
                time = time.toUpperCase();
                ampm = ampm.toUpperCase();
            } else if (TEXT_CASE.equalsIgnoreCase("LOWER")) {
                day = day.toLowerCase();
                dayNumber = dayNumber.toLowerCase();
                month = month.toLowerCase();
                year = year.toLowerCase();
                time = time.toLowerCase();
                ampm = ampm.toLowerCase();
            }
            dayLabel.setText(day);
            dayNumberLabel.setText(dayNumber);
            monthLabel.setText(month);
            yearLabel.setText(year);
            timeLabel.setText(time);
            ampmLabel.setText(ampm);
        }
        repaint();
    }
    
    /**
     * <p>main</p>
     * Entry point for the JVM.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new Clock();
    }
}
