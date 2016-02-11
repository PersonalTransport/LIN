package com.ptransportation.LIN.generator.generation.models;


import com.ptransportation.capability.nodeCapabilityFile.Master;
import com.ptransportation.capability.nodeCapabilityFile.ScheduleTable;
import com.ptransportation.capability.nodeCapabilityFile.ScheduleTableEntry;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class ScheduleEntryModelAdaptor extends PolymorphismModelAdaptor {
    @Override
    public synchronized Object getProperty(ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        ScheduleTableEntry entry = (ScheduleTableEntry) o;
        ScheduleTable scheduleTable = (ScheduleTable) entry.eContainer();
        Master master = (Master) scheduleTable.eContainer();
        if (propertyName.equals("ticks")) {
            double frameTime = Double.parseDouble(entry.getFrameTime());
            double timebase = Double.parseDouble(master.getTimebase());
            return (int) Math.ceil(frameTime / timebase); // TODO maybe add some more advanced things here?
        }
        return super.getProperty(self, o, property, propertyName);
    }
}
