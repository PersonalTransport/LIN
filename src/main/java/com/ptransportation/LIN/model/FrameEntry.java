package com.ptransportation.LIN.model;

public class FrameEntry extends ScheduleTableEntry {
    private Frame frame;

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    @Override
    public String toString() {
        return frame.getName()+" delay " + getFrameTime() + " ms;";
    }
}