package LIN.compiler.capability.parser;

import LIN.compiler.capability.parser.NodeCapabilityFileParser.AsciiEncodedValueContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.BcdEncodedValueContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.LogicalEncodedValueContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.PhysicalEncodedRangeContext;
import LIN.encoding.*;

import static LIN.compiler.capability.parser.Util.convert;

public class EncodedValueVisitor extends NodeCapabilityFileBaseVisitor<EncodedValue> {
    @Override
    public EncodedValue visitLogicalEncodedValue(LogicalEncodedValueContext ctx) {
        return new LogicalEncodedValue(convert(ctx.value),
                (ctx.textInfo != null) ? ctx.textInfo.getText().replace("\"","") : "");
    }

    @Override
    public EncodedValue visitPhysicalEncodedRange(PhysicalEncodedRangeContext ctx) {
        return new PhysicalEncodedRange(convert(ctx.minValue),
                convert(ctx.maxValue),
                convert(ctx.scale),
                convert(ctx.offset),
                (ctx.textInfo != null) ? ctx.textInfo.getText().replace("\"","") : "");
    }

    @Override
    public EncodedValue visitBcdEncodedValue(BcdEncodedValueContext ctx) {
        return new BCDEncodedValue();
    }

    @Override
    public EncodedValue visitAsciiEncodedValue(AsciiEncodedValueContext ctx) {
        return new ASCIIEncodedValue();
    }
}
