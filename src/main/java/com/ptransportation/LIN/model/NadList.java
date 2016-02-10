package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public class NadList extends NadSet {

    private List<Integer> values;

    public NadList() {
        this.values = new ArrayList<Integer>();
    }

    public List<Integer> getValues() {
        return this.values;
    }

}
