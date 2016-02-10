package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public class Master extends Node {
    private double timebase;
    private double jitter;
    private List<Slave> slaves;
    private List<ScheduleTable> scheduleTables;

    public Master() {
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
}
