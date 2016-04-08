package com.ptransportation.LIN;


import com.beust.jcommander.ParameterException;
import com.ptransportation.LIN.generation.Interface;
import com.ptransportation.LIN.generation.Target;
import com.ptransportation.LIN.generation.adaptors.*;
import com.ptransportation.LIN.generation.targets.PIC24FJxxGB00x.PIC24FJxxGB00x;
import com.ptransportation.LIN.generation.targets.generic.GenericTarget;
import com.ptransportation.LIN.model.*;
import com.ptransportation.LIN.parser.NodeCapabilityFileConverter;
import com.ptransportation.LIN.parser.NodeCapabilityFileLexer;
import com.ptransportation.LIN.parser.NodeCapabilityFileLinker;
import com.ptransportation.LIN.parser.NodeCapabilityFileParser;
import com.ptransportation.LIN.validation.DefaultValidator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Compiler {
    private Target[] targets = {
            new GenericTarget(),
            new PIC24FJxxGB00x()
    };

    public static void main(String[] args) throws IOException {
        CompilerOptions compilerOptions = new CompilerOptions();
        try {
            compilerOptions.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.out.println();
            compilerOptions.usage();
            System.exit(-1);
        }

        if (args.length == 0 || compilerOptions.isHelp()) {
            compilerOptions.usage();
            return;
        }
        if(compilerOptions.isVersion()) {
            try {
                final URL resource = Compiler.class.getClassLoader().getResource("config.properties");
                Properties props = new Properties();
                props.load(resource.openConnection().getInputStream());
                System.out.println(props.getProperty("version"));
            }
            catch (IOException e) {
                System.exit(-1);
            }
            return;
        }

        Compiler compiler = new Compiler();
        if(!compiler.compile(compilerOptions)) {
            System.exit(-1);
        }
    }

    private boolean compile(CompilerOptions compilerOptions) throws IOException {
        ErrorModel errorModel = new ErrorModel();

        Target target = null;
        for (Target t : targets) {
            if (t.targetMatches(compilerOptions.getTargetDevice()))
                target = t;
        }
        if (target == null) {
            System.err.println("Not target named '" + compilerOptions.getTargetDevice() + "'.");
            return false;
        }

        Interface inf = target.getInterface(compilerOptions.getTargetInterface());
        if (inf == null) {
            System.err.println(compilerOptions.getTargetDevice() + " doe not have a interface named '" + compilerOptions.getTargetInterface() + "'.");
            return false;
        }

        File outputDir = new File(compilerOptions.getOutputDirectory()).getCanonicalFile();
        if (!outputDir.exists())
            outputDir.mkdirs();


        Node node = generateModel(compilerOptions.getSources(), errorModel);
        if(node != null) {
            generateDriver(target, inf, outputDir, node);
            return true;
        }

        return false;
    }

    private static Node generateModel(List<String> sourceFiles, ErrorModel errorModel) throws IOException {
        List<NodeCapabilityFile> nodeCapabilityFiles = new ArrayList<NodeCapabilityFile>();
        List<NodeCapabilityFileParser.NodeCapabilityFileContext> nodeCapabilityFileContexts = new ArrayList<NodeCapabilityFileParser.NodeCapabilityFileContext>();

        boolean hasErrors = false;
        for (String filepath : sourceFiles) {
            ANTLRInputStream stream = new ANTLRInputStream(new FileInputStream(filepath));
            stream.name = new File(filepath).getCanonicalPath();

            NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(stream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

            lexer.removeErrorListeners();
            parser.removeErrorListeners();

            lexer.addErrorListener(errorModel);
            parser.addErrorListener(errorModel);

            NodeCapabilityFileParser.NodeCapabilityFileContext context = parser.nodeCapabilityFile();
            if(parser.getNumberOfSyntaxErrors() == 0) {
                NodeCapabilityFileConverter converter = new NodeCapabilityFileConverter();
                NodeCapabilityFile nodeCapabilityFile = converter.visit(context);

                nodeCapabilityFiles.add(nodeCapabilityFile);
                nodeCapabilityFileContexts.add(context);
            }
            else {
                hasErrors = true;
            }
        }
        if(!hasErrors) {
            int startingErrors = errorModel.getErrorCount();
            NodeCapabilityFileLinker linker = new NodeCapabilityFileLinker(errorModel);
            linker.link(nodeCapabilityFileContexts, nodeCapabilityFiles);

            if(startingErrors == errorModel.getErrorCount()) {
                startingErrors = errorModel.getErrorCount();
                DefaultValidator validator = new DefaultValidator(errorModel);
                validator.validate(nodeCapabilityFiles.get(0).getNode());

                if(startingErrors == errorModel.getErrorCount())
                    return nodeCapabilityFiles.get(0).getNode();
            }
        }
        return null;
    }

    public void generateDriver(Target target, Interface inf, File outputDir, Node node) throws FileNotFoundException {
        STGroup slaveDriverHeader = new STGroupFile("com/ptransportation/LIN/generation/targets/DriverHeader.stg");
        target.addHeaderGroups(slaveDriverHeader);
        addModelAdaptors(slaveDriverHeader);

        ST headerDriverGroup = slaveDriverHeader.getInstanceOf("driverHeader");
        headerDriverGroup.add("node", node);
        headerDriverGroup.add("target", target);
        headerDriverGroup.add("interface", inf);

        PrintWriter headerFile = new PrintWriter(new FileOutputStream(new File(outputDir, node.getName() + ".h")));
        headerFile.println(headerDriverGroup.render());
        headerFile.close();


        STGroup slaveDriverSource = new STGroupFile("com/ptransportation/LIN/generation/targets/DriverSource.stg");
        target.addSourceGroups(slaveDriverSource);
        addModelAdaptors(slaveDriverSource);

        ST sourceDriverGroup = slaveDriverSource.getInstanceOf("driverSource");
        sourceDriverGroup.add("node", node);
        sourceDriverGroup.add("target", target);
        sourceDriverGroup.add("interface", inf);

        PrintWriter sourceFile = new PrintWriter(new FileOutputStream(new File(outputDir, node.getName() + ".c")));
        sourceFile.println(sourceDriverGroup.render());
        sourceFile.close();
    }

    private void addModelAdaptors(STGroup group) {
        group.registerModelAdaptor(Integer.class, new IntegerModelAdaptor());

        group.registerModelAdaptor(Node.class, new NodeModelAdaptor());
        group.registerModelAdaptor(Slave.class, new SlaveModelAdaptor());
        group.registerModelAdaptor(EncodedValue.class, new EncodedValueModelAdaptor());
        group.registerModelAdaptor(Bitrate.class, new BitrateModelAdaptor());
        group.registerModelAdaptor(Frame.class, new FrameModelAdaptor());
        group.registerModelAdaptor(Signal.class, new SignalModelAdaptor());
        group.registerModelAdaptor(SignalValue.class, new SignalValueModelAdaptor());
        group.registerModelAdaptor(ScheduleTable.class, new ScheduleTableModelAdaptor());
        group.registerModelAdaptor(ScheduleTableEntry.class, new ScheduleEntryModelAdaptor());
    }
}