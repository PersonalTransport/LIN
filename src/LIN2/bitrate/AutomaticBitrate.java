package LIN2.bitrate;

public class AutomaticBitrate extends Bitrate {
    private Float max;
    private Float min;

    public void setMax(Float max) {
        this.max = max;
    }

    public float getMax() {
        return max;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public float getMin() {
        return min;
    }
}
