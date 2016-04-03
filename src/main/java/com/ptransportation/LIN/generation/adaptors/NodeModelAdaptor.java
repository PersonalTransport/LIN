package com.ptransportation.LIN.generation.adaptors;

import com.ptransportation.LIN.model.Frame;
import com.ptransportation.LIN.model.Master;
import com.ptransportation.LIN.model.Node;
import com.ptransportation.LIN.model.Slave;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.List;

public class NodeModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Node node = (Node) o;
        if (propertyName.equals("slave")) {
            return o instanceof Slave;
        } else if (propertyName.equals("master")) {
            return o instanceof Master;
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
