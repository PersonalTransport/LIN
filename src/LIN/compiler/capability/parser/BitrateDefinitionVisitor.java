package LIN.compiler.capability.parser;

import LIN.bitrate.AutomaticBitrate;
import LIN.bitrate.Bitrate;
import LIN.bitrate.FixedBitrate;
import LIN.bitrate.SelectBitrate;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.AutomaticBitrateDefinitionContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.BitrateContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.FixedBitrateDefinitionContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.SelectBitrateDefinitionContext;

import static LIN.compiler.capability.parser.Util.convert;

public class BitrateDefinitionVisitor extends NodeCapabilityFileBaseVisitor<Bitrate> {
    @Override
    public Bitrate visitAutomaticBitrateDefinition(AutomaticBitrateDefinitionContext ctx) {
        AutomaticBitrate bitrate = new AutomaticBitrate();

        if(ctx.max != null)
            bitrate.setMax(convert(ctx.max.value));
        if(ctx.min != null)
            bitrate.setMin(convert(ctx.min.value));

        return bitrate;
    }

    @Override
    public Bitrate visitSelectBitrateDefinition(SelectBitrateDefinitionContext ctx) {
        SelectBitrate bitrate = new SelectBitrate();

        for(BitrateContext bitrateCtx:ctx.bitrate())
            bitrate.add(convert(bitrateCtx.value));

        return bitrate;
    }

    @Override
    public Bitrate visitFixedBitrateDefinition(FixedBitrateDefinitionContext ctx) {
        return new FixedBitrate(convert(ctx.bitrate().value));
    }
}
