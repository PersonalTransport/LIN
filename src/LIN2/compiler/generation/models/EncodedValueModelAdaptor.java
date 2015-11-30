package LIN2.compiler.generation.models;

import LIN2.encoding.ASCIIEncodedValue;
import LIN2.encoding.BCDEncodedValue;
import LIN2.encoding.LogicalEncodedValue;
import LIN2.encoding.PhysicalEncodedRange;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class EncodedValueModelAdaptor extends ObjectModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws
    STNoSuchPropertyException {
        if(propertyName.equals("encodedValueDefinition")) {
            if(o instanceof ASCIIEncodedValue)
                return "asciiEncodedValueDefinition";
            else if(o instanceof BCDEncodedValue)
                return "bcdEncodedValueDefinition";
            else if(o instanceof LogicalEncodedValue)
                return "logicalEncodedValueDefinition";
            else if(o instanceof PhysicalEncodedRange)
                return "physicalEncodedValueDefinition";
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
