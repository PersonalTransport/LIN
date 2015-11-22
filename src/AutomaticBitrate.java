
public class AutomaticBitrate extends Bitrate {
    private float minimum;
    private float maximum;

    public void setMinimum(float minimum) {
        this.minimum = minimum;
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }

    public float getMaximum() {
        return maximum;
    }
}
