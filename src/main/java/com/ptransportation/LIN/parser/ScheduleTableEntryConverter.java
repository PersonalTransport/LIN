package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.*;

public class ScheduleTableEntryConverter extends NodeCapabilityFileBaseVisitor<ScheduleTableEntry> {
    @Override
    public ScheduleTableEntry visitFrameEntry(NodeCapabilityFileParser.FrameEntryContext ctx) {
        FrameEntry entry = new FrameEntry();
        entry.setFrame(new FrameReference(ctx.frameReference().getText()));
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;
    }

    @Override
    public ScheduleTableEntry visitMasterReqEntry(NodeCapabilityFileParser.MasterReqEntryContext ctx) {
        MasterReqEntry entry = new MasterReqEntry();
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;
    }

    @Override
    public ScheduleTableEntry visitSlaveRespEntry(NodeCapabilityFileParser.SlaveRespEntryContext ctx) {
        SlaveRespEntry entry = new SlaveRespEntry();
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;

    }

    @Override
    public ScheduleTableEntry visitAssignNADEntry(NodeCapabilityFileParser.AssignNADEntryContext ctx) {
        AssignNADEntry entry = new AssignNADEntry();
        entry.setSlave(new SlaveReference(ctx.slaveReference().getText()));
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;
    }

    @Override
    public ScheduleTableEntry visitConditionalChangeNADEntry(NodeCapabilityFileParser.ConditionalChangeNADEntryContext ctx) {
        ConditionalChangeNADEntry entry = new ConditionalChangeNADEntry();
        entry.setNAD(Integer.decode(ctx.NAD.getText()));
        entry.setId(Integer.decode(ctx.id.getText()));
        entry.setByte(Integer.decode(ctx.byte_.getText()));
        entry.setMask(Integer.decode(ctx.mask.getText()));
        entry.setInv(Integer.decode(ctx.inv.getText()));
        entry.setNewNAD(Integer.decode(ctx.newNAD.getText()));
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;

    }

    @Override
    public ScheduleTableEntry visitDataDumpEntry(NodeCapabilityFileParser.DataDumpEntryContext ctx) {
        DataDumpEntry entry = new DataDumpEntry();
        entry.setSlave(new SlaveReference(ctx.slaveReference().getText()));
        entry.setD1(Integer.decode(ctx.d1.getText()));
        entry.setD2(Integer.decode(ctx.d2.getText()));
        entry.setD3(Integer.decode(ctx.d3.getText()));
        entry.setD4(Integer.decode(ctx.d4.getText()));
        entry.setD5(Integer.decode(ctx.d5.getText()));
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;
    }

    @Override
    public ScheduleTableEntry visitSaveConfigurationEntry(NodeCapabilityFileParser.SaveConfigurationEntryContext ctx) {
        SaveConfigurationEntry entry = new SaveConfigurationEntry();
        entry.setSlave(new SlaveReference(ctx.slaveReference().getText()));
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;
    }

    @Override
    public ScheduleTableEntry visitAssignFrameIdRangeEntry(NodeCapabilityFileParser.AssignFrameIdRangeEntryContext ctx) {
        AssignFrameIdRangeEntry entry = new AssignFrameIdRangeEntry();
        entry.setSlave(new SlaveReference(ctx.slaveReference().getText()));
        entry.setStartIndex(Integer.decode(ctx.startIndex.getText()));
        if(ctx.PID0 != null) {
            entry.setLookupIDs(false);
            entry.setPID0(Integer.decode(ctx.PID0.getText()));
            entry.setPID1(Integer.decode(ctx.PID1.getText()));
            entry.setPID2(Integer.decode(ctx.PID2.getText()));
            entry.setPID3(Integer.decode(ctx.PID3.getText()));
        }
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;
    }

    @Override
    public ScheduleTableEntry visitFreeFormatEntry(NodeCapabilityFileParser.FreeFormatEntryContext ctx) {
        FreeFormatEntry entry = new FreeFormatEntry();
        entry.setD1(Integer.decode(ctx.d1.getText()));
        entry.setD2(Integer.decode(ctx.d2.getText()));
        entry.setD3(Integer.decode(ctx.d3.getText()));
        entry.setD4(Integer.decode(ctx.d4.getText()));
        entry.setD5(Integer.decode(ctx.d5.getText()));
        entry.setD6(Integer.decode(ctx.d6.getText()));
        entry.setD7(Integer.decode(ctx.d7.getText()));
        entry.setD8(Integer.decode(ctx.d8.getText()));
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;
    }

    @Override
    public ScheduleTableEntry visitAssignFrameIdEntry(NodeCapabilityFileParser.AssignFrameIdEntryContext ctx) {
        AssignFrameIdEntry entry = new AssignFrameIdEntry();
        entry.setSlave(new SlaveReference(ctx.slaveReference().getText()));
        entry.setFrame(new FrameReference(ctx.frameReference().getText()));
        entry.setFrameTime(Double.parseDouble(ctx.frameTime.getText()));
        return entry;
    }
}
