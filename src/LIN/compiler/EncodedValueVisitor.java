package LIN.compiler;

import LIN.encoding.*;

import static LIN.compiler.NodeUtil.*;

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