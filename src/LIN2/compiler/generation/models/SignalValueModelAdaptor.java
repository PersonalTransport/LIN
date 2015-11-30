package LIN2.compiler.generation.models;

import LIN2.signal.ArraySignalValue;
import LIN2.signal.ScalarSignalValue;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;


public class SignalValueModelAdaptor extends ObjectModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if(propertyName.equals("values") && o instanceof ArraySignalValue)
            return ((ArraySignalValue)o).getValues();
        else if(propertyName.equals("value") && o instanceof ScalarSignalValue)
            return ((ScalarSignalValue)o).getValue();
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
