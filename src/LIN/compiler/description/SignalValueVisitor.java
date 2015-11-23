package LIN.compiler.description;

import LIN.signal.ArraySignalValue;
import LIN.signal.ScalarSignalValue;
import LIN.signal.SignalValue;

import java.util.ArrayList;

import static LIN.compiler.description.Util.convert;


class SignalValueVisitor extends DescriptionFileBaseVisitor<SignalValue> {
    @Override
    public SignalValue visitSignalScalarValue(DescriptionFileParser.SignalScalarValueContext ctx) {
        return new ScalarSignalValue(convert(ctx.integer()));
    }

    @Override
    public SignalValue visitSignalArrayValue(DescriptionFileParser.SignalArrayValueContext ctx) {
        ArrayList<Integer> values = new ArrayList<>();
        for(DescriptionFileParser.IntegerContext intCtx: ctx.integerList().integer())
            values.add(convert(intCtx));

        return new ArraySignalValue(values);
    }
}