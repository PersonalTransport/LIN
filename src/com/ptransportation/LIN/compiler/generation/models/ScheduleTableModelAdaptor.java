package com.ptransportation.LIN.compiler.generation.models;


import com.ptransportation.capability.nodeCapabilityFile.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class ScheduleTableModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        ScheduleTable scheduleTable = (ScheduleTable) o;
        if(propertyName.equals("size"))
            return scheduleTable.getEntries().size();
        return super.getProperty(self,o,property,propertyName);
    }
}
