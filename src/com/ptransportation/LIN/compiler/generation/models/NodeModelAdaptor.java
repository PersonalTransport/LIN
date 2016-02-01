package com.ptransportation.LIN.compiler.generation.models;


import com.ptransportation.LIN.models.PolymorphismModelAdaptor;
import com.ptransportation.capability.nodeCapabilityFile.Node;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class NodeModelAdaptor extends PolymorphismModelAdaptor {
    static final List<String> INTEGER_FIELDS = Arrays.asList("supplier", "function","variant","diagnosticClass", "maxMessageLength");

    static final List<String> DOUBLE_FIELDS = Arrays.asList(
            "p2Min", "stMin","nAsTimeout","nCrTimeout");
    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Node node = (Node)o;

        if(propertyName.equals("supportedSIDS")) {
            ArrayList<Integer> supportedSIDS = new ArrayList<Integer>();
            for(String sid:node.getSupportedSIDS())
                supportedSIDS.add(Integer.decode(sid));
            return supportedSIDS;
        }
        else if(propertyName.equals("slave")) {
            return true; // TODO make this not hardcoded.
        }
        else if(propertyName.equals("master")) {
            return false; // TODO make this not hardcoded.
        }
        else if(INTEGER_FIELDS.contains(propertyName)) {
            String str = (String)super.getProperty(self, o, property, propertyName);
            if(str != null)
                return Integer.decode(str);
            return null;
        }
        else if(DOUBLE_FIELDS.contains(propertyName)) {
            String str = (String)super.getProperty(self, o, property, propertyName);
            if(str != null)
                return Double.parseDouble(str);
            return null;
        }
        return super.getProperty(self, o, property, propertyName);
    }
}
