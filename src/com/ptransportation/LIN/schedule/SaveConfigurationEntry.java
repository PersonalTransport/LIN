package com.ptransportation.LIN.schedule;

import com.ptransportation.LIN.Slave;

public class SaveConfigurationEntry extends Entry {
    private Slave slave;

    public SaveConfigurationEntry(Slave slave, float frameTime) {
        super(frameTime);
        this.slave = slave;
    }

    public Slave getSlave() {
        return slave;
    }
}
