package com.ptransportation.LIN.schedule;

public class FreeFormatEntry extends Entry {
    private int D1;
    private int D2;
    private int D3;
    private int D4;
    private int D5;
    private int D6;
    private int D7;
    private int D8;

    public FreeFormatEntry(int d1, int d2, int d3, int d4, int d5, int d6, int d7, int d8, float frameTime) {
        super(frameTime);
        this.D1 = d1;
        this.D2 = d2;
        this.D3 = d3;
        this.D4 = d4;
        this.D5 = d5;
        this.D6 = d6;
        this.D7 = d7;
        this.D8 = d8;
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

    public int getD6() {
        return D6;
    }

    public int getD7() {
        return D7;
    }

    public int getD8() {
        return D8;
    }
}
