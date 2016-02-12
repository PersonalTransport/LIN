package com.ptransportation.LIN.model;

public class AssignFrameIdRangeEntry extends ScheduleTableEntry {
    private Slave slave;
    private int startIndex;
    private boolean lookupIDs;
    private int PID0;
    private int PID1;
    private int PID2;
    private int PID3;

    public AssignFrameIdRangeEntry() {
        this.lookupIDs = true;
    }

    public Slave getSlave() {
        return slave;
    }

    public void setSlave(Slave slave) {
        this.slave = slave;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public boolean isLookupIDs() {
        return lookupIDs;
    }

    public void setLookupIDs(boolean lookupIDs) {
        this.lookupIDs = lookupIDs;
    }

    public int getPID0() {
        return PID0;
    }

    public void setPID0(int PID0) {
        this.PID0 = PID0;
    }

    public int getPID1() {
        return PID1;
    }

    public void setPID1(int PID1) {
        this.PID1 = PID1;
    }

    public int getPID2() {
        return PID2;
    }

    public void setPID2(int PID2) {
        this.PID2 = PID2;
    }

    public int getPID3() {
        return PID3;
    }

    public void setPID3(int PID3) {
        this.PID3 = PID3;
    }

    @Override
    public String toString() {
        return "AssignFrameIdRange { " + slave.getName() + ", " + startIndex
                + (isLookupIDs() ? ", " + PID0 + ", " + PID1 + ", " + PID2 + ", " + PID3 : "")
                + " } delay " + getFrameTime() + " ms;";
    }
}
