package com.ptransportation.LIN.model;

public class ConditionalChangeNADEntry extends ScheduleTableEntry {
    private int NAD;
    private int id;
    private int byte_;
    private int mask;
    private int inv;
    private int newNAD;

    public int getNAD() {
        return NAD;
    }

    public void setNAD(int NAD) {
        this.NAD = NAD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getByte() {
        return byte_;
    }

    public void setByte(int byte_) {
        this.byte_ = byte_;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getInv() {
        return inv;
    }

    public void setInv(int inv) {
        this.inv = inv;
    }

    public int getNewNAD() {
        return newNAD;
    }

    public void setNewNAD(int newNAD) {
        this.newNAD = newNAD;
    }

    @Override
    public String toString() {
        return "ConditionalChangeNAD { " + NAD + ", " + id + ", " + byte_ + ", " + mask + ", " + inv + ", " + newNAD + " } delay "
                + getFrameTime() + " ms;";
    }
}
