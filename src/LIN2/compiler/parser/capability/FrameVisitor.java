package LIN2.compiler.parser.capability;

import LIN2.Slave;
import LIN2.frame.EventTriggeredFrame;
import LIN2.frame.Frame;
import LIN2.frame.UnconditionalFrame;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.*;

public class FrameVisitor extends NodeCapabilityFileBaseVisitor<Frame> {
    private final NodeDefinitionContext nodeCtx;
    private final Slave slave;

    public FrameVisitor(NodeDefinitionContext nodeCtx, Slave slave) {
        this.nodeCtx = nodeCtx;
        this.slave = slave;
    }

    @Override
    public Frame visitFrameDefinition(NodeCapabilityFileParser.FrameDefinitionContext ctx) {
        boolean isPublisher = ctx.kind.getText().equals("publish");
        Frame frame;
        if(ctx.signalsDefinition() != null) {
            UnconditionalFrame uf = new UnconditionalFrame(ctx.name.getText());

            if(isPublisher)
                uf.setPublisher(slave);

            SignalVisitor signalVisitor = new SignalVisitor(nodeCtx,slave);
            for(SignalDefinitionContext signalCtx:ctx.signalsDefinition().signalDefinition())
                uf.addSignal(signalVisitor.visit(signalCtx));

            frame = uf;
        }
        else {
            frame = new EventTriggeredFrame(ctx.name.getText());
        }

        frame.setLength(Util.convert(ctx.length));
        if(ctx.minPeriod != null)
            frame.setMinPeriod(Util.convert(ctx.minPeriod));
        if(ctx.maxPeriod != null)
            frame.setMaxPeriod(Util.convert(ctx.maxPeriod));

        return frame;
    }
}
