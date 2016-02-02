package com.ptransportation.LIN.compiler.generation.models;


import com.ptransportation.capability.nodeCapabilityFile.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterModelAdaptor extends NodeModelAdaptor {
    private final HashMap<Master,List<Frame>> allFrames = new HashMap<Master, List<Frame>>();
    private final HashMap<Master,List<Frame>> subscribeFrames = new HashMap<Master, List<Frame>>();

    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Master master = (Master)o;
        if(propertyName.equals("frames")) {
            return getFrames(master);
        }
        return super.getProperty(self,o,property,propertyName);
    }

    public List<Frame> getFrames(Master master) {
        if(allFrames.containsKey(master)) {
            return allFrames.get(master);
        } else {
            List<Frame> frames = new ArrayList<Frame>();
            frames.addAll(master.getFrames());
            for(Slave slave:master.getSlaves())
                frames.addAll(slave.getFrames());
            allFrames.put(master,frames);
            return frames;
        }
    }

    @Override
    public List<Frame> getSubscribeFrames(Node node) {
        Master master = (Master) node;
        if (subscribeFrames.containsKey(master)) {
            return subscribeFrames.get(master);
        } else {
            List<Frame> frames = new ArrayList<Frame>();
            frames.addAll(super.getSubscribeFrames(node));
            for(Slave slave:master.getSlaves())
                frames.addAll(super.getPublishFrames(slave));
            subscribeFrames.put(master,frames);
            return frames;
        }
    }
}
