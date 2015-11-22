package LIN.bitrate;

import java.util.ArrayList;


public class SelectBitrate extends Bitrate {
    private ArrayList<Float> viableBitrates;

    public SelectBitrate() {
        viableBitrates = new ArrayList<>();
    }

    public SelectBitrate(ArrayList<Float> bitrates) {
        viableBitrates = bitrates;
    }

    public void add(Float viableBitrate) {
        viableBitrates.add(viableBitrate);
    }

    public void remove(Float viableBitrate) {
        viableBitrates.remove(viableBitrate);
    }
}
