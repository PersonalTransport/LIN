package com.ptransportation.LIN.model;

public class ScalarSignalValue extends SignalValue {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "0x" + Integer.toHexString(value);
    }
}
