package LIN.compiler;

import LIN.*;
import static LIN.compiler.NodeUtil.*;

class FrameDefinitionVisitor extends NodeCapabilityFileBaseVisitor<Frame> {
    private final Slave slave;
    private Frame frame;

    FrameDefinitionVisitor(Slave slave) {
        this.slave = slave;
    }

    @Override
    public Frame visitFrameDefinition(NodeCapabilityFileParser.FrameDefinitionContext ctx) {
        frame = new Frame(ctx.name.getText(),ctx.kind.getText(),convert(ctx.length));

        if(ctx.minPeriod != null)
            frame.setMinimumPeriod(convert(ctx.minPeriod));
        if(ctx.maxPeriod != null)
            frame.setMaximumPeriod(convert(ctx.maxPeriod));

        if(ctx.eventTriggeredFrame != null)
            frame.setEventTriggeredFrame(ctx.eventTriggeredFrame.getText());

        if(ctx.signalsDefinition() != null) {
            SignalDefinitionVisitor signalVisitor = new SignalDefinitionVisitor(slave);
            for(NodeCapabilityFileParser.SignalDefinitionContext signalCtx:ctx.signalsDefinition().signalDefinition())
                frame.addSignal(signalVisitor.visit(signalCtx));
        }

        return frame;
    }
}