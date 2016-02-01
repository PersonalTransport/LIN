package com.ptransportation.LIN;


import com.ptransportation.LIN.signal.Signal;
import com.ptransportation.LIN.util.Range;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Slave extends Node {
    private boolean sendsWakeUpSignal;
    private int configuredNAD;
    private Integer initialNAD;
    private Set<Integer> NADSet;
    private Range<Integer> NADRange;
    private Float p2Min;
    private Float STMin;
    private Float nAsTimeout;
    private Float nCrTimeout;
    private Signal responseErrorSignal;
    private Set<Signal> faultStateSignals;
    private String freeText;
    private int diagnosticClass;
    private Set<Integer> supportSIDs;
    private int maxMessageLength;

    public Slave(String name) {
        super(name);
        this.nAsTimeout = 1000f;
        this.nCrTimeout = 1000f;
        this.faultStateSignals = new HashSet<Signal>();
        this.supportSIDs = new HashSet<Integer>(Arrays.asList(0xB2, 0xB7));
        this.maxMessageLength = 4095;
    }

    public boolean getSendsWakeUpSignal() {
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

    public void setNADRange(Range<Integer> NADRange) {
        this.NADRange = NADRange;
        this.NADSet = null;
    }

    public Range<Integer> getNADRange() {
        return NADRange;
    }

    public void setNADSet(Set<Integer> NADSet) {
        this.NADRange = null;
        this.NADSet = NADSet;
    }

    public Set<Integer> getNADSet() {
        return NADSet;
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

    public Set<Signal> getFaultStateSignals() {
        return faultStateSignals;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setDiagnosticClass(int diagnosticClass) {
        this.diagnosticClass = diagnosticClass;
    }

    public int getDiagnosticClass() {
        return diagnosticClass;
    }

    public void addSupportedSID(int SID) {
        supportSIDs.add(SID);
    }

    public void setSupportSIDs(Set<Integer> supportSIDs) {
        this.supportSIDs.clear();
        this.supportSIDs.addAll(supportSIDs);
    }

    public Set<Integer> getSupportSIDs() {
        return supportSIDs;
    }

    public void removeSupportedSID(int SID) {
        supportSIDs.remove(SID);
    }

    public int getMaxMessageLength() {
        return maxMessageLength;
    }

    public void setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
    }

    public boolean isSlave() {
        return true;
    }
}
