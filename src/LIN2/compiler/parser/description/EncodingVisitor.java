package LIN2.compiler.parser.description;

import LIN2.compiler.parser.description.DescriptionFileParser.EncodedValueContext;
import LIN2.compiler.parser.description.DescriptionFileParser.EncodingDefinitionContext;
import LIN2.encoding.Encoding;

public class EncodingVisitor extends DescriptionFileBaseVisitor<Encoding> {
    private EncodedValueVisitor encodedValueVisitor = new EncodedValueVisitor();

    @Override
    public Encoding visitEncodingDefinition(EncodingDefinitionContext ctx) {

        Encoding encoding = new Encoding(ctx.encodingName.getText());
        for(EncodedValueContext encodedValueCtx:ctx.encodedValue())
            encoding.addEncodedValue(encodedValueVisitor.visit(encodedValueCtx));
        return encoding;
    }
}
