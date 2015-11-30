package LIN2.compiler.generation.models;

import LIN2.signal.ArraySignalValue;
import LIN2.signal.Signal;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class SignalModelAdaptor extends ObjectModelAdaptor {

    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Signal signal = (Signal)o;
        if(propertyName.equals("signalWriteInitialValue")) {
            return getPrefix(signal)+"SignalWriteInitialValue";
        }
        else if(propertyName.startsWith("signalPrototype")) {
            return getPrefix(signal)+"SignalPrototype";
        }
        else if(propertyName.startsWith("signalImplementation")) {
            return getPrefix(signal)+"SignalImplementation";
        }
        else if(propertyName.equals("signalMask")) {
            int signalMask = 0;
            for(int i=0;i<signal.getSize();++i)
                signalMask |= (1 << i);
            return signalMask;
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }

    private String getPrefix(Signal signal) {
        if(signal.getInitialValue() instanceof ArraySignalValue) {
            return "bytes";
        } else if(signal.getSize() == 1) {
            return  "bool";
        } else if(signal.getSize() <= 8) {
            return  "u8";
        } else if(signal.getSize() <= 16) {
            return  "u16";
        }
        return "";
    }
}
