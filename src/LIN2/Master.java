package LIN2;

import LIN2.schedule.ScheduleTable;

import java.util.ArrayList;

public class Master extends Node {
    private float bitrate;
    private float timebase;
    private float jitter;
    private String channelName;
    private ArrayList<ScheduleTable> scheduleTables;

    public Master(String name, float bitrate, float timebase, float jitter) {
        super(name);
        this.bitrate = bitrate;
        this.timebase = timebase;
        this.jitter = jitter;
        this.scheduleTables = new ArrayList<>();
    }

    public float getBitrate() {
        return bitrate;
    }

    public void setBitrate(float bitrate) {
        this.bitrate = bitrate;
    }

    public float getTimebase() {
        return timebase;
    }

    public void setTimebase(float timebase) {
        this.timebase = timebase;
    }

    public float getJitter() {
        return jitter;
    }

    public void setJitter(float jitter) {
        this.jitter = jitter;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void addScheduleTable(ScheduleTable table) {
        if(!scheduleTables.contains(table))
            scheduleTables.add(table);
    }

    public ScheduleTable getScheduleTable(String tableName) {
        for(ScheduleTable table:scheduleTables) {
            if(table.getName().equals(tableName))
                return table;
        }
        return null;
    }

    public ArrayList<ScheduleTable> getScheduleTables() {
        return scheduleTables;
    }
}
