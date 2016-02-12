package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public class SelectBitrate extends Bitrate {
    private List<Double> values;

    public SelectBitrate() {
        this.values = new ArrayList<Double>();
    }

    public List<Double> getValues() {
        return this.values;
    }

    @Override
    public String toString() {
        String s = "select { ";
        for (int i = 0; i < values.size() - 1; ++i)
            s += values.get(i) + ", ";
        return s + values.get(values.size() - 1) + " }";
    }
}
