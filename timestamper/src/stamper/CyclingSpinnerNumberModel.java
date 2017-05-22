/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamper;

import javax.swing.SpinnerNumberModel;

/**
 *
 * @author zxxz
 */
public class CyclingSpinnerNumberModel extends SpinnerNumberModel {

  private int first, last;

  public CyclingSpinnerNumberModel(int value, int minimum, int maximum, int stepSize) {
    super(value, minimum, maximum, stepSize);
    this.first = minimum;
    this.last = maximum;
  }//constructor

  private int getFirst() {
    return first;
  }

  private int getLast() {
    return last;
  }

  public void setFirst(int first) {
    this.first = first;
  }

  public void setLast(int last) {
    this.last = last;
  }

  @Override
  public Object getNextValue() {
    Object value = super.getNextValue();
    if (value == null) {
      value = this.getFirst();
    }
    return value;
  }

  @Override
  public Object getPreviousValue() {
    Object value = super.getPreviousValue();
    if (value == null) {
      value = this.getLast();
    }
    return value;
  }
}//class
