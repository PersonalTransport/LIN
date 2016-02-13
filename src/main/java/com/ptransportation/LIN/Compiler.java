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
import java.util.ArrayList;
import java.util.List;

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

        if (args.length == 0 || compilerOptions.getHelp()) {
            compilerOptions.usage();
            return;
        }

        Compiler compiler = new Compiler();
        compiler.compile(compilerOptions);
    }

    private void compile(CompilerOptions compilerOptions) throws IOException {
        ErrorModel errorModel = new ErrorModel();
        DefaultValidator validator = new DefaultValidator(errorModel);

        Target target = null;
        for (Target t : targets) {
            if (t.targetMatches(compilerOptions.getTargetDevice()))
                target = t;
        }
        if (target == null) {
            System.err.println("Not target named '" + compilerOptions.getTargetDevice() + "'.");
            return;
        }

        Interface inf = target.getInterface(compilerOptions.getTargetInterface());
        if (inf == null) {
            System.err.println(compilerOptions.getTargetDevice() + " doe not have a interface named '" + compilerOptions.getTargetInterface() + "'.");
            return;
        }

        if (compilerOptions.getSlaveDriverOptions() != null) {
            CompilerOptions.SlaveDriverOptions slaveOptions = compilerOptions.getSlaveDriverOptions();
            File outputDir = new File(slaveOptions.getOutputDirectory()).getCanonicalFile();
            if (!outputDir.exists())
                outputDir.mkdirs();

            List<NodeCapabilityFile> nodeCapabilityFiles = generateModel(slaveOptions.getSources(), errorModel);
            for (NodeCapabilityFile nodeCapabilityFile : nodeCapabilityFiles) {
                if (nodeCapabilityFile.getNode() instanceof Slave) {
                    validator.validate(nodeCapabilityFile);
                    if(errorModel.getErrorCount() == 0)
                        generateDriver(target, inf, outputDir, nodeCapabilityFile.getNode());
                    return; // TODO check for -s flag.
                }
            }
        } else if (compilerOptions.getMasterDriverOptions() != null) {
            CompilerOptions.MasterDriverOptions masterOptions = compilerOptions.getMasterDriverOptions();
            File outputDir = new File(masterOptions.getOutputDirectory()).getCanonicalFile();
            if (!outputDir.exists())
                outputDir.mkdirs();

            List<NodeCapabilityFile> nodeCapabilityFiles = generateModel(masterOptions.getSources(), errorModel);
            for (NodeCapabilityFile nodeCapabilityFile : nodeCapabilityFiles) {
                if (nodeCapabilityFile.getNode() instanceof Master) {
                    validator.validate(nodeCapabilityFile);
                    if(errorModel.getErrorCount() == 0)
                        generateDriver(target, inf, outputDir, nodeCapabilityFile.getNode());
                    return; // TODO check for more than one master.
                }
            }
        }
    }

    private static List<NodeCapabilityFile> generateModel(List<String> sourceFiles, ErrorModel errorModel) throws IOException {
        List<NodeCapabilityFile> nodeCapabilityFiles = new ArrayList<NodeCapabilityFile>();
        List<NodeCapabilityFileParser.NodeCapabilityFileContext> nodeCapabilityFileContexts = new ArrayList<NodeCapabilityFileParser.NodeCapabilityFileContext>();

        for (String file : sourceFiles) {
            ANTLRInputStream stream = new ANTLRInputStream(new FileInputStream(file));
            stream.name = file;

            NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(stream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

            lexer.removeErrorListeners();
            parser.removeErrorListeners();

            lexer.addErrorListener(errorModel);
            parser.addErrorListener(errorModel);

            NodeCapabilityFileParser.NodeCapabilityFileContext context = parser.nodeCapabilityFile();

            NodeCapabilityFileConverter converter = new NodeCapabilityFileConverter();
            NodeCapabilityFile nodeCapabilityFile = converter.visit(context);

            nodeCapabilityFiles.add(nodeCapabilityFile);
            nodeCapabilityFileContexts.add(context);
        }

        NodeCapabilityFileLinker linker = new NodeCapabilityFileLinker(errorModel);
        linker.link(nodeCapabilityFileContexts, nodeCapabilityFiles);
        return nodeCapabilityFiles;
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

        group.registerModelAdaptor(Master.class, new MasterModelAdaptor());
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