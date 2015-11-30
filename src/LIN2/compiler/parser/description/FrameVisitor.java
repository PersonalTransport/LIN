package LIN2.compiler.parser.description;

import LIN2.Cluster;
import LIN2.Node;
import LIN2.frame.*;
import LIN2.compiler.parser.description.DescriptionFileParser.*;
import LIN2.signal.Signal;

public class FrameVisitor extends DescriptionFileBaseVisitor<Frame> {
    private final DescriptionFileContext fileContext;
    private final Cluster cluster;

    public FrameVisitor(DescriptionFileContext fileContext, Cluster cluster) {
        this.fileContext = fileContext;
        this.cluster = cluster;
    }

    @Override
    public Frame visitFrameDefinition(FrameDefinitionContext ctx) {
        UnconditionalFrame frame = new UnconditionalFrame(ctx.frameName.getText());

        Node publisher = cluster.getNode(ctx.publishedBy.getText()); // TODO check for null!

        frame.setID(Util.convert(ctx.frameId));
        frame.setPublisher(publisher);
        frame.setLength(Util.convert(ctx.frameSize));

        SignalVisitor signalVisitor = new SignalVisitor();
        for(SignalOffsetDefinitionContext signalOffsetDefinitionCtx:ctx.signalOffsetDefinition()) {
            Signal signal = cluster.getSignal(signalOffsetDefinitionCtx.signalName.getText());
            if(signal == null) {
                SignalDefinitionContext signalDefinitionCtx = findSignalDefinition(signalOffsetDefinitionCtx.signalName.getText()); // TODO check for null!
                signal = signalVisitor.visit(signalDefinitionCtx);
                signal.setOffset(Util.convert(signalOffsetDefinitionCtx.signalOffset));
                signal.setFrame(frame);
            }
            frame.addSignal(signal);
        }

        return frame;
    }

    @Override
    public Frame visitEventTriggeredFrameDefinition(EventTriggeredFrameDefinitionContext ctx) {
        EventTriggeredFrame frame = new EventTriggeredFrame(ctx.eventTriggeredFrameName.getText());
        frame.setID(Util.convert(ctx.frameId));
        return frame;
    }

    private SignalDefinitionContext findSignalDefinition(String signalName) {
        for(SignalDefinitionContext ctx:fileContext.signalsDefinition().signalDefinition()) {
            if(ctx.signalName.getText().equals(signalName))
                return ctx;
        }
        return null;
    }
}
