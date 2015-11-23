package LIN.compiler.capability;

import LIN.bitrate.AutomaticBitrate;
import LIN.bitrate.Bitrate;
import LIN.bitrate.FixedBitrate;
import LIN.bitrate.SelectBitrate;

import static LIN.compiler.capability.Util.convert;

class BitrateDefinitionVisitor extends NodeCapabilityFileBaseVisitor<Bitrate> {
    @Override
    public Bitrate visitAutomaticBitrateDefinition(NodeCapabilityFileParser.AutomaticBitrateDefinitionContext ctx) {
        AutomaticBitrate bitrate = new AutomaticBitrate();

        if(ctx.max != null)
            bitrate.setMax(convert(ctx.max.value));
        if(ctx.min != null)
            bitrate.setMin(convert(ctx.min.value));

        return bitrate;
    }

    @Override
    public Bitrate visitSelectBitrateDefinition(NodeCapabilityFileParser.SelectBitrateDefinitionContext ctx) {
        SelectBitrate bitrate = new SelectBitrate();

        for(NodeCapabilityFileParser.BitrateContext bitrateCtx:ctx.bitrate())
            bitrate.add(convert(bitrateCtx.value));

        return bitrate;
    }

    @Override
    public Bitrate visitFixedBitrateDefinition(NodeCapabilityFileParser.FixedBitrateDefinitionContext ctx) {
        return new FixedBitrate(convert(ctx.bitrate().value));
    }
}