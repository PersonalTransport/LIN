package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.*;


public class NodeConverter extends NodeCapabilityFileBaseVisitor<Node> {

    private BitrateConverter bitrateConverter;
    private NadSetConverter nadSetConverter;
    private FrameConverter frameConverter;
    private EncodingConverter encodingConverter;
    private ScheduleTableConverter scheduleTableConverter;

    public NodeConverter() {
        this.bitrateConverter = new BitrateConverter();
        this.nadSetConverter = new NadSetConverter();
        this.frameConverter = new FrameConverter();
        this.encodingConverter = new EncodingConverter();
        this.scheduleTableConverter = new ScheduleTableConverter();
    }

    @Override
    public Node visitSlave(NodeCapabilityFileParser.SlaveContext ctx) {
        Slave node = new Slave();
        node.setName(ctx.name.getText());
        node.setProtocolVersion(Double.parseDouble(ctx.protocolVersion.getText().replaceAll("\"","")));
        node.setSupplier(Integer.decode(ctx.supplier.getText()));
        node.setFunction(Integer.decode(ctx.function.getText()));
        node.setVariant(Integer.decode(ctx.variant.getText()));
        node.setBitrate(bitrateConverter.visit(ctx.bitrate));
        node.setSendsWakeUpSignal(ctx.sendsWakeUpSignal != null);
        node.setNadSet(nadSetConverter.visit(ctx.nadSet()));
        node.setDiagnosticClass(Integer.decode(ctx.diagnosticClass.getText()));
        if(ctx.p2Min != null)
            node.setP2Min(Double.parseDouble(ctx.p2Min.getText()));
        if(ctx.stMin != null)
            node.setStMin(Double.parseDouble(ctx.stMin.getText()));
        if(ctx.nAsTimeout != null)
            node.setNAsTimeout(Double.parseDouble(ctx.nAsTimeout.getText()));
        if(ctx.nCrTimeout != null)
            node.setNCrTimeout(Double.parseDouble(ctx.nCrTimeout.getText()));
        if(ctx.supportedSIDS != null) {
            for(NodeCapabilityFileParser.IntegerContext sid:ctx.supportedSIDS)
                node.getSupportedSIDS().add(Integer.decode(sid.getText()));
        }
        if(ctx.maxMessageLength != null)
            node.setMaxMessageLength(Integer.decode(ctx.maxMessageLength.getText()));

        for(NodeCapabilityFileParser.FrameContext frame:ctx.frames)
            node.getFrames().add(frameConverter.visit(frame));

        for(NodeCapabilityFileParser.EncodingContext encoding:ctx.encodings)
            node.getEncodings().add(encodingConverter.visit(encoding));

        // TODO response_error

        // TODO fault_state_signals

        if(ctx.freeText != null)
            node.setFreeText(ctx.getText().replaceAll("\"",""));
        return node;
    }

    @Override
    public Node visitMaster(NodeCapabilityFileParser.MasterContext ctx) {
        Master node = new Master();
        node.setName(ctx.name.getText());
        node.setProtocolVersion(Double.parseDouble(ctx.protocolVersion.getText().replaceAll("\"","")));
        node.setSupplier(Integer.decode(ctx.supplier.getText()));
        node.setFunction(Integer.decode(ctx.function.getText()));
        node.setVariant(Integer.decode(ctx.variant.getText()));
        node.setBitrate(bitrateConverter.visit(ctx.bitrate));
        node.setTimebase(Double.parseDouble(ctx.timebase.getText()));
        node.setJitter(Double.parseDouble(ctx.jitter.getText()));

        // TODO slaves

        for(NodeCapabilityFileParser.FrameContext frame:ctx.frames)
            node.getFrames().add(frameConverter.visit(frame));

        for(NodeCapabilityFileParser.EncodingContext encoding:ctx.encodings)
            node.getEncodings().add(encodingConverter.visit(encoding));

        for(NodeCapabilityFileParser.ScheduleTableContext scheduleTable:ctx.scheduleTables)
            node.getScheduleTables().add(scheduleTableConverter.visit(scheduleTable));

        if(ctx.freeText != null)
            node.setFreeText(ctx.getText().replaceAll("\"",""));
        return node;
    }
}
