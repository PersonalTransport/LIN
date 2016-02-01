package com.ptransportation.LIN.bitrate;

public class FixedBitrate extends Bitrate {
    private float value;

    public FixedBitrate(float bitrate) {
        this.value = bitrate;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
