package com.ptransportation.LIN.schedule;

import com.ptransportation.LIN.Slave;
import com.ptransportation.LIN.frame.Frame;

public class AssignFrameIdEntry extends Entry {
    private Slave slave;
    private Frame frame;

    public AssignFrameIdEntry(Slave slave, Frame frame, float frameTime) {
        super(frameTime);
        this.slave = slave;
        this.frame = frame;
        // TODO check that the slave lists the frame.
    }

    public Slave getSlave() {
        return slave;
    }

    public Frame getFrame() {
        return frame;
    }
}
