import java.util.ArrayList;

public class SelectBitrate extends Bitrate {
    private ArrayList<Float> bitrates;

    public SelectBitrate() {
        bitrates = new ArrayList<>(bitrates);
    }

    public void add(float bitrate) {
        bitrates.add(bitrate);
    }

    public void remove(float bitrate) {
        bitrates.remove(bitrate);
    }
}
