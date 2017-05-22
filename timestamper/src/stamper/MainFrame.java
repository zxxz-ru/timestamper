/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamper;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.ListEditor;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author zxxz
 */
public class MainFrame extends JFrame {
  //fields

  private JTextField input, output;
  private JLabel yearLabel, monthLabel, dayLabel, hourLabel,
          minuteLabel, inputLabel, outputLabel, layoutsLabel;
  private JSpinner year = new JSpinner();
  private JSpinner month = new JSpinner();
  private JSpinner day = new JSpinner();
  private JSpinner hour = new JSpinner();
  private JSpinner minute = new JSpinner();
  private JComboBox layouts;
  private JButton button;
  private final JLabel[] labels = {yearLabel, monthLabel, dayLabel, hourLabel, minuteLabel};
  private final JSpinner[] spinners = {year, month, day, hour, minute};
  private JPanel[] panels = new JPanel[5];
  private SimpleDateFormat dateFormat = new SimpleDateFormat();
  SpinnerDateModel spm;
  CyclingSpinnerNumberModel dayModel;
  SpinnerListModel monthModel;

  public MainFrame() {
    initFrame();
    initComponents();
    this.add(initTopPanel());
    this.add(Box.createVerticalGlue());
    for (byte i = 0; i < panels.length; i++) {
      this.add(panels[i]);
    }//for
    this.add(Box.createVerticalGlue());
    this.add(initBottomPanel());
    this.add(Box.createVerticalGlue());
  }

  private void initFrame() {
    Dimension size = new Dimension(230, 420);
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
    Border padding = new EmptyBorder(8, 8, 8, 8);
    container.setBorder(padding);
    this.setContentPane(container);
    this.setTitle("TimeStamper");
    this.setMaximumSize(size);
    this.setSize(size);
    this.setMaximumSize(size);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.locateInCentre(size);
    this.setLookAndFeel("Nimbus");
  }//initFrames

  private void initComponents() {
    Dimension spnSize = new Dimension(110, 26);
    Dimension labSize = new Dimension(150, 16);
    Dimension textBoxSize = new Dimension(180, 25);
    Font spinnerFont = new Font(Font.SERIF, Font.PLAIN, 14);
    Font labelFont = new Font(Font.SERIF, Font.BOLD, 12);

    //get calendar values
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeInMillis(System.currentTimeMillis());
    long time = cal.getTimeInMillis();//for output text
    //set current time values
    int presYear = cal.get(Calendar.YEAR);
    int presMonth = cal.get(Calendar.MONTH);
    int presDay = cal.get(Calendar.DAY_OF_MONTH);
    int presHour = cal.get(Calendar.HOUR_OF_DAY);
    int presMinute = cal.get(Calendar.MINUTE);
    //set year spinner
    cal.set(Calendar.YEAR, -200);
    int minYear = cal.get(Calendar.YEAR);
    cal.set(Calendar.YEAR, +400);
    int maxYear = cal.get(Calendar.YEAR);
    CyclingSpinnerNumberModel yearModel = new CyclingSpinnerNumberModel(presYear, presYear - 200, presYear + 200, 1);
    year.setModel(yearModel);
    year.setEditor(new NumberEditor(year, "0000"));
    //set hour spinner
    hour.setModel(new CyclingSpinnerNumberModel(presHour, 0, 23, 1));
    hour.setEditor(new NumberEditor(hour, "00"));
    //set minute spinner
    minute.setModel(new CyclingSpinnerNumberModel(presMinute, 0, 59, 1));
    minute.setEditor(new NumberEditor(minute, "00"));
    //set day
    dayModel = new CyclingSpinnerNumberModel(presDay, 1, 31, 1);
    day.setModel(dayModel);
    day.setEditor(new NumberEditor(day, "00"));
    day.addChangeListener(daySpinnerListener);
    //set month spinner
    String[] months = {"January", "February", "Marth", "April", "May", "June",
      "July", "August", "September", "October", "November", "December"};
    monthModel = new CyclingSpinnerListModel(months);
    month.setModel(monthModel);
    monthModel.setValue(months[presMonth]);

    String[] texts = {"Year", "Month", "Day", "Hour", "Minute"};
    Dimension splDimen = new Dimension(100, 16);
    for (byte b = 0; b < 5; b++) {
      labels[b] = new JLabel();
      labels[b].setFont(labelFont);
      labels[b].setText(texts[b]);
      labels[b].setSize(splDimen);
      labels[b].setPreferredSize(splDimen);
      labels[b].setMaximumSize(splDimen);
      //spinners[b] = new JSpinner();
      spinners[b].setFont(spinnerFont);
      //spinners[b].setValue(date);
      spinners[b].setFocusCycleRoot(true);
      spinners[b].setSize(spnSize);
      spinners[b].setPreferredSize(spnSize);
      spinners[b].setMaximumSize(spnSize);
      JPanel pan = this.createSpinnerPanel(labels[b], spinners[b]);
      panels[b] = pan;
    }//for

    //inputbox
    input = new JTextField();
    input.setSize(textBoxSize);
    input.setPreferredSize(textBoxSize);
    //input.setMaximumSize(textBoxSize);
    input.setText("");
    input.setFont(spinnerFont);
    input.setEditable(true);
    input.addActionListener(actionListener);
    //input.setAlignmentX(Component.CENTER_ALIGNMENT);
    inputLabel = new JLabel("Timestamp to process");
    inputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //combobox
    Vector boxValues = getLFs();
    layouts = new JComboBox(boxValues);
    if (boxValues.contains("Nimbus")) {
      int i = boxValues.indexOf("Nimbus");
      layouts.setSelectedIndex(i);
    } else {
      int i = boxValues.indexOf(UIManager.getSystemLookAndFeelClassName());
      layouts.setSelectedIndex(i);
    }
    layouts.setSize(textBoxSize);
    layouts.setPreferredSize(textBoxSize);
    layouts.setMaximumSize(textBoxSize);
    layouts.setFont(spinnerFont);
    layouts.setMaximumRowCount(4);
    layouts.addActionListener(actionListener);
    layoutsLabel = new JLabel("Change Look&Feel.");
    layoutsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //outbox
    output = new JTextField();
    output.setSize(textBoxSize);
    output.setPreferredSize(textBoxSize);
    output.setMaximumSize(textBoxSize);
    output.setAlignmentX(Component.LEFT_ALIGNMENT);
    output.setFont(spinnerFont);
    output.setEditable(false);
    output.setText("" + time);
    outputLabel = new JLabel(printDate(time) + "     " + printTime(time));
    outputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //button
    button = new JButton("Get Stamp");
    button.addActionListener(actionListener);

  }//initComponents

  private JPanel initTopPanel() {
    Dimension size = new Dimension(220, 100);
    JPanel tp = new JPanel();
    //tp.setLayout(new BoxLayout(tp, BoxLayout.PAGE_AXIS););
    tp.setMinimumSize(size);
    tp.setSize(size);
    tp.setPreferredSize(size);
    tp.add(this.layoutsLabel);
    tp.add(this.layouts);
    tp.add(this.inputLabel);
    tp.add(this.input);
    return tp;
  }//initTopPanel

  private JPanel createSpinnerPanel(JLabel label, JSpinner spin) {
    Dimension size = new Dimension(230, 26);
    JPanel sp = new JPanel();
    sp.setLayout(new BoxLayout(sp, BoxLayout.LINE_AXIS));
    sp.setMaximumSize(size);
    sp.setSize(size);
    sp.setPreferredSize(size);
    sp.setMaximumSize(new Dimension(230, 38));
    sp.add(Box.createHorizontalGlue());
    sp.add(label);
    sp.add(Box.createHorizontalStrut(8));
    sp.add(spin);
    sp.add(Box.createHorizontalGlue());
    return sp;
  }//initSpinnerPanel

  private JPanel initBottomPanel() {
    Dimension size = new Dimension(210, 90);
    JPanel bp = new JPanel();
    bp.setMaximumSize(size);
    bp.setSize(size);
    bp.setPreferredSize(size);
    bp.add(this.outputLabel);
    bp.add(this.output);
    bp.add(this.button);
    return bp;
  }//initBottomPanel

  private void setLookAndFeel(String name) {
    try {
      if (!name.isEmpty() && name != null) {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if (name.equals(info.getName())) {
            UIManager.setLookAndFeel(info.getClassName());
            break;
          } else {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          }//else
        }//for
      }
    }//try
    catch (Exception e) {
      e.printStackTrace();
    }//catch
    SwingUtilities.updateComponentTreeUI(this);
    this.repaint();

  }//setLookAndFeel

  private Vector<String> getLFs() {
    Vector<String> names = new Vector<String>(4);
    try {

      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        names.add(info.getName());
      }//for

    }//try
    catch (Exception e) {
      e.printStackTrace();
    }//catch
    return names;
  }//getLFs

  public String printDate(long time) {
    dateFormat.applyPattern("EEE, MMM d, yyyy");
    return dateFormat.format(new Date(time));
  }//printDate

  /**
   * 
   * @param time
   * @return String representation of parameter time as time of day.
   */
  public String printTime(long time) {
    dateFormat.applyPattern("hh:mm a");
    return dateFormat.format(new Date(time));
  }//printTime

  private void locateInCentre(Dimension size) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2);
  }//locateInCentre

  private int convertMonth(String m) {
    Enum mon = Months.valueOf(m);
    return mon.ordinal();
  }//convertMonth
  ActionListener actionListener = new ActionListener() {

    private Object[] months = Months.values();
    private GregorianCalendar cal = new GregorianCalendar();

    @Override
    public void actionPerformed(ActionEvent ae) {
      Object source = ae.getSource();
      if (source.equals(layouts)) {
        String lay = (String) layouts.getSelectedItem();
        setLookAndFeel(lay);
      }//layouts
      else if (source.equals(input)) {
        String value = input.getText().trim();
        if (value != null && !value.isEmpty()) {
          value = value.replaceAll("\\D", "");
          value.trim();
          input.setText(value);
          long time = 0;
          try {
            time = Long.parseLong(value);
          } catch (NumberFormatException e) {
            Object[] bt = {"OK"};
            JOptionPane.showOptionDialog(null, "The number provided is not a valid Timestamp!",
                    "Wrong input!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, null, bt);
            return;
          }//catch

          cal.setTimeInMillis(time);
          int y = cal.get(Calendar.YEAR);
          int m = cal.get(Calendar.MONTH);
          int d = cal.get(Calendar.DAY_OF_MONTH);
          int h = cal.get(Calendar.HOUR_OF_DAY);
          int mnt = cal.get(Calendar.MINUTE);
          Date date = new Date(time);
          inputLabel.setText(printDate(time) + "     " + printTime(time));
          //set spinners    
          year.setValue(y);

          Component editor = month.getEditor();
          if (editor instanceof JSpinner.ListEditor) {
            ((ListEditor) editor).getTextField().setText(months[m] + "");
            monthModel.setValue(months[m] + "");
          }
          // 
          day.setValue(d);
          hour.setValue(h);
          minute.setValue(mnt);//1327686097049

        }//legitimate value
      }//input
      else if (source.equals(button)) {
        int y = (Integer) year.getValue();
        int d = (Integer) day.getValue();
        int h = (Integer) hour.getValue();
        int mnt = (Integer) minute.getValue();
        String m = (String) month.getValue();
        int mon = convertMonth(m);
        cal.set(y, mon, d, h, mnt);
        long t = cal.getTimeInMillis();
        output.setText(t + "");
        outputLabel.setText(printDate(t) + "     " + printTime(t));

      }//actionPerformed
    }
  };
  //day listener
  ChangeListener daySpinnerListener = new ChangeListener() {

    int y, mon, d;
    int numDays = 0;

    @Override
    public void stateChanged(ChangeEvent e) {
      Object source = e.getSource();
      y = (Integer) year.getValue();
      String monVal = (String) month.getValue();
      mon = convertMonth(monVal);
      d = (Integer) day.getValue();
      // if(source.equals(month)){
      GregorianCalendar cal = new GregorianCalendar();
      cal.set(y, mon, d);
      switch (mon) {
        case 0://Jan
        case 2://Mar e.t.c
        case 4:
        case 6:
        case 7:
        case 9:
        case 11:
          dayModel.setMaximum(31);
          dayModel.setLast(31);
          break;
        case 3:
        case 5:
        case 8:
        case 10:
          dayModel.setMaximum(30);
          dayModel.setLast(30);
          break;
        case 1:
          if (cal.isLeapYear(y)) {
            dayModel.setMaximum(29);
            dayModel.setLast(29);
          } else {
            dayModel.setMaximum(28);
            dayModel.setLast(28);
          }
          break;
      }//swith

    }//method
  };//class/method
}//class
