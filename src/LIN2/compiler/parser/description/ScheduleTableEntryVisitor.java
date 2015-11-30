package LIN2.compiler.parser.description;

import LIN2.Cluster;
import LIN2.Slave;
import LIN2.compiler.parser.description.DescriptionFileParser.*;
import LIN2.frame.Frame;
import LIN2.schedule.*;

public class ScheduleTableEntryVisitor extends DescriptionFileBaseVisitor<Entry> {

    private final Cluster cluster;

    public ScheduleTableEntryVisitor(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public Entry visitUnconditionalEntry(UnconditionalEntryContext ctx) {
        Frame frame = cluster.getFrame(ctx.frameName.getText()); // TODO check for null.
        return new UnconditionalEntry(frame,
                                      Util.convert(ctx.frameTime));
    }

    @Override
    public Entry visitMasterReqEntry(MasterReqEntryContext ctx) {
        return new MasterReqEntry(Util.convert(ctx.frameTime));
    }

    @Override
    public Entry visitSlaveRespEntry(SlaveRespEntryContext ctx) {
        return new SlaveRespEntry(Util.convert(ctx.frameTime));
    }

    @Override
    public Entry visitAssignNADEntry(AssignNADEntryContext ctx) {
        Slave slave = cluster.getSlave(ctx.nodeName.getText()); // TODO check for null
        return new AssignNADEntry(slave,
                                  Util.convert(ctx.frameTime));
    }

    @Override
    public Entry visitConditionalChangeNADEntry(ConditionalChangeNADEntryContext ctx) {
        return new ConditionalChangeNADEntry(Util.convert(ctx.NAD),
                                             Util.convert(ctx.id),
                                             Util.convert(ctx.byteV),
                                             Util.convert(ctx.mask),
                                             Util.convert(ctx.inv),
                                             Util.convert(ctx.newNAD),
                                             Util.convert(ctx.frameTime));
    }

    @Override
    public Entry visitDataDumpEntry(DataDumpEntryContext ctx) {
        Slave slave = cluster.getSlave(ctx.nodeName.getText()); // TODO check for null
        return new DataDumpEntry(slave,
                                 Util.convert(ctx.D1),
                                 Util.convert(ctx.D2),
                                 Util.convert(ctx.D3),
                                 Util.convert(ctx.D4),
                                 Util.convert(ctx.D5),
                                 Util.convert(ctx.frameTime));
    }

    @Override
    public Entry visitSaveConfigurationEntry(SaveConfigurationEntryContext ctx) {
        Slave slave = cluster.getSlave(ctx.nodeName.getText()); // TODO check for null

        return new SaveConfigurationEntry(slave,
                                          Util.convert(ctx.frameTime));
    }

    @Override
    public Entry visitAssignFrameIdRangeEntry(AssignFrameIdRangeEntryContext ctx) {
        Slave slave = cluster.getSlave(ctx.nodeName.getText()); // TODO check for null

        AssignFrameIdRangeEntry entry = new AssignFrameIdRangeEntry(slave,
                                                                    Util.convert(ctx.startIndex),
                                                                    Util.convert(ctx.frameTime));
        if(ctx.PIDIndexP0 != null) {
            entry.setPIDs(Util.convert(ctx.PIDIndexP0),
                          Util.convert(ctx.PIDIndexP1),
                          Util.convert(ctx.PIDIndexP2),
                          Util.convert(ctx.PIDIndexP3));
        }
        return entry;
    }

    @Override
    public Entry visitFreeFormatEntry(FreeFormatEntryContext ctx) {
        return new FreeFormatEntry(Util.convert(ctx.d1),
                                   Util.convert(ctx.d2),
                                   Util.convert(ctx.d3),
                                   Util.convert(ctx.d4),
                                   Util.convert(ctx.d5),
                                   Util.convert(ctx.d6),
                                   Util.convert(ctx.d7),
                                   Util.convert(ctx.d8),
                                   Util.convert(ctx.frameTime));
    }

    @Override
    public Entry visitAssignFrameIdEntry(AssignFrameIdEntryContext ctx) {
        Slave slave = cluster.getSlave(ctx.nodeName.getText()); // TODO check for null
        Frame frame = slave.getFrame(ctx.frameName.getText()); // TODO check for null

        return new AssignFrameIdEntry(slave,
                                      frame,
                                      Util.convert(ctx.frameTime));
    }

}
