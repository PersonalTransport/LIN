package LIN.compiler.capability;

import LIN.Frame;
import LIN.Slave;
import LIN.compiler.description.DescriptionFileParser;

import static LIN.compiler.capability.Util.convert;

class FrameDefinitionVisitor extends NodeCapabilityFileBaseVisitor<Frame> {
    private final Slave slave;
    private Frame frame;

    FrameDefinitionVisitor(Slave slave) {
        this.slave = slave;
    }

    @Override
    public Frame visitFrameDefinition(NodeCapabilityFileParser.FrameDefinitionContext ctx) {
        frame = new Frame(ctx.name.getText(),convert(ctx.length));

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