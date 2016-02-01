package com.ptransportation.LIN.models;

import com.ptransportation.LIN.Node;
import com.ptransportation.LIN.frame.Frame;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class NodeModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if(propertyName.startsWith("publishes_")) {
            Frame frame = (Frame)self.getAttribute(propertyName.replace("publishes_",""));
            return frame.publishedBy((Node) o);
        }
        return super.getProperty(self, o, property, propertyName);
    }
}
