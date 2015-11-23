package LIN.compiler.description;

import LIN.Master;
import LIN.Slave;
import LIN.signal.Signal;
import LIN.signal.SignalValue;

import java.util.HashMap;

import static LIN.compiler.description.Util.convert;

class SignalDefinitionVisitor extends DescriptionFileBaseVisitor<Signal> {
    private final Master master;
    private final HashMap<String,Signal> signals;

    public SignalDefinitionVisitor(Master master, HashMap<String, Signal> signals) {
        this.master = master;
        this.signals = signals;
    }

    @Override
    public Signal visitSignalDefinition(DescriptionFileParser.SignalDefinitionContext ctx) {
        String signalName = ctx.signalName.getText();

        if(signals.containsKey(signalName))
            throw new IllegalArgumentException("Signal \""+signalName+"\" was already defined!"); // TODO better error handling here.

        Signal signal = new Signal(signalName,convert(ctx.signalSize));

        signal.setInitialValue(new SignalValueVisitor().visit(ctx.signalValue()));

        Slave slave = master.getSlave(ctx.publishedBy.getText());
        if(slave == null)
            throw new IllegalArgumentException("Node \""+ctx.publishedBy.getText()+"\" is not defined!"); // TODO better error handling here.
        signals.put(signalName,signal);

        return signal;
    }
}
