package LIN2.compiler.parser.capability;

import LIN2.bitrate.AutomaticBitrate;
import LIN2.bitrate.Bitrate;
import LIN2.bitrate.FixedBitrate;
import LIN2.bitrate.SelectBitrate;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.AutomaticBitrateDefinitionContext;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.BitrateContext;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.FixedBitrateDefinitionContext;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser.SelectBitrateDefinitionContext;

public class BitrateVisitor extends NodeCapabilityFileBaseVisitor<Bitrate> {
    @Override
    public Bitrate visitAutomaticBitrateDefinition(AutomaticBitrateDefinitionContext ctx) {
        AutomaticBitrate bitrate = new AutomaticBitrate();

        if(ctx.max != null)
            bitrate.setMax(Util.convert(ctx.max.value));
        if(ctx.min != null)
            bitrate.setMin(Util.convert(ctx.min.value));

        return bitrate;
    }

    @Override
    public Bitrate visitSelectBitrateDefinition(SelectBitrateDefinitionContext ctx) {
        SelectBitrate bitrate = new SelectBitrate();

        for(BitrateContext bitrateCtx:ctx.bitrate())
            bitrate.add(Util.convert(bitrateCtx.value));

        return bitrate;
    }

    @Override
    public Bitrate visitFixedBitrateDefinition(FixedBitrateDefinitionContext ctx) {
        return new FixedBitrate(Util.convert(ctx.bitrate().value));
    }
}
