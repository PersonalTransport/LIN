package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    public static int getFramePIDFromID(int id) {
        return (~((id << 6) ^ (id << 4) ^ (id << 3) ^ (id << 2)) & 0x80) |
                (((id << 6) ^ (id << 5) ^ (id << 4) ^ (id << 2)) & 0x40) |
                (id & 0x3F);
    }

    private Node node; // TODO is this needed?
    private Node publisher;
    private String name;
    private int length;
    private double minPeriod;
    private double maxPeriod;
    private Frame eventTriggeredFrame;
    private List<Signal> signals;

    // TODO minPeriod?
    // TODO maxPeriod?

    public Frame(String name) {
        this.name = name;
        this.signals = new ArrayList<Signal>();
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getPublisher() {
        return this.publisher;
    }

    public void setPublisher(Node publisher) {
        this.publisher = publisher;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return this.length;

    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getMinPeriod() {
        return this.minPeriod;
    }

    public void setMinPeriod(double minPeriod) {
        this.minPeriod = minPeriod;
    }

    public double getMaxPeriod() {
        return this.maxPeriod;
    }

    public void setMaxPeriod(double maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    public Frame getEventTriggeredFrame() {
        return this.eventTriggeredFrame;
    }

    public void setEventTriggeredFrame(Frame eventTriggeredFrame) {
        this.eventTriggeredFrame = eventTriggeredFrame;
    }

    public List<Signal> getSignals() {
        return this.signals;
    }

    @Override
    public String toString() {
        // TODO (getPublishes() ? "publish " : "subscribe ")
        String s = getName() + " {\n";
        s += "\tlength = " + getLength() + ";\n";
        s += "\tmin_period = " + getMinPeriod() + ";\n";
        s += "\tmax_period = " + getMaxPeriod() + ";\n";
        if (getEventTriggeredFrame() != null)
            s += "\tevent_triggered_frame = " + getEventTriggeredFrame().getName() + ";\n";
        s += "\tsignals {\n";
        for (Signal signal : getSignals())
            s += "\t\t" + signal.toString().replaceAll("\n", "\n\t\t") + "\n";
        s += "}\n";
        return s;
    }
}
