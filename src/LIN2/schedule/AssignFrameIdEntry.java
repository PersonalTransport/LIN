package LIN2.schedule;

import LIN2.frame.Frame;
import LIN2.Slave;

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
