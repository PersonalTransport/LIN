package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.*;

public class EncodedValueConverter extends NodeCapabilityFileBaseVisitor<EncodedValue> {

    @Override
    public EncodedValue visitAsciiEncodedValue(NodeCapabilityFileParser.AsciiEncodedValueContext ctx) {
        return new ASCIIEncodedValue();
    }

    @Override
    public EncodedValue visitBcdEncodedValue(NodeCapabilityFileParser.BcdEncodedValueContext ctx) {
        return new BCDEncodedValue();
    }

    @Override
    public EncodedValue visitLogicalEncodedValue(NodeCapabilityFileParser.LogicalEncodedValueContext ctx) {
        LogicalEncodedValue encodedValue = new LogicalEncodedValue();
        encodedValue.setValue(Integer.decode(ctx.value.getText()));
        if (ctx.textInfo != null)
            encodedValue.setTextInfo(ctx.textInfo.getText().replaceAll("\"", ""));
        return encodedValue;
    }

    @Override
    public EncodedValue visitPhysicalEncodedValue(NodeCapabilityFileParser.PhysicalEncodedValueContext ctx) {
        PhysicalEncodedValue encodedValue = new PhysicalEncodedValue();
        encodedValue.setMinValue(Integer.decode(ctx.minValue.getText()));
        encodedValue.setMaxValue(Integer.decode(ctx.maxValue.getText()));
        encodedValue.setScale(Double.parseDouble(ctx.scale.getText()));
        encodedValue.setOffset(Double.parseDouble(ctx.offset.getText()));
        if (ctx.textInfo != null)
            encodedValue.setTextInfo(ctx.textInfo.getText().replaceAll("\"", ""));
        return encodedValue;
    }
}
