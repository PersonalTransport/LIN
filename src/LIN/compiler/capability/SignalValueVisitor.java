package LIN.compiler.capability;

import LIN.signal.ArraySignalValue;
import LIN.signal.ScalarSignalValue;
import LIN.signal.SignalValue;

import java.util.ArrayList;

import static LIN.compiler.capability.NodeUtil.convert;


class SignalValueVisitor extends NodeCapabilityFileBaseVisitor<SignalValue> {
    @Override
    public SignalValue visitSignalScalarValue(NodeCapabilityFileParser.SignalScalarValueContext ctx) {
        return new ScalarSignalValue(NodeUtil.convert(ctx.integer()));
    }

    @Override
    public SignalValue visitSignalArrayValue(NodeCapabilityFileParser.SignalArrayValueContext ctx) {
        ArrayList<Integer> values = new ArrayList<>();
        for(NodeCapabilityFileParser.IntegerContext intCtx: ctx.integerList().integer())
            values.add(NodeUtil.convert(intCtx));

        return new ArraySignalValue(values);
    }
}