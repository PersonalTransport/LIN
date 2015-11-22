
public class FixedBitrate extends Bitrate {
    private float bitrate;

    public FixedBitrate(float bitrate) {

        this.bitrate = bitrate;
    }

    public float getBitrate() {
        return bitrate;
    }

    public void setBitrate(float bitrate) {
        this.bitrate = bitrate;
    }
}
