package LIN2.compiler.generation.models;

import LIN2.Node;
import LIN2.frame.Frame;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class NodeModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if(propertyName.startsWith("publishes_")) {
            Frame frame = (Frame)self.getAttribute(propertyName.replace("publishes_",""));
            return frame.publishedBy((Node)o);
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
