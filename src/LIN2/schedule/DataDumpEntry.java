package LIN2.schedule;

import LIN2.Slave;

public class DataDumpEntry extends Entry {
    private Slave slave;
    private int D1;
    private int D2;
    private int D3;
    private int D4;
    private int D5;

    public DataDumpEntry(Slave slave, int D1, int D2, int D3, int D4, int D5, float frameTime) {
        super(frameTime);
        this.slave = slave;
        this.D1 = D1;
        this.D2 = D2;
        this.D3 = D3;
        this.D4 = D4;
        this.D5 = D5;
    }

    public Slave getSlave() {
        return slave;
    }

    public int getD1() {
        return D1;
    }

    public int getD2() {
        return D2;
    }

    public int getD3() {
        return D3;
    }

    public int getD4() {
        return D4;
    }

    public int getD5() {
        return D5;
    }
}
