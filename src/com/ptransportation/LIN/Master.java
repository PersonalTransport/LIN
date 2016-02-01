package com.ptransportation.LIN;

import com.ptransportation.LIN.bitrate.Bitrate;
import com.ptransportation.LIN.bitrate.FixedBitrate;
import com.ptransportation.LIN.schedule.ScheduleTable;

import java.util.ArrayList;
import java.util.List;

public class Master extends Node {
    private float timebase;
    private float jitter;
    private String channelName;
    private List<ScheduleTable> scheduleTables;

    public Master(String name, float bitrate, float timebase, float jitter) {
        super(name);
        setBitrate(new FixedBitrate(bitrate));
        this.timebase = timebase;
        this.jitter = jitter;
        this.scheduleTables = new ArrayList<ScheduleTable>();
    }

    @Override
    public void setBitrate(Bitrate bitrate) {
        if(!(bitrate instanceof FixedBitrate))
            throw new IllegalArgumentException("master bitrate must be a fixed bitrate.");
        super.setBitrate(bitrate);
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
        if(!scheduleTables.contains(table)) {
            scheduleTables.add(table);
            table.setMaster(this);
        }
    }

    public ScheduleTable getScheduleTable(String tableName) {
        for(ScheduleTable table:scheduleTables) {
            if(table.getName().equals(tableName))
                return table;
        }
        return null;
    }

    public List<ScheduleTable> getScheduleTables() {
        return scheduleTables;
    }

    public boolean isMaster() {
        return true;
    }
}
