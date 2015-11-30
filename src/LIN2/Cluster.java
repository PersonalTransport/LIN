package LIN2;

import LIN2.encoding.Encoding;
import LIN2.frame.EventTriggeredFrame;
import LIN2.frame.Frame;
import LIN2.frame.UnconditionalFrame;
import LIN2.signal.Signal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.logging.FileHandler;

public class Cluster {
    private Master master;
    private ArrayList<Slave> slaves;

    public Cluster() {
        this.slaves = new ArrayList<>();
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public void addSlave(Slave slave) {
        if(!slaves.contains(slave))
            slaves.add(slave);
    }

    public Node getNode(String nodeName) {
        Slave slave = getSlave(nodeName);
        if(slave != null)
            return slave;
        return master.getName().equals(nodeName) ? master : null;
    }

    public Slave getSlave(String slaveName) {
        for(Slave slave:slaves) {
            if(slave.getName().equals(slaveName))
                return slave;
        }
        return null;
    }

    public ArrayList<Slave> getSlaves() {
        return slaves;
    }

    public Frame getFrame(String frameName) {
        for(Slave slave:slaves) {
            Frame frame = slave.getFrame(frameName);
            if(frame != null)
                return frame;
        }
        return (master != null) ? master.getFrame(frameName) : null;
    }

    public Collection<Frame> getFrames() {
        LinkedHashSet<Frame> frames = new LinkedHashSet<>();
        for(Slave slave:slaves)
            frames.addAll(slave.getFrames());
        if(master != null)
            frames.addAll(master.getFrames());
        return frames;
    }

    public Collection<UnconditionalFrame> getUnconditionalFrames() {
        LinkedHashSet<UnconditionalFrame> unconditionalFrames = new LinkedHashSet<>();
        for(Slave slave:slaves)
            unconditionalFrames.addAll(slave.getUnconditionalFrames());
        if(master != null)
            unconditionalFrames.addAll(master.getUnconditionalFrames());
        return unconditionalFrames;
    }

    public Collection<EventTriggeredFrame> getEventTriggeredFrames() {
        LinkedHashSet<EventTriggeredFrame> eventTriggeredFrames = new LinkedHashSet<>();
        for(Slave slave:slaves)
            eventTriggeredFrames.addAll(slave.getEventTriggeredFrames());
        if(master != null)
            eventTriggeredFrames.addAll(master.getEventTriggeredFrames());
        return eventTriggeredFrames;
    }

    public Signal getSignal(String signalName) {
        for(Slave slave:slaves) {
            Signal signal = slave.getSignal(signalName);
            if(signal != null)
                return signal;
        }
        return (master != null) ? master.getSignal(signalName) : null;
    }

    public Collection<Signal> getSignals() {
        LinkedHashSet<Signal> signals = new LinkedHashSet<>();
        for(Slave slave:slaves)
            signals.addAll(slave.getSignals());
        if(master != null)
            signals.addAll(master.getSignals());
        return signals;
    }

    public Encoding getEncoding(String encodingName) {
        for(Slave slave:slaves) {
            Encoding encoding = slave.getEncoding(encodingName);
            if(encoding != null)
                return encoding;
        }
        return (master != null) ? master.getEncoding(encodingName) : null;
    }

    public Collection<Encoding> getEncodings() {
        LinkedHashSet<Encoding> encodings = new LinkedHashSet<>();
        for(Slave slave:slaves)
            encodings.addAll(slave.getEncodings());
        if(master != null)
            encodings.addAll(master.getEncodings());
        return encodings;
    }
}
