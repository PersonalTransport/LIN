package com.ptransportation.LIN.models;

import com.ptransportation.LIN.signal.ArraySignalValue;
import com.ptransportation.LIN.signal.ScalarSignalValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;


public class SignalValueModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        // TODO let PolymorphismModelAdaptor take care of this
        if(propertyName.equals("values") && o instanceof ArraySignalValue)
            return ((ArraySignalValue)o).getValues();
        else if(propertyName.equals("value") && o instanceof ScalarSignalValue)
            return ((ScalarSignalValue)o).getValue();
        return super.getProperty(self, o, property, propertyName);
    }
}
