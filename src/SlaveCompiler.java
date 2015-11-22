import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;

public class SlaveCompiler {
    private Slave slave;

    public SlaveCompiler(InputStream nodeCapabilityFile) throws IOException {

        NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(new ANTLRInputStream(nodeCapabilityFile));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);
        NodeCapabilityFileParser.Node_capability_fileContext context = parser.node_capability_file();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new SlaveCompilerListener(), context);
    }

    private class SlaveCompilerListener extends NodeCapabilityFileBaseListener {
        @Override
        public void enterNode_definition(NodeCapabilityFileParser.Node_definitionContext ctx) {
            slave = new Slave(ctx.node_name().getText());
        }

        @Override
        public void enterGeneral_definition(NodeCapabilityFileParser.General_definitionContext ctx) {
            slave.setProtocol(ctx.VersionString().getText().replace("\"",""));
            slave.setSupplier(Integer.decode(ctx.supplier_id().getText()));
            slave.setFunction(Integer.decode(ctx.function_id().getText()));
            slave.setVariant(Integer.decode(ctx.variant_id().getText()));
            slave.setSendsWakeUp(ctx.YesOrNo().getText().replace("\"","").equalsIgnoreCase("yes"));
        }

        @Override
        public void enterBitrate_definition(NodeCapabilityFileParser.Bitrate_definitionContext ctx) {
            if(ctx.automatic_bitrate() != null) {
                AutomaticBitrate bitrate = new AutomaticBitrate();

                if(ctx.automatic_bitrate().min_bitrate() != null)
                    bitrate.setMinimum(Float.parseFloat(ctx.automatic_bitrate().min_bitrate().bitrate().real_or_integer().getText()));

                if(ctx.automatic_bitrate().max_bitrate() != null)
                    bitrate.setMaximum(Float.parseFloat(ctx.automatic_bitrate().max_bitrate().bitrate().real_or_integer().getText()));

                slave.setBitrate(bitrate);
            }
            else if(ctx.select_bitrate() != null) {
                SelectBitrate bitrate = new SelectBitrate();

                for(NodeCapabilityFileParser.BitrateContext bitrateCtx: ctx.select_bitrate().bitrate()) {
                    bitrate.add(Float.parseFloat(bitrateCtx.real_or_integer().getText()));
                }

                slave.setBitrate(bitrate);
            }
            else {
                slave.setBitrate(new FixedBitrate(Float.parseFloat(ctx.bitrate().real_or_integer().getText())));
            }
        }

        @Override
        public void enterDiagnostic_definition(NodeCapabilityFileParser.Diagnostic_definitionContext ctx) {
            if(ctx.nad_list() != null) {
                for(NodeCapabilityFileParser.IntegerContext intCtx :ctx.nad_list().integer())
                    slave.addPossibleNAD(Integer.decode(intCtx.getText()));
            }
            else {
                int start = Integer.decode(ctx.nad_range().integer(0).getText());
                int end = Integer.decode(ctx.nad_range().integer(1).getText());
                for(int i=start;i<=end;i++)
                    slave.addPossibleNAD(i);
            }

            slave.setDiagnosticClass(Integer.decode(ctx.diagnostic_class().integer().getText()));
        }
    }
}
