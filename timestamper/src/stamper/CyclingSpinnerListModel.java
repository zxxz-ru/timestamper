/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamper;

import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

/**
 *
 * @author zxxz
 */
public class CyclingSpinnerListModel extends SpinnerListModel {
    Object firstValue, lastValue;

    public CyclingSpinnerListModel(Object[] values) {
        super(values);
        firstValue = values[0];
        lastValue = values[values.length - 1];
    }

    public Object getNextValue() {
        Object value = super.getNextValue();
        if (value == null) {
            value = firstValue;
        }
        return value;
    }

    public Object getPreviousValue() {
        Object value = super.getPreviousValue();
        if (value == null) {
            value = lastValue;
        }
        return value;
    }
}