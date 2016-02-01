package com.ptransportation.LIN.bitrate;

import java.util.ArrayList;


public class SelectBitrate extends Bitrate {
    private ArrayList<Float> bitrates;

    public SelectBitrate() {
        bitrates = new ArrayList<Float>();
    }

    public SelectBitrate(ArrayList<Float> bitrates) {
        this.bitrates = bitrates;
    }

    public void add(float bitrate) {
        bitrates.add(bitrate);
    }

    public ArrayList<Float> getBitrates() {
        return bitrates;
    }

    public void remove(float bitrate) {
        bitrates.remove(bitrate);
    }
}
