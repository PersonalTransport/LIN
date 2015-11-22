package LIN.compiler;

import LIN.signal.ArraySignalValue;
import LIN.signal.ScalarSignalValue;
import LIN.signal.SignalValue;

import static LIN.compiler.NodeUtil.*;

import java.util.ArrayList;

class SignalValueVisitor extends NodeCapabilityFileBaseVisitor<SignalValue> {
    @Override
    public SignalValue visitSignalScalarValue(NodeCapabilityFileParser.SignalScalarValueContext ctx) {
        return new ScalarSignalValue(convert(ctx.integer()));
    }

    @Override
    public SignalValue visitSignalArrayValue(NodeCapabilityFileParser.SignalArrayValueContext ctx) {
        ArrayList<Integer> values = new ArrayList<>();
        for(NodeCapabilityFileParser.IntegerContext intCtx: ctx.integerList().integer())
            values.add(convert(intCtx));

        return new ArraySignalValue(values);
    }
}