package com.ptransportation.LIN.model;

public class AutomaticBitrate extends Bitrate {
    private double minValue;
    private double maxValue;

    public double getMinValue() {
        return this.minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public String toString() {
        return "automatic min " + minValue + " kbps max " + maxValue + " kbps";
    }
}
