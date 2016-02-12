package com.ptransportation.LIN.model;

public class NadRange extends NadSet {
    private int minValue;
    private int maxValue;

    public int getMinValue() {
        return this.minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public String toString() {
        return "0x" + Integer.toHexString(getMinValue()) + " to 0x" + Integer.toHexString(getMaxValue());
    }
}
