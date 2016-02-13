package com.ptransportation.LIN.model;

public class ScheduleTableEntry {
    private ScheduleTable scheduleTable;
    private double frameTime;

    public ScheduleTable getScheduleTable() {
        return scheduleTable;
    }

    public void setScheduleTable(ScheduleTable scheduleTable) {
        this.scheduleTable = scheduleTable;
    }

    public double getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(double frameTime) {
        this.frameTime = frameTime;
    }
}
