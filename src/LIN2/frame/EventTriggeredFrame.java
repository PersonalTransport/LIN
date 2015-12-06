package LIN2.frame;

import LIN2.Node;
import LIN2.schedule.ScheduleTable;

import java.util.ArrayList;

public class EventTriggeredFrame extends Frame {
    private ArrayList<UnconditionalFrame> associatedFrames;
    private ScheduleTable scheduleTable;

    public EventTriggeredFrame(String name) {
        super(name);
        this.associatedFrames = new ArrayList<>();
    }

    public void addAssociatedFrame(UnconditionalFrame frame) {
        if(!associatedFrames.contains(frame)) {
            this.associatedFrames.add(frame);
            frame.setAssociatedEventTriggeredFrame(this);
        }
    }

    public ArrayList<UnconditionalFrame> getAssociatedFrames() {
        return associatedFrames;
    }

    public void setScheduleTable(ScheduleTable scheduleTable) {
        this.scheduleTable = scheduleTable;
    }

    public ScheduleTable getScheduleTable() {
        return scheduleTable;
    }

    @Override
    public boolean publishedBy(Node node) {
        for(UnconditionalFrame frame:associatedFrames) {
            if(frame.publishedBy(node))
                return true;
        }
        return false;
    }
}
