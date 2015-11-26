package LIN.compiler.capability.parser;

import LIN.compiler.capability.parser.NodeCapabilityFileParser.IntegerContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.RealContext;

class Util {
    static protected int convert(IntegerContext ctx) {
        return Integer.decode(ctx.getText());
    }

    static protected float convert(RealContext ctx) {
        if(ctx.integer() != null)
            return convert(ctx.integer());
        return Float.parseFloat(ctx.Real().getText());
    }
}
