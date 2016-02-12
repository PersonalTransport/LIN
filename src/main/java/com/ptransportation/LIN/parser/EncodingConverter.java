package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.Encoding;

public class EncodingConverter extends NodeCapabilityFileBaseVisitor<Encoding> {
    private EncodedValueConverter encodedValueConverter;

    public EncodingConverter() {
        this.encodedValueConverter = new EncodedValueConverter();
    }

    @Override
    public Encoding visitEncoding(NodeCapabilityFileParser.EncodingContext ctx) {
        Encoding encoding = new Encoding(ctx.name.getText());
        for (NodeCapabilityFileParser.EncodedValueContext encodedValueContext : ctx.encodedValues)
            encoding.getEncodedValues().add(encodedValueConverter.visit(encodedValueContext));
        return encoding;
    }
}
