package com.ptransportation.LIN.encoding;

public class PhysicalEncodedRange extends EncodedValue {
    private int min;
    private int max;
    private float scale;
    private float offset;
    private String textInfo;

    public PhysicalEncodedRange(int minValue, int maxValue, float scale, float offset, String textInfo) {
        this.min = minValue;
        this.max = maxValue;
        this.scale = scale;
        this.offset = offset;
        this.textInfo = textInfo;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public float getScale() {
        return scale;
    }

    public float getOffset() {
        return offset;
    }

    public String getTextInfo() {
        return textInfo;
    }
}
