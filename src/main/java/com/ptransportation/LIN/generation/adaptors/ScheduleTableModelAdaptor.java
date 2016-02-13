package com.ptransportation.LIN.generation.adaptors;


import com.ptransportation.LIN.model.ScheduleTable;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class ScheduleTableModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        ScheduleTable scheduleTable = (ScheduleTable) o;
        if (propertyName.equals("size"))
            return scheduleTable.getEntries().size();
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
