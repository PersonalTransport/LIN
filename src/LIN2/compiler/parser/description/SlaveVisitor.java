package LIN2.compiler.parser.description;

import LIN2.Cluster;
import LIN2.Slave;
import LIN2.frame.Frame;
import LIN2.compiler.parser.description.DescriptionFileParser.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SlaveVisitor extends DescriptionFileBaseVisitor<Slave> {
    private final DescriptionFileContext fileContext;
    private final Cluster cluster;

    public SlaveVisitor(DescriptionFileContext fileContext, Cluster cluster) {
        this.fileContext = fileContext;
        this.cluster = cluster;
    }

    @Override
    public Slave visitSlaveDefinition(SlaveDefinitionContext ctx) {
        Slave slave = cluster.getSlave(ctx.name.getText()); // TODO check for null!

        slave.setProtocol(ctx.protocolVersion.getText().replace("\"",""));
        slave.setConfiguredNAD(Util.convert(ctx.configuredNAD));
        if(ctx.initialNAD != null)
            slave.setInitialNAD(Util.convert(ctx.initialNAD));

        slave.setSupplier(Util.convert(ctx.supplierId));
        slave.setFunction(Util.convert(ctx.functionId));
        if(ctx.variant != null)
            slave.setVariant(Util.convert(ctx.variant));

        P2MinStMinNAsTimeoutNCrTimeoutContext timeoutCtx = ctx.p2MinStMinNAsTimeoutNCrTimeout();
        if(timeoutCtx.p2Min != null)
            slave.setP2Min(Util.convert(timeoutCtx.p2Min));
        if(timeoutCtx.stMin != null)
            slave.setSTMin(Util.convert(timeoutCtx.stMin));
        if(timeoutCtx.nAsTimeout != null)
            slave.setNAsTimeout(Util.convert(timeoutCtx.nAsTimeout));
        if(timeoutCtx.nCrTimeout != null)
            slave.setNCrTimeout(Util.convert(timeoutCtx.nCrTimeout));

        FrameVisitor frameVisitor = new FrameVisitor(fileContext, cluster);
        for(ConfigurableFrame21DefinitionContext configurableFrameCtx:ctx.configurableFrames21Definition().configurableFrame21Definition()) {
            Frame frame = cluster.getFrame(configurableFrameCtx.frameName.getText());
            if(frame == null)
                frame = frameVisitor.visit(findFrameDefinition(configurableFrameCtx.frameName.getText())); // TODO check for null!
            slave.addFrame(frame);
        }

        slave.setResponseErrorSignal(slave.getSignal(ctx.responseErrorSignal.getText())); // TODO check for null

        if(ctx.faultStateSignals != null) {
            for(TerminalNode signalName:ctx.faultStateSignals.Identifier())
                slave.addFaultStateSignal(slave.getSignal(signalName.getText())); // TODO check for null
        }

        return slave;
    }

    private ParserRuleContext findFrameDefinition(String frameName) {
        for(FrameDefinitionContext ctx:fileContext.framesDefinition().frameDefinition()) {
            if(ctx.frameName.getText().equals(frameName))
                return ctx;
        }
        for(EventTriggeredFrameDefinitionContext ctx:fileContext.eventTriggeredFramesDefinition().eventTriggeredFrameDefinition()) {
            if(ctx.eventTriggeredFrameName.getText().equals(frameName))
                return ctx;
        }
        return null;
    }
}
