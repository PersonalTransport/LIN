package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private Node node;
    private boolean publishes;
    private String name;
    private int length;
    private double minPeriod;
    private double maxPeriod;
    private Frame eventTriggeredFrame;
    private List<Signal> signals;

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

    public boolean getPublishes() {
        return this.publishes;
    }

    public void setPublishes(boolean publishes) {
        this.publishes = publishes;
    }

    public boolean getSubscribes() {
        return !this.publishes;
    }

    public void setSubscribes(boolean subscribes) {
        this.publishes = !subscribes;
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
        String s = (getPublishes() ? "publish " : "subscribe ") + getName() + " {\n";
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
