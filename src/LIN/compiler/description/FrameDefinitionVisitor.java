package LIN.compiler.description;

import LIN.Frame;
import LIN.Master;

class FrameDefinitionVisitor extends DescriptionFileBaseVisitor<Frame> {
    private final Master master;
    private Frame frame;

    public FrameDefinitionVisitor(Master master) {
        this.master = master;
    }

    @Override
    public Frame visitFrameDefinition(DescriptionFileParser.FrameDefinitionContext ctx) {
        //frame = new Frame(ctx.frameName.getText());
        return null;
    }
}
