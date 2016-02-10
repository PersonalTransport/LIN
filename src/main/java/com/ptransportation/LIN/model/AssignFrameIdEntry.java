package com.ptransportation.LIN.model;

public class AssignFrameIdEntry extends ScheduleTableEntry {
    private Node node;
    private Frame frame;

    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Frame getFrame() {
        return this.frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }
}
