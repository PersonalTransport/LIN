package com.ptransportation.LIN.generator.generation.models;


import com.ptransportation.capability.nodeCapabilityFile.ArraySignalValue;
import com.ptransportation.capability.nodeCapabilityFile.Frame;
import com.ptransportation.capability.nodeCapabilityFile.Signal;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.Arrays;
import java.util.List;

public class SignalModelAdaptor extends PolymorphismModelAdaptor {
    static final List<String> INTEGER_FIELDS = Arrays.asList("size", "offset");

    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Signal signal = (Signal)o;
        if(propertyName.startsWith("signalType_")) {
            return  getPrefix(signal) + propertyName.replace("signalType_","");
        }
        else if(propertyName.equals("signalMask")) {
            int signalMask = 0;
            for(int i=0;i<Integer.decode(signal.getSize());++i)
                signalMask |= (1 << i);
            return signalMask;
        }
        else if(propertyName.startsWith("frame")) {
            return (Frame)signal.eContainer();
        }
        else if(INTEGER_FIELDS.contains(propertyName)) {
            String str = (String)super.getProperty(self, o, property, propertyName);
            if(str != null)
                return Integer.decode(str);
            return null;
        }

        return super.getProperty(self, o, property, propertyName);
    }

    private String getPrefix(Signal signal) {
        int size = Integer.decode(signal.getSize());
        if(signal.getInitialValue() instanceof ArraySignalValue) {
            return "bytes";
        } else if(size == 1) {
            return  "bool";
        } else if(size <= 8) {
            return  "u8";
        } else if(size <= 16) {
            return  "u16";
        }
        // TODO error?
        return "";
    }
}
