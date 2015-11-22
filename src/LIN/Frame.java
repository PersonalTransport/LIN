package LIN;

import LIN.signal.Signal;

import java.util.ArrayList;

public class Frame {
    private String name;
    private String type;
    private int length;
    private int minimumPeriod;
    private int maximumPeriod;
    private String eventTriggeredFrame;
    private ArrayList<Signal> signals;

    public Frame(String name,String type,int length) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.minimumPeriod = 0;
        this.maximumPeriod = Integer.MAX_VALUE;
        this.signals = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public void setMinimumPeriod(int minimumPeriod) {
        this.minimumPeriod = minimumPeriod;
    }

    public int getMinimumPeriod() {
        return minimumPeriod;
    }

    public void setMaximumPeriod(int maximumPeriod) {
        this.maximumPeriod = maximumPeriod;
    }

    public int getMaximumPeriod() {
        return maximumPeriod;
    }

    public void setEventTriggeredFrame(String eventTriggeredFrame) {
        this.eventTriggeredFrame = eventTriggeredFrame;
    }

    public String getEventTriggeredFrame() {
        return eventTriggeredFrame;
    }

    public void addSignal(Signal signal) {
        this.signals.add(signal);
    }

    public Signal getSignal(String signalName) {
        for(Signal signal : signals) {
            if(signal.getName().equals(signalName))
                return  signal;
        }
        return null;
    }

    public void removeSignal(Signal signal) {
        this.signals.remove(signal);
    }
}
