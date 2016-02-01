package com.ptransportation.LIN.schedule;


import com.ptransportation.LIN.Master;

import java.util.ArrayList;

public class ScheduleTable {
    private String name;
    private ArrayList<Entry> entries;
    private Master master;

    public ScheduleTable(String name) {
        this.name = name;
        this.entries = new ArrayList<Entry>();
    }

    public String getName() {
        return name;
    }

    public void addEntry(Entry entry) {
        this.entries.add(entry);
        entry.setTable(this);
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public int getEntryCount() {
        return entries.size();
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }
}
