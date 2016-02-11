package com.ptransportation.LIN.model;

public class FrameReference extends Frame implements Reference {
    private Frame linked;

    public FrameReference(String name) {
        super(name);
    }

    @Override
    public void setName(String name) {
        if (linked != null && !linked.getName().equals(name))
            linked = null;
        super.setName(name);
    }

    public Frame getLinked() {
        return linked;
    }

    public void link(Frame frame) {
        this.linked = frame;
    }
}
