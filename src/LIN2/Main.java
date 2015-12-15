package LIN2;

import LIN2.bitrate.Bitrate;
import LIN2.compiler.generation.models.*;
import LIN2.compiler.parser.capability.NodeCapabilityFileLexer;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser;
import LIN2.compiler.parser.capability.SlaveVisitor;
import LIN2.encoding.EncodedValue;
import LIN2.frame.Frame;
import LIN2.schedule.Entry;
import LIN2.signal.Signal;
import LIN2.signal.SignalValue;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void addModelAdaptors(STGroup group) {
        group.registerModelAdaptor(Frame.class,new FrameModelAdaptor());
        group.registerModelAdaptor(Entry.class,new PolymorphismModelAdaptor());
        group.registerModelAdaptor(Integer.class,new IntegerModelAdaptor());
        group.registerModelAdaptor(Signal.class,new SignalModelAdaptor());
        group.registerModelAdaptor(SignalValue.class,new SignalValueModelAdaptor());
        group.registerModelAdaptor(EncodedValue.class,new PolymorphismModelAdaptor());
        group.registerModelAdaptor(Bitrate.class,new PolymorphismModelAdaptor());

    }
    public static void main(String[] args) throws IOException {
        Cluster cluster = new Cluster();

        SlaveVisitor slaveVisitor = new SlaveVisitor(cluster);
        for(String file:args) {
            NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(new ANTLRInputStream(new FileInputStream(file)));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

            NodeCapabilityFileParser.NodeCapabilityFileContext context = parser.nodeCapabilityFile();

            for(NodeCapabilityFileParser.NodeDefinitionContext nodeCtx:context.nodeDefinition())
                cluster.addSlave(slaveVisitor.visit(nodeCtx));
        }

        STGroup capabilityFileGroup = new STGroupFile("LIN2/compiler/generation/template/DescriptionFile.stg");
        addModelAdaptors(capabilityFileGroup);

        ST descriptionFile = capabilityFileGroup.getInstanceOf("descriptionFile");
        descriptionFile.add("cluster",cluster);
        System.out.println(descriptionFile.render());
    }
}
