package LIN.compiler.capability;

import LIN.Slave;
import org.antlr.v4.runtime.tree.TerminalNode;

import static LIN.compiler.capability.NodeUtil.convert;

class NodeDefinitionVisitor extends NodeCapabilityFileBaseVisitor<Slave> {
        private Slave slave;

        @Override
        public Slave visitNodeDefinition(NodeCapabilityFileParser.NodeDefinitionContext ctx) {
            slave = new Slave(ctx.nodeName.getText());
            visit(ctx.generalDefinition());
            visit(ctx.diagnosticDefinition());
            visit(ctx.encodingsDefinition());
            visit(ctx.framesDefinition());
            visit(ctx.statusManagement());

            if(ctx.freeTextDefinition() != null)
                visit(ctx.freeTextDefinition());

            return slave;
        }

        @Override
        public Slave visitGeneralDefinition(NodeCapabilityFileParser.GeneralDefinitionContext ctx) {
            slave.setProtocol(ctx.version.getText().replace("\"",""));
            slave.setSupplier(convert(ctx.supplier));
            slave.setFunction(convert(ctx.function));
            slave.setVariant(convert(ctx.variant));
            slave.setSendWakeUp(ctx.sendsWakeUp.getText().replace("\"","").equalsIgnoreCase("yes"));
            slave.setBitrate(new BitrateDefinitionVisitor().visit(ctx.bitrateDefinition()));
            return slave;
        }

        @Override
        public Slave visitDiagnosticDefinition(NodeCapabilityFileParser.DiagnosticDefinitionContext ctx) {
            if(ctx.nadList != null) {
                for(NodeCapabilityFileParser.IntegerContext intCtx: ctx.nadList.integer())
                    slave.addPossibleNad(convert(intCtx));
            }
            else {
                for(int i=convert(ctx.startNAD); i<=convert(ctx.endNAD); ++i)
                    slave.addPossibleNad(i);
            }

            slave.setDiagnosticClass(convert(ctx.diagnosticClass));

            if(ctx.p2Min != null)
                slave.setP2Min(convert(ctx.p2Min));

            if(ctx.stMin != null)
                slave.setSTMin(convert(ctx.stMin));

            if(ctx.nAsTimeout != null)
                slave.setNAsTimeout(convert(ctx.nAsTimeout));

            if(ctx.nCrTimeout != null)
                slave.setNCrTimeout(convert(ctx.nCrTimeout));


            if(ctx.supportSids != null) {
                for(NodeCapabilityFileParser.IntegerContext intCtx:ctx.supportSids.integer())
                    slave.addSupportSID(convert(intCtx));
            }

            if(ctx.maxMessageLength != null)
                slave.setMaxMessageLength(convert(ctx.maxMessageLength));

            return slave;
        }

        @Override
        public Slave visitEncodingsDefinition(NodeCapabilityFileParser.EncodingsDefinitionContext ctx) {
            EncodingVisitor encodingVisitor = new EncodingVisitor();
            for(NodeCapabilityFileParser.EncodingDefinitionContext encodingCtx:ctx.encodingDefinition())
                slave.addEncoding(encodingVisitor.visit(encodingCtx));

            return slave;
        }

        @Override
        public Slave visitFramesDefinition(NodeCapabilityFileParser.FramesDefinitionContext ctx) {
            FrameDefinitionVisitor frameVisitor = new FrameDefinitionVisitor(slave);
            for(NodeCapabilityFileParser.FrameDefinitionContext frameCtx:ctx.frameDefinition())
                slave.addFrame(frameVisitor.visit(frameCtx));

            return slave;
        }

        @Override
        public Slave visitStatusManagement(NodeCapabilityFileParser.StatusManagementContext ctx) {
            slave.setResponseErrorSignal(slave.getSignal(ctx.responseErrorSignal.getText()));

            if(ctx.faultStateSignals != null) {
                for(TerminalNode signal : ctx.faultStateSignals.Identifier())
                    slave.addFaultStateSignal(slave.getSignal(signal.getText())); // TODO error if null.
            }
            return slave;
        }

        @Override
        public Slave visitFreeTextDefinition(NodeCapabilityFileParser.FreeTextDefinitionContext ctx) {
            slave.setFreeText(ctx.text.getText());
            return slave;
        }
    }