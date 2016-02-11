package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectBitrate extends Bitrate {
    private List<Double> values;

    public  SelectBitrate() {
        this.values = new ArrayList<Double>();
    }

    public List<Double> getValues() {
        return this.values;
    }
}
