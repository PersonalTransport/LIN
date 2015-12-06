package LIN2;

import LIN2.bitrate.Bitrate;
import LIN2.encoding.Encoding;
import LIN2.frame.EventTriggeredFrame;
import LIN2.frame.Frame;
import LIN2.frame.UnconditionalFrame;
import LIN2.signal.Signal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class Node {
    private String name;
    private String protocol;
    private int supplier;
    private int function;
    private int variant;
    private Bitrate bitrate;
    private ArrayList<Frame> frames;

    public Node(String name) {
        this.name = name;
        this.protocol = "2.2";
        this.supplier = Integer.MIN_VALUE; // Invalid
        this.function = Integer.MIN_VALUE; // Invalid
        this.variant = 0;
        this.frames = new ArrayList<>();
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

    public Bitrate getBitrate() {
        return bitrate;
    }

    public void setBitrate(Bitrate bitrate) {
        this.bitrate = bitrate;
    }

    public void addFrame(Frame frame) {
        if(frame instanceof UnconditionalFrame) {
            UnconditionalFrame uf = (UnconditionalFrame)frame;
            if(uf.getPublisher() != this)
                uf.addSubscriber(this);
        }
        this.frames.add(frame);
    }

    public Frame getFrame(String frameName) {
        for(Frame frame:frames) {
            if(frame.getName().equals(frameName))
                return frame;
        }
        return null;
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }

    public Collection<UnconditionalFrame> getUnconditionalFrames() {
        LinkedHashSet<UnconditionalFrame> unconditionalFrames = new LinkedHashSet<>();
        for(Frame frame:frames) {
            if(frame instanceof UnconditionalFrame)
                unconditionalFrames.add((UnconditionalFrame) frame);
        }
        return unconditionalFrames;
    }

    public Collection<EventTriggeredFrame> getEventTriggeredFrames() {
        LinkedHashSet<EventTriggeredFrame> eventTriggeredFrames = new LinkedHashSet<>();
        for(Frame frame:frames) {
            if(frame instanceof EventTriggeredFrame)
                eventTriggeredFrames.add((EventTriggeredFrame) frame);
        }
        return eventTriggeredFrames;
    }

    public Signal getSignal(String signalName) {
        for(Frame frame:frames) {
            if (frame instanceof UnconditionalFrame) {
                UnconditionalFrame uf = (UnconditionalFrame)frame;
                Signal signal = uf.getSignal(signalName);
                if(signal != null)
                    return signal;
            }
        }
        return null;
    }

    public Collection<Signal> getSignals() {
        LinkedHashSet<Signal> signals = new LinkedHashSet<>();
        for(Frame frame:frames) {
            if(frame instanceof UnconditionalFrame)
                signals.addAll(((UnconditionalFrame)frame).getSignals());
        }
        return signals;
    }

    public Encoding getEncoding(String encodingName) {
        for(Frame frame:frames) {
            if (frame instanceof UnconditionalFrame) {
                UnconditionalFrame uf = (UnconditionalFrame)frame;
                Encoding encoding = uf.getEncoding(encodingName);
                if(encoding != null)
                    return encoding;
            }
        }
        return null;
    }

    public Collection<Encoding> getEncodings() {
        LinkedHashSet<Encoding> encodings = new LinkedHashSet<>();
        for(Frame frame:frames) {
            if(frame instanceof UnconditionalFrame)
                encodings.addAll(((UnconditionalFrame)frame).getEncodings());
        }
        return encodings;
    }
}
