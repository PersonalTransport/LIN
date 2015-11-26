package LIN.compiler.capability.parser;

import LIN.Slave;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.SignalDefinitionContext;
import LIN.signal.Signal;

import static LIN.compiler.capability.parser.Util.convert;

public class SignalDefinitionVisitor extends NodeCapabilityFileBaseVisitor<Signal> {
    private final Slave slave;

    public SignalDefinitionVisitor(Slave slave) {
        this.slave = slave;
    }

    @Override
    public Signal visitSignalDefinition(SignalDefinitionContext ctx) {
        Signal signal = new Signal(ctx.name.getText(), convert(ctx.size), convert(ctx.offset));

        signal.setInitialValue(new SignalValueVisitor().visit(ctx.signalValue()));

        if(ctx.encoding != null)
            signal.setEncoding(slave.getEncoding(ctx.encoding.getText()));

        return signal;
    }
}
