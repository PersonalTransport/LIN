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

public class MasterModelAdaptor extends NodeModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Master master = (Master) o;
        if (propertyName.equals("frames"))
            return getFrames(master);
        return super.getProperty(interp, self, o, property, propertyName);
    }

    public List<Frame> getFrames(Master master) {
        List<Frame> frames = new ArrayList<Frame>();
        frames.addAll(master.getPublishFrames());
        for (Slave slave : master.getSlaves()) {
            frames.addAll(slave.getPublishFrames());
        }
        return frames;
    }
}
