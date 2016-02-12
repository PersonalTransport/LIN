package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.*;
import org.antlr.v4.runtime.Token;

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
        Slave node = new Slave(ctx.name.getText());
        node.setProtocolVersion(Double.parseDouble(ctx.protocolVersion.getText().replaceAll("\"", "")));
        node.setSupplier(Integer.decode(ctx.supplier.getText()));
        node.setFunction(Integer.decode(ctx.function.getText()));
        node.setVariant(Integer.decode(ctx.variant.getText()));
        node.setBitrate(bitrateConverter.visit(ctx.bitrate));
        node.setSendsWakeUpSignal(ctx.sendsWakeUpSignal != null);
        node.setNadSet(nadSetConverter.visit(ctx.nadSet()));
        node.setDiagnosticClass(Integer.decode(ctx.diagnosticClass.getText()));
        if (ctx.p2Min != null)
            node.setP2Min(Double.parseDouble(ctx.p2Min.getText()));
        if (ctx.stMin != null)
            node.setStMin(Double.parseDouble(ctx.stMin.getText()));
        if (ctx.nAsTimeout != null)
            node.setNAsTimeout(Double.parseDouble(ctx.nAsTimeout.getText()));
        if (ctx.nCrTimeout != null)
            node.setNCrTimeout(Double.parseDouble(ctx.nCrTimeout.getText()));
        if (ctx.supportedSIDS != null) {
            for (NodeCapabilityFileParser.IntegerContext sid : ctx.supportedSIDS)
                node.getSupportedSIDS().add(Integer.decode(sid.getText()));
        }
        if (ctx.maxMessageLength != null)
            node.setMaxMessageLength(Integer.decode(ctx.maxMessageLength.getText()));

        for (NodeCapabilityFileParser.FrameContext frameCtx : ctx.frames) {
            Frame frame = frameConverter.visit(frameCtx);
            frame.setNode(node);
            node.getFrames().add(frame);
        }

        for (NodeCapabilityFileParser.EncodingContext encodingCtx : ctx.encodings) {
            Encoding encoding = encodingConverter.visit(encodingCtx);
            encoding.setNode(node);
            node.getEncodings().add(encoding);
        }

        if (ctx.freeText != null)
            node.setFreeText(ctx.getText().replaceAll("\"", ""));
        return node;
    }

    @Override
    public Node visitMaster(NodeCapabilityFileParser.MasterContext ctx) {
        Master node = new Master(ctx.name.getText());
        node.setProtocolVersion(Double.parseDouble(ctx.protocolVersion.getText().replaceAll("\"", "")));
        node.setSupplier(Integer.decode(ctx.supplier.getText()));
        node.setFunction(Integer.decode(ctx.function.getText()));
        node.setVariant(Integer.decode(ctx.variant.getText()));
        node.setBitrate(bitrateConverter.visit(ctx.bitrate));
        node.setTimebase(Double.parseDouble(ctx.timebase.getText()));
        node.setJitter(Double.parseDouble(ctx.jitter.getText()));

        for (NodeCapabilityFileParser.FrameContext frameCtx : ctx.frames) {
            Frame frame = frameConverter.visit(frameCtx);
            frame.setNode(node);
            node.getFrames().add(frame);
        }

        for (NodeCapabilityFileParser.EncodingContext encodingCtx : ctx.encodings) {
            Encoding encoding = encodingConverter.visit(encodingCtx);
            encoding.setNode(node);
            node.getEncodings().add(encoding);
        }

        for (NodeCapabilityFileParser.ScheduleTableContext scheduleTableCtx : ctx.scheduleTables) {
            ScheduleTable scheduleTable = scheduleTableConverter.visit(scheduleTableCtx);
            scheduleTable.setMaster(node);
            node.getScheduleTables().add(scheduleTable);
        }

        if (ctx.freeText != null)
            node.setFreeText(ctx.getText().replaceAll("\"", ""));
        return node;
    }
}
