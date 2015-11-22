package LIN;

import LIN.bitrate.Bitrate;
import LIN.encoding.Encoding;
import LIN.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class Slave {

    private String name;
    private String protocol;
    private int supplier;
    private int function;
    private int variant;
    private boolean sendWakeUp;
    private Bitrate bitrate;
    private ArrayList<Frame> frames;
    private ArrayList<Encoding> encodings;
    private ArrayList<Integer> possibleNADs;
    private int diagnosticClass;
    private float p2Min;
    private float stMin;
    private float nAsTimeout;
    private float nCrTimeout;
    private ArrayList<Integer> supportSIDs;
    private int maxMessageLength;
    private Signal responseErrorSignal;
    private ArrayList<Signal> faultStateSignals;
    private String freeText;

    public Slave(String name) {
        this.name = name;
        this.frames = new ArrayList<>();
        this.encodings = new ArrayList<>();
        this.possibleNADs = new ArrayList<>();
        this.p2Min = 50;
        this.stMin = 0;
        this.nAsTimeout = 1000;
        this.nCrTimeout = 1000;
        this.supportSIDs = new ArrayList<>();
        this.maxMessageLength = 4095;
        this.faultStateSignals = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public int getFunction() {
        return function;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public int getVariant() {
        return variant;
    }

    public void setSendWakeUp(boolean sendWakeUp) {
        this.sendWakeUp = sendWakeUp;
    }

    public boolean isSendWakeUp() {
        return sendWakeUp;
    }

    public void setBitrate(Bitrate bitrate) {
        this.bitrate = bitrate;
    }

    public Bitrate getBitrate() {
        return bitrate;
    }

    public void addFrame(Frame frame) {
        frames.add(frame);
    }

    public void removeFrame(Frame frame) {
        frames.remove(frame);
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public void addEncoding(Encoding encoding) {
        encodings.add(encoding);
    }

    public Encoding getEncoding(String encoding) {
        for(Encoding e:encodings) {
            if(e.getName().equals(encoding))
                return e;
        }
        return null;
    }

    public void removeEncoding(Encoding encoding) {
        encodings.remove(encoding);
    }

    public void setDiagnosticClass(int diagnosticClass) {
        this.diagnosticClass = diagnosticClass;
    }

    public int getDiagnosticClass() {
        return diagnosticClass;
    }

    public void setP2Min(float p2Min) {
        this.p2Min = p2Min;
    }

    public float getP2Min() {
        return p2Min;
    }

    public void setSTMin(float STMin) {
        this.stMin = STMin;
    }

    public float getSTMin() {
        return stMin;
    }

    public void setNAsTimeout(float NAsTimeout) {
        this.nAsTimeout = NAsTimeout;
    }

    public float getNAsTimeout() {
        return nAsTimeout;
    }

    public void setNCrTimeout(float NCrTimeout) {
        this.nCrTimeout = NCrTimeout;
    }

    public void addPossibleNad(int NAD) {
        this.possibleNADs.add(NAD);
    }

    public void removePossibleNad(int NAD) {
        this.possibleNADs.remove(NAD);
    }

    public void addSupportSID(int SID) {
        this.supportSIDs.add(SID);
    }
    public void removeSupportSID(int SID) {
        this.supportSIDs.remove(SID);
    }

    public int getMaxMessageLength() {
        return maxMessageLength;
    }

    public void setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
    }

    public Signal getSignal(String signalName) {
        for(Frame frame: frames) {
            Signal signal = frame.getSignal(signalName);
            if(signal != null)
                return signal;
        }
        return null;
    }

    public void setResponseErrorSignal(Signal responseErrorSignal) {
        this.responseErrorSignal = responseErrorSignal;
    }

    public Signal getResponseErrorSignal() {
        return responseErrorSignal;
    }

    public void addFaultStateSignal(Signal signal) {
        this.faultStateSignals.add(signal);
    }

    public void removeFaultStateSignal(Signal signal) {
        this.faultStateSignals.remove(signal);
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public String getFreeText() {
        return freeText;
    }
}
