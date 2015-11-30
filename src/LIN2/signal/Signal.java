package LIN2.signal;

import LIN2.encoding.Encoding;
import LIN2.frame.UnconditionalFrame;

public class Signal {
    private String name;
    private int size;
    private int offset;
    private Encoding encoding;
    private SignalValue initialValue;
    private UnconditionalFrame frame;

    public Signal(String name) {
        this.name = name;
        this.size = Integer.MIN_VALUE;
        this.offset = Integer.MIN_VALUE;
        this.frame = null;
    }

    public String getName() {
        return name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setFrame(UnconditionalFrame frame) {
        if(this.frame != frame) {
            if (this.frame != null)
                this.frame.removeSignal(this);
            this.frame = frame;
            this.frame.addSignal(this);
        }
    }

    public UnconditionalFrame getFrame() {
        return frame;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public void setInitialValue(SignalValue initialValue) {
        this.initialValue = initialValue;
    }

    public SignalValue getInitialValue() {
        return initialValue;
    }
}
