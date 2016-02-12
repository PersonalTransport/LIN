package com.ptransportation.LIN.model;

public class PhysicalEncodedValue extends EncodedValue {
    private int minValue;
    private int maxValue;
    private double scale;
    private double offset;
    private String textInfo;

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

    public double getScale() {
        return this.scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getOffset() {
        return this.offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public String getTextInfo() {
        return this.textInfo;
    }

    public void setTextInfo(String textInfo) {
        this.textInfo = textInfo;
    }

    @Override
    public String toString() {
        return "physical_value, " + minValue + ", " + maxValue + ", " + scale + ", " + offset + ((getTextInfo() != null) ? ", \"" + getTextInfo() + "\"" : "");
    }

}
