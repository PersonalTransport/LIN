package com.ptransportation.LIN.generator.generation.models;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class IntegerModelAdaptor extends ObjectModelAdaptor {
    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Integer value = (Integer)o;
        if(propertyName.equals("hex"))
            return "0x"+Integer.toHexString(value).toUpperCase();

        return super.getProperty(self, o, property, propertyName);
    }
}
