package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Slave extends Node {
    private boolean sendsWakeUpSignal;
    private NadSet nadSet;
    private int diagnosticClass;
    private double p2Min;
    private double stMin;
    private double nAsTimeout;
    private double nCrTimeout;
    private List<Integer> supportedSIDS;
    private int maxMessageLength;
    private Signal responseError;
    private List<Signal> faultStateSignals;

    public Slave(String name) {
        super(name);
        this.supportedSIDS = new ArrayList<Integer>();
        this.faultStateSignals = new ArrayList<Signal>();
    }

    public boolean getSendsWakeUpSignal() {
        return this.sendsWakeUpSignal;
    }

    public void setSendsWakeUpSignal(boolean sendsWakeUpSignal) {
        this.sendsWakeUpSignal = sendsWakeUpSignal;
    }

    public NadSet getNadSet() {
        return this.nadSet;
    }

    public void setNadSet(NadSet nadSet) {
        this.nadSet = nadSet;
    }

    public int getDiagnosticClass() {
        return this.diagnosticClass;
    }

    public void setDiagnosticClass(int diagnosticClass) {
        this.diagnosticClass = diagnosticClass;
    }

    public double getP2Min() {
        return this.p2Min;
    }

    public void setP2Min(double p2Min) {
        this.p2Min = p2Min;
    }

    public double getStMin() {
        return this.stMin;
    }

    public void setStMin(double stMin) {
        this.stMin = stMin;
    }

    public double getNAsTimeout() {
        return this.nAsTimeout;
    }

    public void setNAsTimeout(double nAsTimeout) {
        this.nAsTimeout = nAsTimeout;
    }

    public double getNCrTimeout() {
        return this.nCrTimeout;
    }

    public void setNCrTimeout(double nCrTimeout) {
        this.nCrTimeout = nCrTimeout;
    }

    public List<Integer> getSupportedSIDS() {
        return Collections.unmodifiableList(this.supportedSIDS);
    }

    public int getMaxMessageLength() {
        return this.maxMessageLength;
    }

    public void setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
    }

    public Signal getResponseError() {
        return this.responseError;
    }

    public void setResponseError(Signal responseError) {
        this.responseError = responseError;
    }

    public List<Signal> getFaultStateSignals() {
        return this.faultStateSignals;
    }
}
