package com.ptransportation.LIN.compiler.generation.models;


import com.ptransportation.capability.nodeCapabilityFile.NadList;
import com.ptransportation.capability.nodeCapabilityFile.NadRange;
import com.ptransportation.capability.nodeCapabilityFile.Slave;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlaveModelAdaptor extends NodeModelAdaptor {
    static final List<String> INTEGER_FIELDS = Arrays.asList("diagnosticClass", "maxMessageLength");

    static final List<String> DOUBLE_FIELDS = Arrays.asList("p2Min", "stMin","nAsTimeout","nCrTimeout");
    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        Slave slave = (Slave)o;
        if(propertyName.equals("initialNAD")) {
            // TODO add instance generation!
            if(slave.getNadSet() instanceof NadList) {
                NadList list = (NadList) slave.getNadSet();
                return Integer.decode(list.getValues().get(0));
            }
            return Integer.decode(((NadRange)slave.getNadSet()).getMinValue());
        }
        else if(propertyName.equals("supportedSIDS")) {
            ArrayList<Integer> supportedSIDS = new ArrayList<Integer>();
            for(String sid:slave.getSupportedSIDS())
                supportedSIDS.add(Integer.decode(sid));
            return supportedSIDS;
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
