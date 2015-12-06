package LIN2.compiler.parser.capability;


import LIN2.Slave;
import LIN2.encoding.Encoding;
import LIN2.signal.Signal;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.*;

public class SignalVisitor extends NodeCapabilityFileBaseVisitor<Signal> {
    private final NodeDefinitionContext nodeCtx;
    private final Slave slave;

    public SignalVisitor(NodeDefinitionContext nodeCtx, Slave slave) {
        this.nodeCtx = nodeCtx;
        this.slave = slave;
    }

    @Override
    public Signal visitSignalDefinition(SignalDefinitionContext ctx) {
        Signal signal = new Signal(ctx.name.getText());

        signal.setSize(Util.convert(ctx.size));
        signal.setOffset(Util.convert(ctx.offset));

        if(ctx.signalValue().signalScalarValue() != null)
            signal.setInitialValue(Util.convert(ctx.signalValue().signalScalarValue()));
        else
            signal.setInitialValue(Util.convert(ctx.signalValue().signalArrayValue()));

        if(ctx.encoding != null) {
            Encoding encoding = slave.getEncoding(ctx.encoding.getText());
            if(encoding == null) {
                EncodingDefinitionContext encodingCtx = getEncoding(ctx.encoding.getText()); // TODO check for null!
                encoding = new EncodingVisitor().visit(encodingCtx);
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
