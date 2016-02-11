package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public class ArraySignalValue extends SignalValue {
    private List<Integer> values;

    public ArraySignalValue() {
        this.values = new ArrayList<Integer>();
    }

    public List<Integer> getValues() {
        return this.values;
    }
}
