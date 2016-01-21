package LIN2.compiler;


import LIN2.Cluster;
import LIN2.Node;
import LIN2.Slave;
import LIN2.bitrate.Bitrate;
import LIN2.compiler.generation.PIC24FJxxGB00x.PIC24FJxxGB00x;
import LIN2.compiler.generation.PIC24FJxxGB00x.UART1;
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
import com.beust.jcommander.ParameterException;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.*;
import java.util.Set;

public class Compiler {
    public static void main(String [] args) throws IOException {
        CompilerOptions compilerOptions = new CompilerOptions();
        try {
            compilerOptions.parse(args);
        }
        catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.out.println();
            compilerOptions.usage();
            System.exit(-1);
        }

        if(args.length ==0 || compilerOptions.getHelp()) {
            compilerOptions.usage();
            return;
        }

        if(compilerOptions.getSlaveDriverOptions() != null) {
            CompilerOptions.SlaveDriverOptions slaveOptions = compilerOptions.getSlaveDriverOptions();
            File outputDir = new File(slaveOptions.getOutputDirectory()).getCanonicalFile();

            Cluster cluster = null;
            for(String sourceFile:slaveOptions.getSources()) {
                if(sourceFile.endsWith(".ldf") && cluster == null) {
                    cluster = parseDescriptionFile(sourceFile);
                }
                else {
                    System.out.println("error: more than one LIN Description file.");
                    System.exit(-1);
                }
            }

            if(cluster == null)
                cluster = new Cluster();

            for(String sourceFile:slaveOptions.getSources()) {
                if (sourceFile.endsWith(".ncf"))
                    parseNodeCapability(sourceFile, cluster);
            }

            Slave slave = null;

            Set<Slave> slaves = cluster.getSlaves();
            if(slaves.size() == 1) {
                slave = slaves.iterator().next();
            }
            else if(slaveOptions.getSlaveName() !=null) {
                slave = cluster.getSlave(slaveOptions.getSlaveName());
            }
            else {
                System.out.println("The following option is required: -s, --slave");
                System.out.println();
                compilerOptions.usage();
                System.out.println("       possible slave names:");
                for(Slave s:slaves) {
                    System.out.println("         "+s.getName());
                }
                System.exit(-1);
            }
            generateDriver(compilerOptions,outputDir,slave);
        }
        else if(compilerOptions.getMasterDriverOptions() != null) {
            CompilerOptions.MasterDriverOptions masterOptions = compilerOptions.getMasterDriverOptions();
            File outputDir = new File(masterOptions.getOutputDirectory()).getCanonicalFile();

            if(masterOptions.getSources().size() != 1) { // TODO add other types
                System.out.println("too many source files should only contain one .ldf");
                System.exit(-1);
            }

            Cluster cluster = parseDescriptionFile(masterOptions.getSources().get(0));

            generateDriver(compilerOptions,outputDir,cluster.getMaster());
        }
    }

    public static void generateDriver(CompilerOptions options,File outputDir,Node node) throws FileNotFoundException {
        // TODO check that output is really a directory
        if(!outputDir.exists())
            outputDir.mkdirs();

        PIC24FJxxGB00x target = new PIC24FJxxGB00x();
        UART1 inf = new UART1();

        STGroup slaveDriverHeader = new STGroupFile("LIN2/compiler/generation/template/generic/DriverHeader.stg");
        target.addHeaderGroups(slaveDriverHeader);
        addModelAdaptors(slaveDriverHeader);

        STGroup slaveDriverSource = new STGroupFile("LIN2/compiler/generation/template/generic/DriverSource.stg");
        target.addSourceGroups(slaveDriverSource);
        addModelAdaptors(slaveDriverSource);

        ST headerDriverGroup = slaveDriverHeader.getInstanceOf("driverHeader");
        headerDriverGroup.add("node", node);
        headerDriverGroup.add("target", target);
        headerDriverGroup.add("interface", inf);

        PrintWriter headerFile = new PrintWriter(new FileOutputStream(new File(outputDir,node.getName() + ".h")));
        headerFile.println(headerDriverGroup.render());
        headerFile.close();


        ST sourceDriverGroup = slaveDriverSource.getInstanceOf("driverSource");
        sourceDriverGroup.add("node",node);
        sourceDriverGroup.add("target", target);
        sourceDriverGroup.add("interface", inf);

        PrintWriter sourceFile = new PrintWriter(new FileOutputStream(new File(outputDir,node.getName()+".c")));
        sourceFile.println(sourceDriverGroup.render());
        sourceFile.close();
    }

    private static void parseNodeCapability(String sourceFile,Cluster cluster) throws IOException {
        ANTLRFileStream inputStream = new ANTLRFileStream(sourceFile);

        NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

        NodeCapabilityFileParser.NodeCapabilityFileContext context = parser.nodeCapabilityFile();

        if(parser.getNumberOfSyntaxErrors() > 0)
            System.exit(-1);

        SlaveVisitor slaveVisitor = new SlaveVisitor(cluster);
        for(NodeCapabilityFileParser.NodeDefinitionContext nodeCtx:context.nodeDefinition())
            cluster.addSlave(slaveVisitor.visit(nodeCtx));
    }

    private static Cluster parseDescriptionFile(String sourceFile) throws IOException {
        ANTLRFileStream inputStream = new ANTLRFileStream(sourceFile);

        DescriptionFileLexer lexer = new DescriptionFileLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DescriptionFileParser parser = new DescriptionFileParser(tokens);

        DescriptionFileParser.DescriptionFileContext context = parser.descriptionFile();

        if(parser.getNumberOfSyntaxErrors() > 0)
            System.exit(-1);

        return new ClusterVisitor().visit(context);
    }

    private static void addModelAdaptors(STGroup group) {
        group.registerModelAdaptor(Node.class,new NodeModelAdaptor());
        group.registerModelAdaptor(Frame.class,new FrameModelAdaptor());
        group.registerModelAdaptor(Entry.class,new PolymorphismModelAdaptor());
        group.registerModelAdaptor(Integer.class,new IntegerModelAdaptor());
        group.registerModelAdaptor(Signal.class,new SignalModelAdaptor());
        group.registerModelAdaptor(SignalValue.class,new SignalValueModelAdaptor());
        group.registerModelAdaptor(EncodedValue.class,new PolymorphismModelAdaptor());
        group.registerModelAdaptor(Bitrate.class,new PolymorphismModelAdaptor());
    }
}
