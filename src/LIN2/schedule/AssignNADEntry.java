package LIN2.schedule;

import LIN2.Slave;

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
