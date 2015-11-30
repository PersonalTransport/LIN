package LIN2.schedule;

import LIN2.Slave;

public class AssignFrameIdRangeEntry extends Entry {
    private Slave slave;
    private int startIndex;
    private int[] PIDs;

    public AssignFrameIdRangeEntry(Slave slave, int startIndex, float frameTime) {
        super(frameTime);
        this.slave = slave;
        this.startIndex = startIndex;
    }

    public void setPIDs(int PID0, int PID1, int PID2, int PID3) {
        this.PIDs = new int[]{PID0,PID1,PID2,PID3};
    }

    public int[] getPIDs() {
        return PIDs;
    }

    public void useFramePIDS() {
        this.PIDs = null;
    }

    public Slave getSlave() {
        return slave;
    }

    public int getStartIndex() {
        return startIndex;
    }
}
