package LIN2.encoding;

public class LogicalEncodedValue extends EncodedValue {
    private int value;
    private String textInfo;

    public LogicalEncodedValue(int value,String textInfo) {
        this.value = value;
        this.textInfo = textInfo;
    }

    public int getValue() {
        return value;
    }

    public String getTextInfo() {
        return textInfo;
    }
}