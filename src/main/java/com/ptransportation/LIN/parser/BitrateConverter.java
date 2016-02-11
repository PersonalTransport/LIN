package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.AutomaticBitrate;
import com.ptransportation.LIN.model.Bitrate;
import com.ptransportation.LIN.model.FixedBitrate;
import com.ptransportation.LIN.model.SelectBitrate;

public class BitrateConverter extends NodeCapabilityFileBaseVisitor<Bitrate> {
    @Override
    public Bitrate visitFixedBitrate(NodeCapabilityFileParser.FixedBitrateContext ctx) {
        FixedBitrate bitrate = new FixedBitrate();
        bitrate.setValue(Double.parseDouble(ctx.value.getText()));
        return bitrate;
    }

    @Override
    public Bitrate visitAutomaticBitrate(NodeCapabilityFileParser.AutomaticBitrateContext ctx) {
        AutomaticBitrate bitrate = new AutomaticBitrate();
        bitrate.setMinValue(Double.parseDouble(ctx.minValue.getText()));
        bitrate.setMaxValue(Double.parseDouble(ctx.maxValue.getText()));
        return bitrate;
    }

    @Override
    public Bitrate visitSelectBitrate(NodeCapabilityFileParser.SelectBitrateContext ctx) {
        SelectBitrate bitrate = new SelectBitrate();
        for (NodeCapabilityFileParser.NumberContext number : ctx.values)
            bitrate.getValues().add(Double.parseDouble(number.getText()));
        return bitrate;
    }
}
