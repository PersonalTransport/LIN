package LIN2.frame;

import LIN2.Node;

public abstract class Frame {
    private String name;
    private int length;
    private int ID;
    private Integer minPeriod;
    private Integer maxPeriod;

    public Frame(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setMinPeriod(Integer minPeriod) {
        this.minPeriod = minPeriod;
    }

    public Integer getMinPeriod() {
        return minPeriod;
    }

    public void setMaxPeriod(Integer maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    public Integer getMaxPeriod() {
        return maxPeriod;
    }

    public abstract boolean publishedBy(Node node);
}
