package LIN.compiler.capability;

import LIN.Slave;
import LIN.signal.Signal;

class SignalDefinitionVisitor extends NodeCapabilityFileBaseVisitor<Signal> {
    private final Slave slave;

    public SignalDefinitionVisitor(Slave slave) {
        this.slave = slave;
    }

    @Override
    public Signal visitSignalDefinition(NodeCapabilityFileParser.SignalDefinitionContext ctx) {
        Signal signal = new Signal(ctx.name.getText(), Util.convert(ctx.size), Util.convert(ctx.offset));

        signal.setInitialValue(new SignalValueVisitor().visit(ctx.signalValue()));

        if(ctx.encoding != null)
            signal.setEncoding(slave.getEncoding(ctx.encoding.getText()));

        return signal;
    }
}