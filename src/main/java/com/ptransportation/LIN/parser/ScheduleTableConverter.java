package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.ScheduleTable;

public class ScheduleTableConverter extends NodeCapabilityFileBaseVisitor<ScheduleTable> {
    private ScheduleTableEntryConverter scheduleTableEntryConverter;

    public ScheduleTableConverter() {
        this.scheduleTableEntryConverter = new ScheduleTableEntryConverter();
    }

    @Override
    public ScheduleTable visitScheduleTable(NodeCapabilityFileParser.ScheduleTableContext ctx) {
        ScheduleTable scheduleTable = new ScheduleTable(ctx.name.getText());
        for (NodeCapabilityFileParser.ScheduleTableEntryContext entry : ctx.entries)
            scheduleTable.getEntries().add(scheduleTableEntryConverter.visit(entry));
        return scheduleTable;
    }
}
