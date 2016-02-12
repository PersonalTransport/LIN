package com.ptransportation.LIN.model;

public class AssignFrameIdEntry extends ScheduleTableEntry {
    private Slave slave;
    private Frame frame;

    public Slave getSlave() {
        return slave;
    }

    public void setSlave(Slave slave) {
        this.slave = slave;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    @Override
    public String toString() {
        return "AssignFrameId { " + slave.getName() + ", " + frame.getName() + " } delay " + getFrameTime() + " ms;";
    }
}
