package LIN2;

import LIN2.signal.Signal;

import java.util.ArrayList;

public class Slave extends Node {
    private boolean sendsWakeUpSignal;
    private int configuredNAD;
    private Integer initialNAD;
    private Float p2Min;
    private Float STMin;
    private Float nAsTimeout;
    private Float nCrTimeout;
    private Signal responseErrorSignal;
    private ArrayList<Signal> faultStateSignals;

    public Slave(String name) {
        super(name);
        this.nAsTimeout = 1000f;
        this.nCrTimeout = 1000f;
        this.faultStateSignals = new ArrayList<>();
    }

    public boolean isSendsWakeUpSignal() {
        return sendsWakeUpSignal;
    }

    public void setSendsWakeUpSignal(boolean sendsWakeUpSignal) {
        this.sendsWakeUpSignal = sendsWakeUpSignal;
    }

    public void setConfiguredNAD(int configuredNAD) {
        this.configuredNAD = configuredNAD;
    }

    public int getConfiguredNAD() {
        return configuredNAD;
    }

    public void setInitialNAD(Integer initialNAD) {
        this.initialNAD = initialNAD;
    }

    public Integer getInitialNAD() {
        return initialNAD;
    }

    public Float getP2Min() {
        return p2Min;
    }

    public void setP2Min(Float p2Min) {
        this.p2Min = p2Min;
    }

    public Float getSTMin() {
        return STMin;
    }

    public void setSTMin(Float STMin) {
        this.STMin = STMin;
    }

    public Float getNAsTimeout() {
        return nAsTimeout;
    }

    public void setNAsTimeout(Float nAsTimeout) {
        this.nAsTimeout = nAsTimeout;
    }

    public Float getNCrTimeout() {
        return nCrTimeout;
    }

    public void setNCrTimeout(Float nCrTimeout) {
        this.nCrTimeout = nCrTimeout;
    }

    public void setResponseErrorSignal(Signal responseErrorSignal) {
        this.responseErrorSignal = responseErrorSignal;
    }

    public Signal getResponseErrorSignal() {
        return responseErrorSignal;
    }

    public void addFaultStateSignal(Signal signal) {
        if(!faultStateSignals.contains(signal))
            this.faultStateSignals.add(signal);
    }

    public void removeFaultStateSignal(Signal signal) {
        this.faultStateSignals.remove(signal);
    }

    public ArrayList<Signal> getFaultStateSignals() {
        return faultStateSignals;
    }
}
