package com.ptransportation.LIN.parser;


import com.ptransportation.LIN.ErrorModel;
import com.ptransportation.LIN.model.*;

import java.util.List;

public class NodeCapabilityFileLinker extends NodeCapabilityFileBaseVisitor<Void> {

    // TODO link subscribe frames to publish frames


    private List<NodeCapabilityFile> nodeCapabilityFiles;
    private ErrorModel errorModel;

    private Node node;
    private Slave slave;
    private Master master;
    private Frame frame;
    private ScheduleTable scheduleTable;
    private int entryIndex;

    public NodeCapabilityFileLinker(ErrorModel errorModel) {
        this.errorModel = errorModel;
    }

    // TODO check that subscribed frames,signals,and encodings match published frames,signals,and encodings.
    public void link(List<NodeCapabilityFileParser.NodeCapabilityFileContext> nodeCapabilityFileContexts, List<NodeCapabilityFile> nodeCapabilityFiles) {
        this.nodeCapabilityFiles = nodeCapabilityFiles;
        for (NodeCapabilityFileParser.NodeCapabilityFileContext fileContext : nodeCapabilityFileContexts)
            this.visit(fileContext);
        this.nodeCapabilityFiles = null;
        this.node = null;
        this.slave = null;
        this.master = null;
        this.frame = null;
        this.scheduleTable = null;
        this.entryIndex = 0;
    }

    @Override
    public Void visitMaster(NodeCapabilityFileParser.MasterContext ctx) {
        node = master = getMaster(ctx.name.getText());
        for (int i = 0; i < ctx.slaves.size(); ++i) {
            String slaveName = ctx.slaves.get(i).getText();
            Slave slave = getSlave(slaveName);
            if (slave != null) {
                master.getSlaves().add(slave);
                master.getFrames().addAll(slave.getPublishFrames());
            }
            else
                error("Slave '" + slaveName + "' was not defined.", master, "slaves", i);
        }
        return super.visitMaster(ctx);
    }

    @Override
    public Void visitScheduleTable(NodeCapabilityFileParser.ScheduleTableContext ctx) {
        scheduleTable = getScheduleTable(master, ctx.name.getText());
        for (int i = 0; i < ctx.entries.size(); ++i) {
            entryIndex = i;
            visitScheduleTableEntry(ctx.entries.get(i));
        }
        return null;
    }

    @Override
    public Void visitScheduleTableEntry(NodeCapabilityFileParser.ScheduleTableEntryContext ctx) {
        ScheduleTableEntry entry = (ScheduleTableEntry) scheduleTable.getEntries().get(entryIndex);
        entry.setScheduleTable(scheduleTable);
        return super.visitScheduleTableEntry(ctx);
    }

    @Override
    public Void visitFrameEntry(NodeCapabilityFileParser.FrameEntryContext ctx) {
        FrameEntry entry = (FrameEntry) scheduleTable.getEntries().get(entryIndex);

        Frame frame = getFrame(ctx.frameName.getText());
        if (frame != null)
            entry.setFrame(frame);
        else
            error("Frame '" + ctx.frameName.getText() + "' was not defined.", entry, "frame");

        return super.visitFrameEntry(ctx);
    }


    @Override
    public Void visitAssignNADEntry(NodeCapabilityFileParser.AssignNADEntryContext ctx) {
        AssignNADEntry entry = (AssignNADEntry) scheduleTable.getEntries().get(entryIndex);

        Slave slave = getSlave(ctx.slaveName.getText());
        if (slave != null)
            entry.setSlave(slave);
        else
            error("Slave '" + ctx.slaveName.getText() + "' was not defined.", entry, "slave");

        return super.visitAssignNADEntry(ctx);
    }

    @Override
    public Void visitSaveConfigurationEntry(NodeCapabilityFileParser.SaveConfigurationEntryContext ctx) {
        SaveConfigurationEntry entry = (SaveConfigurationEntry) scheduleTable.getEntries().get(entryIndex);

        Slave slave = getSlave(ctx.slaveName.getText());
        if (slave != null)
            entry.setSlave(slave);
        else
            error("Slave '" + ctx.slaveName.getText() + "' was not defined.", entry, "slave");

        return super.visitSaveConfigurationEntry(ctx);
    }

    @Override
    public Void visitAssignFrameIdRangeEntry(NodeCapabilityFileParser.AssignFrameIdRangeEntryContext ctx) {
        AssignFrameIdRangeEntry entry = (AssignFrameIdRangeEntry) scheduleTable.getEntries().get(entryIndex);

        Slave slave = getSlave(ctx.slaveName.getText());
        if (slave != null)
            entry.setSlave(slave);
        else
            error("Slave '" + ctx.slaveName.getText() + "' was not defined.", entry, "slave");

        return super.visitAssignFrameIdRangeEntry(ctx);
    }

    @Override
    public Void visitAssignFrameIdEntry(NodeCapabilityFileParser.AssignFrameIdEntryContext ctx) {
        AssignFrameIdEntry entry = (AssignFrameIdEntry) scheduleTable.getEntries().get(entryIndex);

        Slave slave = getSlave(ctx.slaveName.getText());
        if (slave != null)
            entry.setSlave(slave);
        else
            error("Slave '" + ctx.slaveName.getText() + "' was not defined.", entry, "slave");

        Frame frame = getFrame(ctx.frameName.getText());
        if (frame != null)
            entry.setFrame(frame);
        else
            error("Frame '" + ctx.frameName.getText() + "' was not defined.", entry, "frame");

        return super.visitAssignFrameIdEntry(ctx);
    }

    @Override
    public Void visitSlave(NodeCapabilityFileParser.SlaveContext ctx) {
        node = slave = getSlave(ctx.name.getText());

        Signal responseError = getSignal(slave, ctx.responseError.getText());
        if (responseError != null)
            slave.setResponseError(responseError);
        else
            error("Signal '" + ctx.responseError.getText() + "' was not defined.", slave, "responseError");

        for (int i = 0; i < ctx.faultStateSignals.size(); i++) {
            String faultStateSignalName = ctx.faultStateSignals.get(i).getText();
            Signal faultStateSignal = getSignal(slave, faultStateSignalName);
            if (faultStateSignal != null)
                slave.getFaultStateSignals().add(faultStateSignal);
            else
                error("Signal '" + faultStateSignalName + "' was not defined.", slave, "faultStateSignals", i);
        }

        return super.visitSlave(ctx);
    }

    @Override
    public Void visitFrame(NodeCapabilityFileParser.FrameContext ctx) {
        frame = getFrame(node, ctx.name.getText());
        return super.visitFrame(ctx);
    }

    @Override
    public Void visitSignal(NodeCapabilityFileParser.SignalContext ctx) {
        Signal signal = getSignal(frame, ctx.name.getText());

        if (ctx.encodingName != null) {
            String encodingName = ctx.encodingName.getText();
            Encoding encoding = getEncoding(node, encodingName);
            if (encoding != null)
                signal.setEncoding(encoding);
            else
                error("Encoding '" + encodingName + "' was not defined.", signal, "encoding");
        }
        return super.visitSignal(ctx);
    }

    private Master getMaster(String name) {
        for (NodeCapabilityFile file : nodeCapabilityFiles) {
            if (file.getNode() instanceof Master) {
                Master master = (Master) file.getNode();
                if (master.getName().equals(name))
                    return master;
            }
        }
        return null;
    }

    private Slave getSlave(String name) {
        for (NodeCapabilityFile file : nodeCapabilityFiles) {
            if (file.getNode() instanceof Slave) {
                Slave slave = (Slave) file.getNode();
                if (slave.getName().equals(name))
                    return slave;
            }
        }
        return null;
    }

    private Frame getFrame(Node node, String name) {
        for (Frame frame : node.getFrames()) {
            if (frame.getName().equals(name))
                return frame;
        }
        return null;
    }

    private Signal getSignal(Frame frame, String name) {
        for (Signal signal : frame.getSignals())
            if (signal.getName().equals(name))
                return signal;
        return null;
    }

    private Signal getSignal(Node node, String name) {
        for (Frame frame : node.getFrames()) {
            for (Signal signal : frame.getSignals())
                if (signal.getName().equals(name))
                    return signal;
        }
        return null;
    }

    private Encoding getEncoding(Node node, String name) {
        for (Encoding encoding : node.getEncodings()) {
            if (encoding.getName().equals(name))
                return encoding;
        }
        return null;
    }

    private ScheduleTable getScheduleTable(Master master, String name) {
        for (ScheduleTable scheduleTable : master.getScheduleTables()) {
            if (scheduleTable.getName().equals(name))
                return scheduleTable;
        }
        return null;
    }

    private Frame getFrame(String name) {
        for (NodeCapabilityFile file : nodeCapabilityFiles) {
            for (Frame frame : file.getNode().getFrames()) {
                if (frame.getName().equals(name) && frame.getPublisher() != null) // TODO this does not look right.
                    return frame;
            }
        }
        return null;
    }

    protected void error(String message, Object object, String field) {
        errorModel.error(message, object, field);
    }

    protected void error(String message, Object object, String field, int index) {
        errorModel.error(message, object, field, index);
    }
}
