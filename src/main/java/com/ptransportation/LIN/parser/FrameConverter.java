package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.Frame;
import com.ptransportation.LIN.model.Signal;

public class FrameConverter extends NodeCapabilityFileBaseVisitor<Frame> {
    private SignalValueConverter signalValueConverter;

    public FrameConverter() {
        this.signalValueConverter = new SignalValueConverter();
    }

    @Override
    public Frame visitFrame(NodeCapabilityFileParser.FrameContext ctx) {
        Frame frame = new Frame(ctx.name.getText());

        frame.setLength(Integer.decode(ctx.length.getText()));

        if (ctx.minPeriod != null)
            frame.setMinPeriod(Integer.decode(ctx.minPeriod.getText()));

        if (ctx.maxPeriod != null)
            frame.setMaxPeriod(Integer.decode(ctx.maxPeriod.getText()));

        if (ctx.eventTriggeredFrame != null)
            frame.setEventTriggeredFrame(new Frame(ctx.eventTriggeredFrame.getText()));

        for (NodeCapabilityFileParser.SignalContext signalCtx : ctx.signals) {
            Signal signal = new Signal(signalCtx.name.getText());
            
            signal.setFrame(frame);

            signal.setSize(Integer.decode(signalCtx.size.getText()));

            signal.setInitialValue(signalValueConverter.visit(signalCtx.initialValue));

            signal.setOffset(Integer.decode(signalCtx.offset.getText()));

            frame.getSignals().add(signal);
        }

        return frame;
    }
}
