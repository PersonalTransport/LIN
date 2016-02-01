package com.ptransportation.LIN.schedule;

public class ConditionalChangeNADEntry extends Entry {
    private int NAD;
    private int id;
    private int byteV;
    private int mask;
    private int invert;
    private int newNAD;
    public ConditionalChangeNADEntry(int NAD, int id, int byteV, int mask, int invert, int newNAD,float frameTime) {
        super(frameTime);
        this.NAD = NAD;
        this.id = id;
        this.byteV = byteV;
        this.mask = mask;
        this.invert = invert;
        this.newNAD = newNAD;
    }

    public int getNAD() {
        return NAD;
    }

    public int getId() {
        return id;
    }

    public int getByteV() {
        return byteV;
    }

    public int getMask() {
        return mask;
    }

    public int getInvert() {
        return invert;
    }

    public int getNewNAD() {
        return newNAD;
    }
}
