package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public class ScheduleTable {
    private Master master;
    private String name;
    private List<ScheduleTableEntry> entries;

    public ScheduleTable(String name) {
        this.name = name;
        this.entries = new ArrayList<ScheduleTableEntry>();
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScheduleTableEntry> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        String s = getName() + " {\n";
        for(ScheduleTableEntry entry:getEntries())
            s += "\t" + entry.toString().replaceAll("\n","\n\t") + "\n";
        return s + "}";
    }
}
