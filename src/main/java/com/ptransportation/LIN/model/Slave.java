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

    @Override
    public String toString() {
        String s = "";
        s += "node" + " " + getName() + " {\n";

        s += "\tgeneral {\n";
        s += "\t\tLIN_protocol_version = \"" + getProtocolVersion() + "\";\n";
        s += "\t\tsupplier = 0x" + Integer.toHexString(getSupplier()) + ";\n";
        s += "\t\tfunction = 0x" + Integer.toHexString(getFunction()) + ";\n";
        s += "\t\tvariant = " + getVariant() + ";\n";
        s += "\t\tbitrate = " + getBitrate().toString() + ";\n";
        s += "\t\tsends_wake_up_signal = " + (getSendsWakeUpSignal() ? "\"yes\"" : "\"no\"") + ";\n";
        s += "\t}\n";

        s += "\tdiagnostic {\n";
        s += "\t\tNAD = " + getNadSet() + ";\n";
        s += "\t\tdiagnostic_class = " + getDiagnosticClass() + ";\n";
        s += "\t\tP2_min = " + getP2Min() + ";\n";
        s += "\t\tST_min = " + getStMin() + ";\n";
        s += "\t\tN_As_timeout = " + getNAsTimeout() + ";\n";
        s += "\t\tN_Cr_timeout = " + getNCrTimeout() + ";\n";
        s += "\t\tsupport_sid " + getSupportedSIDS().toString().replace("[", "{").replace("]", "}") + ";\n";
        s += "\t\tmax_message_length = " + getMaxMessageLength() + ";\n";
        s += "\t}\n";

        s += "\tframes {\n";
        for (Frame frame : getFrames())
            s += "\t\t" + frame.toString().replaceAll("\n", "\n\t\t") + "\n";
        s += "\t}\n";

        s += "\tencoding {\n";
        for (Encoding encoding : getEncodings())
            s += "\t\t" + encoding.toString().replaceAll("\n", "\n\t\t") + "\n";
        s += "\t}\n";

        s += "\tdiagnostic {\n";
        s += "\t\tresponse_error = " + getResponseError().getName() + ";\n";
        if (faultStateSignals.size() > 0) {
            s += "\t\tfault_state_signals = ";
            for (int i = 0; i < faultStateSignals.size() - 1; ++i)
                s += faultStateSignals.get(i).getName() + ", ";
            s += faultStateSignals.get(faultStateSignals.size() - 1) + ";";
        }
        s += "\t}\n";

        if (getFreeText() != null) {
            s += "\tfree_text {\n";
            s += "\t\t\"" + getFreeText() + "\"\n";
            s += "\t}\n";
        }

        s += "}";

        return s;
    }
}
