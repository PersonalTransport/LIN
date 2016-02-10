package com.ptransportation.LIN.parser;


import com.ptransportation.LIN.model.NodeCapabilityFile;

public class NodeCapabilityFileConverter extends NodeCapabilityFileBaseVisitor<NodeCapabilityFile> {

    private NodeConverter nodeConverter;

    public NodeCapabilityFileConverter() {
        this.nodeConverter = new NodeConverter();
    }

    @Override
    public NodeCapabilityFile visitNodeCapabilityFile(NodeCapabilityFileParser.NodeCapabilityFileContext ctx) {
        NodeCapabilityFile file = new NodeCapabilityFile();
        file.setLanguageVersion(Double.parseDouble(ctx.languageVersion.getText().replaceAll("\"","")));
        file.setNode(nodeConverter.visit(ctx.node()));
        return file;
    }
}
