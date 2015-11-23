package LIN.compiler.capability;

import LIN.encoding.*;

import static LIN.compiler.capability.Util.convert;

class EncodedValueVisitor extends NodeCapabilityFileBaseVisitor<EncodedValue> {
    @Override
    public EncodedValue visitLogicalEncodedValue(NodeCapabilityFileParser.LogicalEncodedValueContext ctx) {
        return new LogicalEncodedValue(convert(ctx.value),
                (ctx.textInfo != null) ? ctx.textInfo.getText().replace("\"","") : "");
    }

    @Override
    public EncodedValue visitPhysicalEncodedRange(NodeCapabilityFileParser.PhysicalEncodedRangeContext ctx) {
        return new PhysicalEncodedRange(convert(ctx.minValue),
                convert(ctx.maxValue),
                convert(ctx.scale),
                convert(ctx.offset),
                (ctx.textInfo != null) ? ctx.textInfo.getText().replace("\"","") : "");
    }

    @Override
    public EncodedValue visitBcdEncodedValue(NodeCapabilityFileParser.BcdEncodedValueContext ctx) {
        return new BCDEncodedValue();
    }

    @Override
    public EncodedValue visitAsciiEncodedValue(NodeCapabilityFileParser.AsciiEncodedValueContext ctx) {
        return new ASCIIEncodedValue();
    }
}