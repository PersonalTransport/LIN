package com.ptransportation.LIN.model;

public class FreeFormatEntry extends ScheduleTableEntry {
    private int d1;
    private int d2;
    private int d3;
    private int d4;
    private int d5;
    private int d6;
    private int d7;
    private int d8;

    public int getD1() {
        return d1;
    }

    public void setD1(int d1) {
        this.d1 = d1;
    }

    public int getD2() {
        return d2;
    }

    public void setD2(int d2) {
        this.d2 = d2;
    }

    public int getD3() {
        return d3;
    }

    public void setD3(int d3) {
        this.d3 = d3;
    }

    public int getD4() {
        return d4;
    }

    public void setD4(int d4) {
        this.d4 = d4;
    }

    public int getD5() {
        return d5;
    }

    public void setD5(int d5) {
        this.d5 = d5;
    }

    public int getD6() {
        return d6;
    }

    public void setD6(int d6) {
        this.d6 = d6;
    }

    public int getD7() {
        return d7;
    }

    public void setD7(int d7) {
        this.d7 = d7;
    }

    public int getD8() {
        return d8;
    }

    public void setD8(int d8) {
        this.d8 = d8;
    }

    @Override
    public String toString() {
        return "FreeFormat { " + d1 + ", " + d2 + ", " + d3 + ", " + d4 + ", " + d5 + ", " + d6 + ", " + d7 + ", " + d8 + " } delay "
                + getFrameTime()+" ms;";
    }
}
