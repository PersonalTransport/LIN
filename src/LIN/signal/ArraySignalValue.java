package LIN.signal;

import java.util.ArrayList;

public class ArraySignalValue extends SignalValue {
    private ArrayList<Integer> values;

    public ArraySignalValue(ArrayList<Integer> values) {
        this.values = values;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }
}
