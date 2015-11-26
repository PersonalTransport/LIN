package LIN.compiler.capability.parser;

import LIN.compiler.capability.parser.NodeCapabilityFileParser.EncodedValueContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.EncodingDefinitionContext;
import LIN.encoding.Encoding;

public class EncodingVisitor extends NodeCapabilityFileBaseVisitor<Encoding> {
    private EncodedValueVisitor encodedValueVisitor = new EncodedValueVisitor();

    @Override
    public Encoding visitEncodingDefinition(EncodingDefinitionContext ctx) {
        Encoding encoding = new Encoding(ctx.encodingName.getText());
        for(EncodedValueContext encodedValueCtx:ctx.encodedValue())
            encoding.addEncodedValue(encodedValueVisitor.visit(encodedValueCtx));
        return encoding;
    }
}
