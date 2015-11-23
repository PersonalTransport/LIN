package LIN;


import LIN.bitrate.Bitrate;
import LIN.bitrate.FixedBitrate;

import java.util.ArrayList;

public class Master extends Slave {
    private float timeBase;
    private float jitter;
    private String protocolVersion;
    private String languageVersion;

    private ArrayList<Slave> slaves;
    private String channelName;

    public Master(String name, float timeBase, float jitter) {
        super(name);
        this.timeBase = timeBase;
        this.jitter = jitter;
        this.slaves = new ArrayList<>();
        this.slaves.add(this);
    }

    public float getTimeBase() {
        return timeBase;
    }

    public float getJitter() {
        return jitter;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getLanguageVersion() {
        return languageVersion;
    }

    public void setLanguageVersion(String languageVersion) {
        this.languageVersion = languageVersion;
    }

    @Override
    public void setBitrate(Bitrate bitrate) {
        if(!(bitrate instanceof FixedBitrate))
            throw new IllegalArgumentException("Master node "+getName()+" must have a fixed bitrate!");
        super.setBitrate(bitrate);
    }

    public void addSlave(Slave slave) {
        this.slaves.add(slave);
    }

    public Slave getSlave(String slave) {
        for(Slave s: slaves) {
            if(s.getName().equals(slave))
                return s;
        }
        return null;
    }

    public void removeSlave(Slave slave) {
        this.slaves.remove(slave);
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
}
