package LIN2.compiler.parser.description;

import LIN2.Cluster;
import LIN2.compiler.parser.description.DescriptionFileParser.ScheduleTableDefinitionContext;
import LIN2.compiler.parser.description.DescriptionFileParser.ScheduleTableEntryContext;
import LIN2.schedule.ScheduleTable;

public class ScheduleTableVisitor extends DescriptionFileBaseVisitor<ScheduleTable> {
    private final Cluster cluster;

    public ScheduleTableVisitor(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public ScheduleTable visitScheduleTableDefinition(ScheduleTableDefinitionContext ctx) {
        ScheduleTable schedule = new ScheduleTable(ctx.tableName.getText());

        ScheduleTableEntryVisitor entryVisitor = new ScheduleTableEntryVisitor(cluster);
        for(ScheduleTableEntryContext entryCtx:ctx.scheduleTableEntry())
            schedule.addEntry(entryVisitor.visit(entryCtx));

        return schedule;
    }
}
