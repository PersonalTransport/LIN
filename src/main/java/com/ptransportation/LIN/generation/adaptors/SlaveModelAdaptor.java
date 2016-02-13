package com.ptransportation.LIN.generation.adaptors;


import com.ptransportation.LIN.model.NadList;
import com.ptransportation.LIN.model.NadRange;
import com.ptransportation.LIN.model.Slave;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class SlaveModelAdaptor extends NodeModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Slave slave = (Slave) o;
        if (propertyName.equals("initialNAD")) {
            // TODO add instance generation!
            if (slave.getNadSet() instanceof NadList) {
                NadList list = (NadList) slave.getNadSet();
                return list.getValues().get(0);
            }
            return ((NadRange) slave.getNadSet()).getMinValue();
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
