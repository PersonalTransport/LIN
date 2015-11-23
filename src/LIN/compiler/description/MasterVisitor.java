package LIN.compiler.description;

import LIN.Frame;
import LIN.Master;
import LIN.Slave;
import LIN.bitrate.FixedBitrate;
import LIN.compiler.capability.NodeCapabilityFileParser;
import LIN.signal.Signal;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;

import static LIN.compiler.description.Util.convert;

class MasterVisitor extends DescriptionFileBaseVisitor<Master> {
    private Master master;

    @Override
    public Master visitDescriptionFile(DescriptionFileParser.DescriptionFileContext ctx) {
        DescriptionFileParser.NodesDefinitionContext nodesDefinitionCtx = ctx.nodesDefinition();

        master = new Master(nodesDefinitionCtx.masterName.getText(),convert(nodesDefinitionCtx.timeBase),convert(nodesDefinitionCtx.jitter));

        master.setProtocolVersion(ctx.protocolVersion.getText().replace("\"",""));
        master.setLanguageVersion(ctx.languageVersion.getText().replace("\"",""));
        master.setBitrate(new FixedBitrate(convert(ctx.bitrate().value)));

        if(ctx.channelName != null)
            master.setChannelName(ctx.channelName.getText());

        for(TerminalNode slaveID:nodesDefinitionCtx.slaves.Identifier())
            master.addSlave(new Slave(slaveID.getText()));

        if(ctx.nodeCompositionDefinition() != null)
            throw new RuntimeException("Node composition is not implemented!"); // TODO better error handling here.


        for(DescriptionFileParser.FrameDefinitionContext frameCtx : ctx.framesDefinition().frameDefinition()) {
            Frame frame = new Frame(frameCtx.frameName.getText(),convert(frameCtx.frameSize));
        }

        /*HashMap<String,Frame> frames = new HashMap<>();
        for(DescriptionFileParser.FrameDefinitionContext frameCtx : ctx.framesDefinition().frameDefinition()) {
            Slave slave = master.getSlave(frameCtx.publishedBy.getText());

            if(slave == null)
                throw new IllegalArgumentException("Node \""+frameCtx.publishedBy.getText()+"\" is not defined!"); // TODO better error handling here.

            slave.addPublishingFrame(new FrameDefinitionVisitor(master).visit(frameCtx));
        }

        HashMap<String,Signal> signals = new HashMap<>();
        SignalDefinitionVisitor signalVisitor = new SignalDefinitionVisitor(master,signals);
        for(DescriptionFileParser.SignalDefinitionContext signalCtx:ctx.signalsDefinition().signalDefinition())
            signalVisitor.visit(signalCtx);*/



        return master;
    }
}
