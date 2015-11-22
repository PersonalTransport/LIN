package LIN.bitrate;

public class AutomaticBitrate extends Bitrate {
    private float max;
    private float min;

    public AutomaticBitrate() {
        min = 0.0f;
        max = 20.0f;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMax() {
        return max;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMin() {
        return min;
    }

    @Override
    public String toString() {
        return "["+min+" kpbs, "+max+" kbps]";
    }
}
