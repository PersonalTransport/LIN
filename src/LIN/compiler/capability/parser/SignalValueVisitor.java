package LIN.compiler.capability.parser;

import LIN.compiler.capability.parser.NodeCapabilityFileParser.IntegerContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.SignalArrayValueContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.SignalScalarValueContext;
import LIN.signal.ArraySignalValue;
import LIN.signal.ScalarSignalValue;
import LIN.signal.SignalValue;

import java.util.ArrayList;

import static LIN.compiler.capability.parser.Util.convert;

public class SignalValueVisitor extends NodeCapabilityFileBaseVisitor<SignalValue> {
    @Override
    public SignalValue visitSignalScalarValue(SignalScalarValueContext ctx) {
        return new ScalarSignalValue(convert(ctx.integer()));
    }

    @Override
    public SignalValue visitSignalArrayValue(SignalArrayValueContext ctx) {
        ArrayList<Integer> values = new ArrayList<>();
        for(IntegerContext intCtx: ctx.integerList().integer())
            values.add(convert(intCtx));

        return new ArraySignalValue(values);
    }
}
