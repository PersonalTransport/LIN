package com.ptransportation.LIN.generation.adaptors;


import com.ptransportation.LIN.model.Frame;
import com.ptransportation.LIN.model.Master;
import com.ptransportation.LIN.model.Node;
import com.ptransportation.LIN.model.Slave;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class NodeModelAdaptor extends PolymorphismModelAdaptor {
    private final HashMap<Node, List<Frame>> publishFrames = new HashMap<Node, List<Frame>>();
    private final HashMap<Node, List<Frame>> subscribeFrames = new HashMap<Node, List<Frame>>();

    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Node node = (Node) o;
        if (propertyName.equals("slave")) {
            return o instanceof Slave;
        } else if (propertyName.equals("master")) {
            return o instanceof Master;
        } else if (propertyName.equals("publish_frames")) {
            return getPublishFrames(node);
        } else if (propertyName.equals("subscribe_frames")) {
            return getSubscribeFrames(node);
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }

    public List<Frame> getPublishFrames(Node node) {
        if (publishFrames.containsKey(node)) {
            return publishFrames.get(node);
        } else {
            List<Frame> frames = new ArrayList<Frame>();
            for (Frame frame : node.getFrames()) {
                if (frame.getPublishes())
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
                if (frame.getSubscribes())
                    frames.add(frame);
            }
            subscribeFrames.put(node, frames);
            return frames;
        }
    }
}
