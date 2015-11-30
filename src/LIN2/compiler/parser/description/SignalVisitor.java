package LIN2.compiler.parser.description;

import LIN2.signal.Signal;
import LIN2.compiler.parser.description.DescriptionFileParser.*;

public class SignalVisitor extends DescriptionFileBaseVisitor<Signal> {


    @Override
    public Signal visitSignalDefinition(SignalDefinitionContext ctx) {
        Signal signal = new Signal(ctx.signalName.getText());
        signal.setSize(Util.convert(ctx.signalSize));

        if(ctx.signalValue().signalScalarValue() != null)
            signal.setInitialValue(Util.convert(ctx.signalValue().signalScalarValue()));
        else
            signal.setInitialValue(Util.convert(ctx.signalValue().signalArrayValue()));

        // TODO get the encodings??

        return signal;
    }
}
