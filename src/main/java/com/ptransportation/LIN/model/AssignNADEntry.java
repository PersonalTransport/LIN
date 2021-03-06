package com.ptransportation.LIN.model;

public class AssignNADEntry extends ScheduleTableEntry {
    private Slave slave;

    public Slave getSlave() {
        return slave;
    }

    public void setSlave(Slave slave) {
        this.slave = slave;
    }

    @Override
    public String toString() {
        return "AssignNAD { " + slave.getName() + " } delay " + getFrameTime() + " ms;";
    }
}
