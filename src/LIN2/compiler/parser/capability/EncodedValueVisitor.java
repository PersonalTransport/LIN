package LIN2.compiler.parser.capability;

import LIN2.compiler.parser.capability.NodeCapabilityFileParser.*;
import LIN2.encoding.*;

public class EncodedValueVisitor extends NodeCapabilityFileBaseVisitor<EncodedValue> {

    @Override
    public EncodedValue visitEncodedValue(EncodedValueContext ctx) {
        if(ctx.asciiEncodedValue() != null)
            return this.visit(ctx.asciiEncodedValue());
        else if(ctx.bcdEncodedValue() != null)
            return this.visit(ctx.bcdEncodedValue());
        else if(ctx.logicalEncodedValue() != null)
            return this.visit(ctx.logicalEncodedValue());
        else if(ctx.physicalEncodedRange() != null)
            return this.visit(ctx.physicalEncodedRange());
        return null;
    }

    @Override
    public EncodedValue visitLogicalEncodedValue(LogicalEncodedValueContext ctx) {
        return new LogicalEncodedValue(Util.convert(ctx.value),
                                      (ctx.textInfo != null) ? ctx.textInfo.getText().replace("\"","") : "");
    }

    @Override
    public EncodedValue visitPhysicalEncodedRange(PhysicalEncodedRangeContext ctx) {
        return new PhysicalEncodedRange(Util.convert(ctx.minValue),
                                        Util.convert(ctx.maxValue),
                                        Util.convert(ctx.scale),
                                        Util.convert(ctx.offset),
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
