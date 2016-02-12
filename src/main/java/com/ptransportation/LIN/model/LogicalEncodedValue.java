package com.ptransportation.LIN.model;

public class LogicalEncodedValue extends EncodedValue {
    private int value;
    private String textInfo;

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTextInfo() {
        return this.textInfo;
    }

    public void setTextInfo(String textInfo) {
        this.textInfo = textInfo;
    }

    @Override
    public String toString() {
        return "logical_value, " + value + ((getTextInfo() != null) ? ", \"" + getTextInfo() + "\"" : "");
    }
}
