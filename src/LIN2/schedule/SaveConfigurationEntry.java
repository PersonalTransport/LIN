package LIN2.schedule;

import LIN2.Slave;

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
