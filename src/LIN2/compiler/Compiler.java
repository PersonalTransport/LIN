package LIN2.compiler;


import LIN2.Cluster;
import LIN2.compiler.generation.models.*;
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
import java.io.IOException;

public class Compiler {
    private static void addModelAdaptors(STGroup group) {
        group.registerModelAdaptor(Frame.class,new FrameModelAdaptor());
        group.registerModelAdaptor(Entry.class,new FrameEntryModelAdaptor());
        group.registerModelAdaptor(Integer.class,new IntegerModelAdaptor());
        group.registerModelAdaptor(Signal.class,new SignalModelAdaptor());
        group.registerModelAdaptor(SignalValue.class,new SignalValueModelAdaptor());
        group.registerModelAdaptor(EncodedValue.class,new EncodedValueModelAdaptor());
    }

    public static void main(String [] args) throws IOException {
        for(String arg : args) {
            System.out.println(arg);

            DescriptionFileLexer lexer = new DescriptionFileLexer(new ANTLRInputStream(new FileInputStream(arg)));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            DescriptionFileParser parser = new DescriptionFileParser(tokens);

            DescriptionFileParser.DescriptionFileContext context = parser.descriptionFile();
            if(parser.getNumberOfSyntaxErrors() <= 0) {
                Cluster cluster = new ClusterVisitor().visit(context);

                STGroup capabilityFileGroup = new STGroupFile("LIN2/compiler/generation/template/CapabilityFile.stg");
                addModelAdaptors(capabilityFileGroup);

                System.out.println("//-------------------------------------------------------------------------------------------------------------//");
                ST capabilityFile = capabilityFileGroup.getInstanceOf("capabilityFile");
                capabilityFile.add("slave",cluster.getSlave("LSM"));
                System.out.println(capabilityFile.render());
                System.out.println("//-------------------------------------------------------------------------------------------------------------//");

                /*System.out.println("//-------------------------------------------------------------------------------------------------------------//");
                ST slaveHeader = group.getInstanceOf("slaveDriverHeader");
                slaveHeader.add("slave",cluster.getSlave("LSM"));
                System.out.println(slaveHeader.render());
                System.out.println("//-------------------------------------------------------------------------------------------------------------//");

                System.out.println("//-------------------------------------------------------------------------------------------------------------//");
                ST slaveSource = group.getInstanceOf("slaveDriverSource");
                slaveSource.add("slave",cluster.getSlave("LSM"));
                System.out.println(slaveSource.render());
                System.out.println("//-------------------------------------------------------------------------------------------------------------//");*/
            }
        }
    }
}
