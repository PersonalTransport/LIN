package LIN2;

import LIN2.encoding.Encoding;
import LIN2.frame.EventTriggeredFrame;
import LIN2.frame.Frame;
import LIN2.frame.UnconditionalFrame;
import LIN2.signal.Signal;

import java.util.HashSet;
import java.util.Set;

public class Cluster {
    private Master master;
    private Set<Slave> slaves;

    public Cluster() {
        this.slaves = new HashSet<>();
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public void addSlave(Slave slave) {
        slaves.add(slave);
    }

    public void removeSlave(Slave slave) {
        slaves.remove(slave);
    }

    public Node getNode(String nodeName) {
        Slave slave = getSlave(nodeName);
        if(slave != null)
            return slave;
        return (master != null && master.getName().equals(nodeName)) ? master : null;
    }

    public Slave getSlave(String slaveName) {
        for(Slave slave:slaves) {
            if(slave.getName().equals(slaveName))
                return slave;
        }
        return null;
    }

    public Set<Slave> getSlaves() {
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

    public Set<Node> getNodes() {
        HashSet<Node> nodes = new HashSet<>();
        nodes.add(master);
        nodes.addAll(slaves);
        return nodes;
    }

    public Set<Frame> getFrames() {
        HashSet<Frame> frames = new HashSet<>();
        for(Slave slave:slaves)
            frames.addAll(slave.getFrames());
        if(master != null)
            frames.addAll(master.getFrames());
        return frames;
    }

    public UnconditionalFrame getUnconditionalFrame(String name) {
        for(Slave slave:slaves) {
            UnconditionalFrame frame = slave.getUnconditionalFrame(name);
            if(frame != null)
                return frame;
        }
        return (master != null) ? master.getUnconditionalFrame(name) : null;
    }

    public Set<UnconditionalFrame> getUnconditionalFrames() {
        HashSet<UnconditionalFrame> unconditionalFrames = new HashSet<>();
        for(Slave slave:slaves)
            unconditionalFrames.addAll(slave.getUnconditionalFrames());
        if(master != null)
            unconditionalFrames.addAll(master.getUnconditionalFrames());
        return unconditionalFrames;
    }

    public EventTriggeredFrame getEventTriggeredFrame(String name) {
        for(Slave slave:slaves) {
            EventTriggeredFrame frame = slave.getEventTriggeredFrame(name);
            if(frame != null)
                return frame;
        }
        return (master != null) ? master.getEventTriggeredFrame(name) : null;
    }

    public Set<EventTriggeredFrame> getEventTriggeredFrames() {
        HashSet<EventTriggeredFrame> eventTriggeredFrames = new HashSet<>();
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

    public Set<Signal> getSignals() {
        HashSet<Signal> signals = new HashSet<>();
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

    public Set<Encoding> getEncodings() {
        HashSet<Encoding> encodings = new HashSet<>();
        for(Slave slave:slaves)
            encodings.addAll(slave.getEncodings());
        if(master != null)
            encodings.addAll(master.getEncodings());
        return encodings;
    }
}
