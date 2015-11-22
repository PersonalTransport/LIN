package LIN.compiler.capability;

class NodeUtil {
    static protected int convert(NodeCapabilityFileParser.IntegerContext ctx) {
        return Integer.decode(ctx.getText());
    }

    static protected float convert(NodeCapabilityFileParser.RealContext ctx) {
        if(ctx.integer() != null)
            return convert(ctx.integer());
        return Float.parseFloat(ctx.Real().getText());
    }
}
