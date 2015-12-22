package LIN2;

import LIN2.bitrate.Bitrate;
import LIN2.encoding.Encoding;
import LIN2.frame.EventTriggeredFrame;
import LIN2.frame.Frame;
import LIN2.frame.UnconditionalFrame;
import LIN2.signal.Signal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    private String name;
    private String protocol;
    private int supplier;
    private int function;
    private int variant;
    private Bitrate bitrate;
    private List<Frame> frames;

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

    public boolean getUseClassicChecksum() {
        // TODO make maybe allow this to be true in the future.
        return false;
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

    public List<Frame> getFrames() {
        return frames;
    }

    public UnconditionalFrame getUnconditionalFrame(String name) {
        for(Frame frame:frames) {
            if (frame instanceof UnconditionalFrame && frame.getName().equals(name))
                return (UnconditionalFrame) frame;
        }
        return null;
    }

    public Set<UnconditionalFrame> getUnconditionalFrames() {
        HashSet<UnconditionalFrame> unconditionalFrames = new HashSet<>();
        for(Frame frame:frames) {
            if(frame instanceof UnconditionalFrame)
                unconditionalFrames.add((UnconditionalFrame) frame);
        }
        return unconditionalFrames;
    }

    public EventTriggeredFrame getEventTriggeredFrame(String name) {
        for(Frame frame:frames) {
            if (frame instanceof EventTriggeredFrame && frame.getName().equals(name))
                return (EventTriggeredFrame) frame;
        }
        return null;
    }

    public Set<EventTriggeredFrame> getEventTriggeredFrames() {
        HashSet<EventTriggeredFrame> eventTriggeredFrames = new HashSet<>();
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

    public Set<Signal> getSignals() {
        HashSet<Signal> signals = new HashSet<>();
        for(Frame frame:frames) {
            if(frame instanceof UnconditionalFrame)
                signals.addAll(((UnconditionalFrame)frame).getSignals());
        }
        return signals;
    }

    public Set<Signal> getPublishedSignals() {
        HashSet<Signal> signals = new HashSet<>();
        for(Frame frame:frames) {
            if(frame instanceof UnconditionalFrame) {
                UnconditionalFrame uf = (UnconditionalFrame)frame;
                if(uf.getPublisher() == this)
                    signals.addAll(uf.getSignals());
            }
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

    public Set<Encoding> getEncodings() {
        HashSet<Encoding> encodings = new HashSet<>();
        for(Frame frame:frames) {
            if(frame instanceof UnconditionalFrame)
                encodings.addAll(((UnconditionalFrame)frame).getEncodings());
        }
        return encodings;
    }
}
