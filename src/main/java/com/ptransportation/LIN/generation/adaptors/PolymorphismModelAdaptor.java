package com.ptransportation.LIN.generation.adaptors;

import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class PolymorphismModelAdaptor extends ObjectModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if (propertyName.startsWith("polymorphic_")) {
            String name = o.getClass().getSimpleName();
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            return name + propertyName.replace("polymorphic_", "");
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
