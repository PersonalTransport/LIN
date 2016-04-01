package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public class Master extends Node {
    private double timebase;
    private double jitter;
    private List<Slave> slaves;
    private List<ScheduleTable> scheduleTables;

    public Master(String name) {
        super(name);
        this.slaves = new ArrayList<Slave>();
        this.scheduleTables = new ArrayList<ScheduleTable>();
    }

    public double getTimebase() {
        return this.timebase;
    }

    public void setTimebase(double timebase) {
        this.timebase = timebase;
    }

    public double getJitter() {
        return this.jitter;
    }

    public void setJitter(double jitter) {
        this.jitter = jitter;
    }

    public List<Slave> getSlaves() {
        return this.slaves;
    }

    public List<ScheduleTable> getScheduleTables() {
        return this.scheduleTables;
    }

    /*@Override
    public List<Frame> getFrames() {
        List<Frame> allFrames = new ArrayList<Frame>();
        allFrames.addAll(super.getPublishFrames());
        for (Slave slave : slaves) {
            allFrames.addAll(slave.getPublishFrames());
        }
        return allFrames;
    }*/

    @Override
    public List<Frame> getSubscribeFrames() {
        ArrayList<Frame> frames = new ArrayList<>();
        frames.addAll(super.getSubscribeFrames());
        for(Slave slave:this.slaves)
            frames.addAll(slave.getPublishFrames());
        return frames;
    }

    @Override
    public String toString() {
        String s = "";
        s += "node" + " " + getName() + " {\n";

        s += "\tgeneral {\n";
        s += "\t\tLIN_protocol_version = \"" + getProtocolVersion() + "\";\n";
        s += "\t\tsupplier = 0x" + Integer.toHexString(getSupplier()) + ";\n";
        s += "\t\tfunction = 0x" + Integer.toHexString(getFunction()) + ";\n";
        s += "\t\tvariant = " + getVariant() + ";\n";
        s += "\t\tbitrate = " + getBitrate().toString() + ";\n";
        s += "\t\ttimebase = " + getTimebase() + " ms;\n";
        s += "\t\tjitter = " + getJitter() + " ms;\n";
        s += "\t}\n";

        s += "\tslaves {\n";
        for (Slave slave : getSlaves())
            s += "\t\t" + slave.getName() + ";\n";
        s += "\t}\n";

        s += "\tframes {\n";
        for (Frame frame : getFrames())
            s += "\t\t" + frame.toString().replaceAll("\n", "\n\t\t") + "\n";
        s += "\t}\n";

        s += "\tencoding {\n";
        for (Encoding encoding : getEncodings())
            s += "\t\t" + encoding.toString().replaceAll("\n", "\n\t\t") + "\n";
        s += "\t}\n";

        s += "\tschedule_tables {\n";
        for (ScheduleTable scheduleTable : getScheduleTables())
            s += "\t\t" + scheduleTable.toString().replaceAll("\n", "\n\t\t") + "\n";
        s += "\t}\n";

        if (getFreeText() != null) {
            s += "\tfree_text {\n";
            s += "\t\t\"" + getFreeText() + "\"\n";
            s += "\t}\n";
        }

        s += "}";

        return s;
    }

}
