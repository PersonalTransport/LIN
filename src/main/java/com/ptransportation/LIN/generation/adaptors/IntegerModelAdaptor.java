package com.ptransportation.LIN.generation.adaptors;

import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class IntegerModelAdaptor extends ObjectModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if (o instanceof Integer) {
            Integer value = (Integer) o;
            if (propertyName.equals("hex"))
                return "0x" + Integer.toHexString(value).toUpperCase();
        } else if (o instanceof Long) {
            Long value = (Long) o;
            if (propertyName.equals("hex"))
                return "0x" + Long.toHexString(value).toUpperCase();
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
