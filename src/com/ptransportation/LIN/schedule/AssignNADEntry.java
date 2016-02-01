package com.ptransportation.LIN.schedule;


import com.ptransportation.LIN.Slave;

public class AssignNADEntry extends Entry {
    private Slave slave;

    public AssignNADEntry(Slave slave, float frameTime) {
        super(frameTime);
        this.slave = slave;
    }

    public Slave getSlave() {
        return slave;
    }
}
