package LIN.compiler;

import LIN.encoding.Encoding;

class EncodingVisitor extends NodeCapabilityFileBaseVisitor<Encoding> {
    private EncodedValueVisitor encodedValueVisitor = new EncodedValueVisitor();

    @Override
    public Encoding visitEncodingDefinition(NodeCapabilityFileParser.EncodingDefinitionContext ctx) {
        Encoding encoding = new Encoding(ctx.encodingName.getText());
        for(NodeCapabilityFileParser.EncodedValueContext encodedValueCtx:ctx.encodedValue())
            encoding.addEncodedValue(encodedValueVisitor.visit(encodedValueCtx));
        return encoding;
    }
}