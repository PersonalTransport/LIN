package com.ptransportation.LIN.model;

public class SlaveRespEntry extends ScheduleTableEntry {
    @Override
    public String toString() {
        return "SlaveResp delay " + getFrameTime() + " ms;";
    }
}
