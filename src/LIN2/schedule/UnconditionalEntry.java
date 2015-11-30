package LIN2.schedule;

import LIN2.frame.Frame;

public class UnconditionalEntry extends Entry {
    private Frame frame;

    public UnconditionalEntry(Frame frame, float frameTime) {
        super(frameTime);
        this.frame = frame;

    }

    public Frame getFrame() {
        return frame;
    }
}
