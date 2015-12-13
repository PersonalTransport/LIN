package LIN2.schedule;

import java.util.List;

public abstract class Entry {
    private float frameTime;
    private ScheduleTable table;

    public Entry(float frameTime) {
        this.frameTime = frameTime;
    }

    public float getFrameTime() {
        return frameTime;
    }

    public int getTicks() {
        return (int)(frameTime/table.getMaster().getTimebase());
    }

    public void setTable(ScheduleTable table) {
        this.table = table;
    }

    public ScheduleTable getTable() {
        return table;
    }

    // TODO move to model
    public int getTicksMinusOne() {
        return (int)(frameTime/table.getMaster().getTimebase())-1;
    }

    // TODO move to model
    public int getNextEntry() {
        List<Entry> entries = table.getEntries();
        return (entries.indexOf(this)+1) %entries.size();
    }

    // TODO move to model
    public int getNextEntryPlusOne() {
        return getNextEntry()+1;
    }
}
