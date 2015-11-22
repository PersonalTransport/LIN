package LIN.signal;

import LIN.encoding.Encoding;

public class Signal {
    private String name;
    private int size;
    private int offset;
    private Encoding encoding;
    private SignalValue initialValue;

    public Signal(String name, int size, int offset) {
        this.name = name;
        this.size = size;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getOffset() {
        return offset;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setInitialValue(SignalValue initialValue) {
        this.initialValue = initialValue;
    }

    public SignalValue getInitialValue() {
        return initialValue;
    }
}
