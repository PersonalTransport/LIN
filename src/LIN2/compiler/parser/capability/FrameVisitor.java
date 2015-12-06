package LIN2.compiler.parser.capability;

import LIN2.Cluster;
import LIN2.Slave;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.NodeDefinitionContext;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.SignalDefinitionContext;
import LIN2.frame.EventTriggeredFrame;
import LIN2.frame.Frame;
import LIN2.frame.UnconditionalFrame;

public class FrameVisitor extends NodeCapabilityFileBaseVisitor<Frame> {
    private final NodeDefinitionContext nodeCtx;
    private final Cluster cluster;
    private final Slave slave;

    public FrameVisitor(NodeDefinitionContext nodeCtx, Cluster cluster, Slave slave) {
        this.nodeCtx = nodeCtx;
        this.cluster = cluster;
        this.slave = slave;
    }

    @Override
    public Frame visitFrameDefinition(NodeCapabilityFileParser.FrameDefinitionContext ctx) {
        Frame frame;
        if(ctx.signalsDefinition() != null) {
            UnconditionalFrame uf = cluster.getUnconditionalFrame(ctx.name.getText());
            if(uf == null) {
                uf = new UnconditionalFrame(ctx.name.getText());
            }

            if(ctx.kind.getText().equals("publish"))
                uf.setPublisher(slave);

            SignalVisitor signalVisitor = new SignalVisitor(nodeCtx, cluster, uf);
            for(SignalDefinitionContext signalCtx:ctx.signalsDefinition().signalDefinition())
                uf.addSignal(signalVisitor.visit(signalCtx));

            frame = uf;
        }
        else {
            frame = cluster.getEventTriggeredFrame(ctx.name.getText());
            if(frame == null) {
                frame = new EventTriggeredFrame(ctx.name.getText());
            }
        }

        frame.setLength(Util.convert(ctx.length));
        if(ctx.minPeriod != null)
            frame.setMinPeriod(Util.convert(ctx.minPeriod));
        if(ctx.maxPeriod != null)
            frame.setMaxPeriod(Util.convert(ctx.maxPeriod));



        // TODO check that the frame defintions are compatable!

        return frame;
    }
}
