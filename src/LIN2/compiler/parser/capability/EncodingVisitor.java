package LIN2.compiler.parser.capability;

import LIN2.compiler.parser.capability.NodeCapabilityFileParser.EncodedValueContext;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.EncodingDefinitionContext;
import LIN2.encoding.Encoding;

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
