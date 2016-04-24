package com.ptransportation.LIN.generation.adaptors;


import com.ptransportation.LIN.model.ArraySignalValue;
import com.ptransportation.LIN.model.Signal;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class SignalModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Signal signal = (Signal) o;
        if (propertyName.startsWith("signalType_")) {
            return getPrefix(signal) + propertyName.replace("signalType_", "");
        } else if (propertyName.equals("signalMask")) {
            int signalMask = 0;
            for (int i = 0; i < signal.getSize(); ++i)
                signalMask |= (1 << i);
            return signalMask;
        }
        else if (propertyName.equals("signalFullMask")) {
            long signalMask = 0;
            for (long i = 0; i < signal.getSize(); ++i)
                signalMask |= ((long)1 << i);
            return signalMask << (long)signal.getOffset();
        }

        return super.getProperty(interp, self, o, property, propertyName);
    }

    private String getPrefix(Signal signal) {
        int size = signal.getSize();
        if (signal.getInitialValue() instanceof ArraySignalValue)
            return "bytes";
        else if (size == 1)
            return "bool";
        else if (size <= 8)
            return "u8";
        else if (size <= 16)
            return "u16";
        return null;
    }
}
