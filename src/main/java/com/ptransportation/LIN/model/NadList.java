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

    @Override
    public String toString() {
        String s = "";
        for(int i=0;i<values.size()-1;++i)
            s += "0x"+Integer.toHexString(values.get(i)) + ", ";
        return s + "0x"+Integer.toHexString(values.get(values.size()-1));
    }
}
