package LIN2.compiler;


import LIN2.Cluster;
import LIN2.Node;
import LIN2.Slave;
import LIN2.bitrate.Bitrate;
import LIN2.compiler.generation.models.*;
import LIN2.compiler.parser.capability.NodeCapabilityFileLexer;
import LIN2.compiler.parser.capability.NodeCapabilityFileParser;
import LIN2.compiler.parser.capability.SlaveVisitor;
import LIN2.compiler.parser.description.ClusterVisitor;
import LIN2.compiler.parser.description.DescriptionFileLexer;
import LIN2.compiler.parser.description.DescriptionFileParser;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Compiler {
    public static void addModelAdaptors(STGroup group) {
        group.registerModelAdaptor(Frame.class,new FrameModelAdaptor());
        group.registerModelAdaptor(Entry.class,new FrameEntryModelAdaptor());
        group.registerModelAdaptor(Integer.class,new IntegerModelAdaptor());
        group.registerModelAdaptor(Signal.class,new SignalModelAdaptor());
        group.registerModelAdaptor(SignalValue.class,new SignalValueModelAdaptor());
        group.registerModelAdaptor(EncodedValue.class,new EncodedValueModelAdaptor());
        group.registerModelAdaptor(Bitrate.class,new BitrateModelAdaptor());

    }

    public static void main(String [] args) throws IOException {
        for(String arg : args) {
            System.out.println(arg);
            if(arg.endsWith(".ncf")) {
                Cluster cluster = new Cluster();
                NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(new ANTLRInputStream(new FileInputStream(arg)));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

                NodeCapabilityFileParser.NodeCapabilityFileContext context = parser.nodeCapabilityFile();

                SlaveVisitor slaveVisitor = new SlaveVisitor(cluster);
                for(NodeCapabilityFileParser.NodeDefinitionContext nodeCtx:context.nodeDefinition()) {
                    Slave slave = slaveVisitor.visit(nodeCtx);

                    STGroup capabilityFileGroup = new STGroupFile("LIN2/compiler/generation/template/CapabilityFile.stg");
                    addModelAdaptors(capabilityFileGroup);

                    System.out.println("//-------------------------------------------------------------------------------------------------------------//");
                    ST capabilityFile = capabilityFileGroup.getInstanceOf("capabilityFile");
                    capabilityFile.add("slave", slave);
                    System.out.println(capabilityFile.render());
                    System.out.println("//-------------------------------------------------------------------------------------------------------------//");
                }
            }
            else if(arg.endsWith(".ldf")) {

                DescriptionFileLexer lexer = new DescriptionFileLexer(new ANTLRInputStream(new FileInputStream(arg)));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                DescriptionFileParser parser = new DescriptionFileParser(tokens);

                DescriptionFileParser.DescriptionFileContext context = parser.descriptionFile();
                if (parser.getNumberOfSyntaxErrors() <= 0) {
                    Cluster cluster = new ClusterVisitor().visit(context);

                    STGroup capabilityFileGroup = new STGroupFile("LIN2/compiler/generation/template/CapabilityFile.stg");
                    addModelAdaptors(capabilityFileGroup);

                    for(Node slave:cluster.getNodes()) {
                        ST capabilityFile = capabilityFileGroup.getInstanceOf("capabilityFile");
                        capabilityFile.add("slave", slave);

                        PrintWriter output = new PrintWriter(new FileOutputStream("examples/"+slave.getName()+".ncf"));
                        output.print(capabilityFile.render());
                        output.close();
                    }
                }
            }
        }
    }
}
