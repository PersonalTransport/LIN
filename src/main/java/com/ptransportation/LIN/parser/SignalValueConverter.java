package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.ArraySignalValue;
import com.ptransportation.LIN.model.ScalarSignalValue;
import com.ptransportation.LIN.model.SignalValue;

public class SignalValueConverter extends NodeCapabilityFileBaseVisitor<SignalValue> {
    @Override
    public SignalValue visitArraySignalValue(NodeCapabilityFileParser.ArraySignalValueContext ctx) {
        ArraySignalValue signalValue = new ArraySignalValue();
        for (NodeCapabilityFileParser.IntegerContext value : ctx.values)
            signalValue.getValues().add(Integer.decode(value.getText()));
        return signalValue;
    }

    @Override
    public SignalValue visitScalarSignalValue(NodeCapabilityFileParser.ScalarSignalValueContext ctx) {
        ScalarSignalValue signalValue = new ScalarSignalValue();
        signalValue.setValue(Integer.decode(ctx.value.getText()));
        return signalValue;
    }
}
