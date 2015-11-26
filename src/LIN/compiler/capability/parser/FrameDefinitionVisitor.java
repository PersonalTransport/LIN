package LIN.compiler.capability.parser;

import LIN.Frame;
import LIN.Slave;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.FrameDefinitionContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.SignalDefinitionContext;

import static LIN.compiler.capability.parser.Util.convert;

public class FrameDefinitionVisitor extends NodeCapabilityFileBaseVisitor<Frame> {
    private final Slave slave;
    private Frame frame;

    FrameDefinitionVisitor(Slave slave) {
        this.slave = slave;
    }

    @Override
    public Frame visitFrameDefinition(FrameDefinitionContext ctx) {
        frame = new Frame(ctx.name.getText(), convert(ctx.length));

        if(ctx.minPeriod != null)
            frame.setMinimumPeriod(convert(ctx.minPeriod));
        if(ctx.maxPeriod != null)
            frame.setMaximumPeriod(convert(ctx.maxPeriod));

        if(ctx.eventTriggeredFrame != null)
            frame.setEventTriggeredFrame(ctx.eventTriggeredFrame.getText());

        if(ctx.signalsDefinition() != null) {
            SignalDefinitionVisitor signalVisitor = new SignalDefinitionVisitor(slave);
            for(SignalDefinitionContext signalCtx:ctx.signalsDefinition().signalDefinition())
                frame.addSignal(signalVisitor.visit(signalCtx));
        }

        return frame;
    }
}
