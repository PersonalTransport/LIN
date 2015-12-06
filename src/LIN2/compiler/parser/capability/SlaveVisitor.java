package LIN2.compiler.parser.capability;

import LIN2.Slave;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.*;
import LIN2.util.Range;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashSet;

public class SlaveVisitor extends NodeCapabilityFileBaseVisitor<Slave> {
    @Override
    public Slave visitNodeDefinition(NodeDefinitionContext ctx) {
        Slave slave =  new Slave(ctx.nodeName.getText());

        GeneralDefinitionContext generalCtx = ctx.generalDefinition();
        slave.setProtocol(generalCtx.protocol.getText().replace("\"",""));

        slave.setSupplier(Util.convert(generalCtx.supplier));
        slave.setFunction(Util.convert(generalCtx.function));
        if(generalCtx.variant != null)
            slave.setVariant(Util.convert(generalCtx.variant));

        slave.setBitrate(new BitrateVisitor().visit(generalCtx.bitrateDefinition()));

        if(generalCtx.sendsWakeUp != null)
            slave.setSendsWakeUpSignal(true);

        DiagnosticDefinitionContext diagnosticCtx = ctx.diagnosticDefinition();

        if(diagnosticCtx.nadList != null) {
            HashSet<Integer> NADSet = new HashSet<>();
            for(IntegerContext nadCtx:diagnosticCtx.nadList.integer())
                NADSet.add(Util.convert(nadCtx));
            slave.setNADSet(NADSet);
        } else {
            slave.setNADRange(new Range<>(Util.convert(diagnosticCtx.startNAD),Util.convert(diagnosticCtx.endNAD)));
        }

        slave.setDiagnosticClass(Util.convert(diagnosticCtx.diagnosticClass));

        P2MinStMinNAsTimeoutNCrTimeoutContext timeoutCtx = diagnosticCtx.p2MinStMinNAsTimeoutNCrTimeout();
        if(timeoutCtx.p2Min != null)
            slave.setP2Min(Util.convert(timeoutCtx.p2Min));
        if(timeoutCtx.stMin != null)
            slave.setSTMin(Util.convert(timeoutCtx.stMin));
        if(timeoutCtx.nAsTimeout != null)
            slave.setNAsTimeout(Util.convert(timeoutCtx.nAsTimeout));
        if(timeoutCtx.nCrTimeout != null)
            slave.setNCrTimeout(Util.convert(timeoutCtx.nCrTimeout));

        if(diagnosticCtx.supportSids != null) {
            for(IntegerContext integerCtx:diagnosticCtx.supportSids.integer())
                slave.addSupportedSID(Util.convert(integerCtx));
        }

        if(diagnosticCtx.maxMessageLength != null)
            slave.setMaxMessageLength(Util.convert(diagnosticCtx.maxMessageLength));

        FrameVisitor frameVisitor = new FrameVisitor(ctx, slave);
        for(FrameDefinitionContext frameCtx:ctx.framesDefinition().frameDefinition())
            slave.addFrame(frameVisitor.visit(frameCtx));

        StatusManagementContext statusManagementCtx = ctx.statusManagement();
        slave.setResponseErrorSignal(slave.getSignal(statusManagementCtx.responseErrorSignal.getText())); // TODO check for null
        if(statusManagementCtx.faultStateSignals != null) {
            for(TerminalNode signalName:statusManagementCtx.faultStateSignals.Identifier())
                slave.addFaultStateSignal(slave.getSignal(signalName.getText())); // TODO check for null
        }

        if(ctx.freeTextDefinition() != null)
            slave.setFreeText(ctx.freeTextDefinition().text.getText().replace("\"",""));

        return slave;
    }
}
