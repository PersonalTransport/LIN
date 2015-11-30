package LIN2.compiler.generation.models;

import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class FrameEntryModelAdaptor extends ObjectModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws
    STNoSuchPropertyException {
        if(propertyName.equals("type")) {
            String name = o.getClass().getSimpleName();
            name = Character.toLowerCase(name.charAt(0))+name.substring(1);
            return  name + "Definition";
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
