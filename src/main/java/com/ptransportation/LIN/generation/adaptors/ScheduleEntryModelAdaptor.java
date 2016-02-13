package com.ptransportation.LIN.generation.adaptors;


import com.ptransportation.LIN.model.Master;
import com.ptransportation.LIN.model.ScheduleTable;
import com.ptransportation.LIN.model.ScheduleTableEntry;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class ScheduleEntryModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        ScheduleTableEntry entry = (ScheduleTableEntry) o;
        ScheduleTable scheduleTable = entry.getScheduleTable();
        Master master = scheduleTable.getMaster();
        if (propertyName.equals("ticks"))
            return (int) Math.ceil(entry.getFrameTime() / master.getTimebase()); // TODO maybe add some more advanced things here?
        return super.getProperty(interp, self, o, property, propertyName);
    }
}
