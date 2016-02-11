package com.ptransportation.LIN.generator.generation.models;


import com.ptransportation.capability.nodeCapabilityFile.Frame;
import com.ptransportation.capability.nodeCapabilityFile.Master;
import com.ptransportation.capability.nodeCapabilityFile.Node;
import com.ptransportation.capability.nodeCapabilityFile.Slave;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class NodeModelAdaptor extends PolymorphismModelAdaptor {
    static final List<String> INTEGER_FIELDS = Arrays.asList("supplier", "function", "variant");
    private final HashMap<Node, List<Frame>> publishFrames = new HashMap<Node, List<Frame>>();
    private final HashMap<Node, List<Frame>> subscribeFrames = new HashMap<Node, List<Frame>>();

    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Node node = (Node) o;

        if (propertyName.equals("slave")) {
            return o instanceof Slave;
        } else if (propertyName.equals("master")) {
            return o instanceof Master;
        } else if (propertyName.equals("publish_frames")) {
            return getPublishFrames(node);
        } else if (propertyName.equals("subscribe_frames")) {
            return getSubscribeFrames(node);
        } else if (INTEGER_FIELDS.contains(propertyName)) {
            String str = (String) super.getProperty(self, o, property, propertyName);
            if (str != null)
                return Integer.decode(str);
            return null;
        }
        return super.getProperty(self, o, property, propertyName);
    }

    public List<Frame> getPublishFrames(Node node) {
        if (publishFrames.containsKey(node)) {
            return publishFrames.get(node);
        } else {
            List<Frame> frames = new ArrayList<Frame>();
            for (Frame frame : node.getFrames()) {
                if (frame.getPublishes() != null)
                    frames.add(frame);
            }
            publishFrames.put(node, frames);
            return frames;
        }
    }

    public List<Frame> getSubscribeFrames(Node node) {
        if (subscribeFrames.containsKey(node)) {
            return subscribeFrames.get(node);
        } else {
            List<Frame> frames = new ArrayList<Frame>();
            for (Frame frame : node.getFrames()) {
                if (frame.getSubscribes() != null)
                    frames.add(frame);
            }
            subscribeFrames.put(node, frames);
            return frames;
        }
    }
}
