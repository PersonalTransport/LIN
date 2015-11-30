package LIN2.schedule;

import java.util.ArrayList;

public class ScheduleTable {
    private String name;
    private ArrayList<Entry> entries;

    public ScheduleTable(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addEntry(Entry entry) {
        this.entries.add(entry);
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }
}
