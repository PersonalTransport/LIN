package LIN2.compiler.parser.capability;


import LIN2.Cluster;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.EncodingDefinitionContext;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.NodeDefinitionContext;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.SignalDefinitionContext;
import LIN2.encoding.Encoding;
import LIN2.frame.UnconditionalFrame;
import LIN2.signal.Signal;

public class SignalVisitor extends NodeCapabilityFileBaseVisitor<Signal> {
    private final NodeDefinitionContext nodeCtx;
    private final Cluster cluster;
    private final UnconditionalFrame frame;

    public SignalVisitor(NodeDefinitionContext nodeCtx, Cluster cluster, UnconditionalFrame frame) {
        this.nodeCtx = nodeCtx;
        this.cluster = cluster;
        this.frame = frame;
    }

    @Override
    public Signal visitSignalDefinition(SignalDefinitionContext ctx) {
        Signal signal = frame.getSignal(ctx.name.getText());
        if(signal == null)
            signal = new Signal(ctx.name.getText());

        signal.setSize(Util.convert(ctx.size));
        signal.setOffset(Util.convert(ctx.offset));

        if(ctx.signalValue().signalScalarValue() != null)
            signal.setInitialValue(Util.convert(ctx.signalValue().signalScalarValue()));
        else
            signal.setInitialValue(Util.convert(ctx.signalValue().signalArrayValue()));

        if(ctx.encoding != null) {
            Encoding encoding = cluster.getEncoding(ctx.encoding.getText());
            if(encoding == null) {
                EncodingDefinitionContext encodingCtx = getEncoding(ctx.encoding.getText()); // TODO check for null!
                encoding = new EncodingVisitor().visit(encodingCtx);
            }
            else {
                // TODO check that they are the same!
            }
            signal.setEncoding(encoding);
        }

        return signal;
    }

    private EncodingDefinitionContext getEncoding(String name) {
        for(EncodingDefinitionContext encodingCtx:nodeCtx.encodingsDefinition().encodingDefinition()) {
            if(encodingCtx.encodingName.getText().equals(name))
                return encodingCtx;
        }
        return null;
    }
}
