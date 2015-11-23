package LIN.compiler.description;

class Util {
    static protected int convert(DescriptionFileParser.IntegerContext ctx) {
        return Integer.decode(ctx.getText());
    }

    static protected float convert(DescriptionFileParser.RealContext ctx) {
        if(ctx.integer() != null)
            return convert(ctx.integer());
        return Float.parseFloat(ctx.Real().getText());
    }
}
