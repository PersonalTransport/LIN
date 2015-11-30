package LIN2.compiler.parser.description;

import LIN2.compiler.parser.description.DescriptionFileParser.IntegerContext;
import LIN2.compiler.parser.description.DescriptionFileParser.RealContext;
import LIN2.compiler.parser.description.DescriptionFileParser.SignalArrayValueContext;
import LIN2.compiler.parser.description.DescriptionFileParser.SignalScalarValueContext;
import LIN2.signal.ArraySignalValue;
import LIN2.signal.ScalarSignalValue;

import java.util.ArrayList;

public class Util {
    public static int convert(IntegerContext ctx) {
        return Integer.decode(ctx.getText());
    }

    public static float convert(RealContext ctx) {
        if(ctx.integer() != null)
            return convert(ctx.integer());
        return Float.parseFloat(ctx.Real().getText());
    }

    static ScalarSignalValue convert(SignalScalarValueContext ctx) {
        return new ScalarSignalValue(convert(ctx.integer()));
    }

    public static ArraySignalValue convert(SignalArrayValueContext ctx) {
        ArrayList<Integer> values = new ArrayList<>();
        for(IntegerContext intCtx:ctx.integerList().integer())
            values.add(convert(intCtx));
        return new ArraySignalValue(values);
    }
}
