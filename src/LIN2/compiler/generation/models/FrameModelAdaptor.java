package LIN2.compiler.generation.models;

import LIN2.Node;
import LIN2.frame.Frame;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;


public class FrameModelAdaptor extends ObjectModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if(propertyName.startsWith("published_by_")) {
            String name = propertyName.replace("published_by_","");
            Object node = self.getAttribute(name);
            return (((Frame)o).publishedBy((Node)node));
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
