package LIN2.compiler.parser.description;

import LIN2.Cluster;
import LIN2.Master;
import LIN2.Slave;
import LIN2.compiler.parser.description.DescriptionFileParser.*;
import LIN2.encoding.Encoding;
import LIN2.frame.EventTriggeredFrame;
import LIN2.frame.Frame;
import LIN2.frame.UnconditionalFrame;
import LIN2.signal.Signal;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ClusterVisitor extends DescriptionFileBaseVisitor<Cluster> {
    private DescriptionFileContext fileContext;
    @Override
    public Cluster visitDescriptionFile(DescriptionFileContext ctx) {
        fileContext = ctx;

        Cluster cluster = new Cluster();
        Master master = new Master(ctx.nodesDefinition().masterName.getText(),
                                   Util.convert(ctx.bitrate().value),
                                   Util.convert(ctx.nodesDefinition().timeBase),
                                   Util.convert(ctx.nodesDefinition().jitter));
        if(ctx.channelName != null)
            master.setChannelName(ctx.channelName.getText());
        cluster.setMaster(master);

        master.setProtocol(ctx.protocolVersion.getText().replace("\"",""));

        for(TerminalNode slaveName:ctx.nodesDefinition().slaves.Identifier())
            cluster.addSlave(new Slave(slaveName.getText()));

        SlaveVisitor slaveVisitor = new SlaveVisitor(ctx, cluster);
        for(SlaveDefinitionContext slaveCtx:ctx.nodesAttributesDefinition().slaveDefinition())
            slaveVisitor.visit(slaveCtx);

        for(Frame frame:cluster.getFrames())
            master.addFrame(frame);

        ScheduleTableVisitor scheduleTableVisitor = new ScheduleTableVisitor(cluster);
        for(ScheduleTableDefinitionContext scheduleTableCtx:ctx.scheduleTablesDefinition().scheduleTableDefinition())
            master.addScheduleTable(scheduleTableVisitor.visit(scheduleTableCtx));

        for(EventTriggeredFrameDefinitionContext evtFrmCtx:ctx.eventTriggeredFramesDefinition().eventTriggeredFrameDefinition()) {
            EventTriggeredFrame frame = (EventTriggeredFrame)cluster.getFrame(evtFrmCtx.eventTriggeredFrameName.getText());
            frame.setScheduleTable(master.getScheduleTable(evtFrmCtx.collisionST.getText()));

            for(TerminalNode frameNameCtx:evtFrmCtx.frameNames.Identifier())
                frame.addAssociatedFrame((UnconditionalFrame) cluster.getFrame(frameNameCtx.getText())); // TODO check that the frame was found and unconditional!
        }

        EncodingVisitor encodingVisitor = new EncodingVisitor();
        for(SignalRepresentationDefinitionContext signalRepCtx:ctx.signalRepresentationsDefinition().signalRepresentationDefinition()) {
            Encoding encoding = cluster.getEncoding(signalRepCtx.signalEncodingTypeName.getText());
            if(encoding == null) {
                EncodingDefinitionContext encodingCtx = findEncodingDefinition(signalRepCtx.signalEncodingTypeName.getText()); // TODO check for null!
                encoding = encodingVisitor.visit(encodingCtx);
            }
            for(TerminalNode signalName:signalRepCtx.signals.Identifier()) {
                Signal signal = cluster.getSignal(signalName.getText()); // TODO check for null!
                signal.setEncoding(encoding);
            }
        }

        return cluster;
    }

    private EncodingDefinitionContext findEncodingDefinition(String encodingName) {
        for(EncodingDefinitionContext ctx:fileContext.signalEncodingTypesDefinition().encodingDefinition()) {
            if(ctx.encodingName.getText().equals(encodingName))
                return ctx;
        }
        return null;
    }
}
