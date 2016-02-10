package com.ptransportation.LIN.parser;

import com.ptransportation.LIN.model.NadList;
import com.ptransportation.LIN.model.NadRange;
import com.ptransportation.LIN.model.NadSet;

public class NadSetConverter extends NodeCapabilityFileBaseVisitor<NadSet> {
    @Override
    public NadSet visitNadList(NodeCapabilityFileParser.NadListContext ctx) {
        NadList nadList = new NadList();
        for(NodeCapabilityFileParser.IntegerContext nad:ctx.values)
            nadList.getValues().add(Integer.decode(nad.getText()));
        return nadList;
    }

    @Override
    public NadSet visitNadRange(NodeCapabilityFileParser.NadRangeContext ctx) {
        NadRange range = new NadRange();
        range.setMinValue(Integer.decode(ctx.minValue.getText()));
        range.setMaxValue(Integer.decode(ctx.maxValue.getText()));
        return range;
    }
}
