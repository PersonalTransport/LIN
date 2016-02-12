package com.ptransportation.LIN.model;

public class SaveConfigurationEntry extends ScheduleTableEntry {
    private Slave slave;

    public Slave getSlave() {
        return slave;
    }

    public void setSlave(Slave slave) {
        this.slave = slave;
    }

    @Override
    public String toString() {
        return "SaveConfiguration { " + slave.getName()+ " } delay "+getFrameTime()+" ms;";
    }
}
