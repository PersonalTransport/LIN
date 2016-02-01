package com.ptransportation.LIN.models;

import com.ptransportation.LIN.Node;
import com.ptransportation.LIN.frame.Frame;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class FrameModelAdaptor extends ObjectModelAdaptor {
    public Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if(propertyName.startsWith("published_by_")) {
            String name = propertyName.replace("published_by_","");
            Object node = self.getAttribute(name);
            return (((Frame)o).publishedBy((Node)node));
        }
        return super.getProperty(self, o, property, propertyName);
    }
}
