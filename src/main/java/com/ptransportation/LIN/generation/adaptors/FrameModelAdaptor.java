package com.ptransportation.LIN.generation.adaptors;


import com.ptransportation.LIN.model.Frame;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class FrameModelAdaptor extends PolymorphismModelAdaptor {

    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Frame frame = (Frame) o;
        if (propertyName.equals("useClassicChecksum"))
            return frame.getNode().getProtocolVersion() < 2.0;
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
