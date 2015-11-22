package LIN.encoding;

public class PhysicalEncodedRange extends EncodedValue {
    private int minValue;
    private int maxValue;
    private float scale;
    private float offset;
    private String textInfo;

    public PhysicalEncodedRange(int minValue, int maxValue, float scale, float offset, String textInfo) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.scale = scale;
        this.offset = offset;
        this.textInfo = textInfo;
    }
}
