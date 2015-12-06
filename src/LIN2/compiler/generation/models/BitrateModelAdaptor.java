package LIN2.compiler.generation.models;

import LIN2.bitrate.AutomaticBitrate;
import LIN2.bitrate.FixedBitrate;
import LIN2.bitrate.SelectBitrate;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;


public class BitrateModelAdaptor extends ObjectModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if(propertyName.equals("bitrateDefinition")) {
            if(o instanceof FixedBitrate)
                return "fixedBitrateDefinition";
            else if(o instanceof SelectBitrate)
                return "selectBitrateDefinition";
            else if(o instanceof AutomaticBitrate)
                return "automaticBitrateDefinition";
        }
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
