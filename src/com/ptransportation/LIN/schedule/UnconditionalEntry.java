package com.ptransportation.LIN.schedule;

import com.ptransportation.LIN.frame.Frame;

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
