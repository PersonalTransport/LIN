package com.ptransportation.LIN;


import com.beust.jcommander.ParameterException;
import com.ptransportation.LIN.generation.Target;
import com.ptransportation.LIN.generation.targets.PIC24FJxxGB00x.PIC24FJxxGB00x;
import com.ptransportation.LIN.generation.targets.generic.GenericTarget;
import com.ptransportation.LIN.model.Node;
import com.ptransportation.LIN.model.NodeCapabilityFile;
import com.ptransportation.LIN.parser.NodeCapabilityFileConverter;
import com.ptransportation.LIN.parser.NodeCapabilityFileLexer;
import com.ptransportation.LIN.parser.NodeCapabilityFileLinker;
import com.ptransportation.LIN.parser.NodeCapabilityFileParser;
import com.ptransportation.LIN.validation.DefaultValidator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Compiler {
    private static Target[] targets = {
            new GenericTarget(),
            new PIC24FJxxGB00x()
    };

    public static void main(String[] args) throws IOException {
        CommandLineOptions commandLineOptions = new CommandLineOptions();
        try {
            commandLineOptions.parse(args);
        } catch (ParameterException e) {
            commandLineOptions.usage();
            System.exit(-1);
        }

        if (args.length == 0 || commandLineOptions.isHelp()) {
            commandLineOptions.usage();
            return;
        }
        if (commandLineOptions.isVersion()) {
            try {
                final URL resource = Compiler.class.getClassLoader().getResource("config.properties");
                Properties props = new Properties();
                props.load(resource.openConnection().getInputStream());
                System.out.println(props.getProperty("version"));
            } catch (IOException e) {
                System.exit(-1);
            }
            return;
        }

        File outputDir = new File(commandLineOptions.getOutputDirectory()).getCanonicalFile();
        if (!outputDir.exists())
            outputDir.mkdirs();

        Compiler compiler = new Compiler();

        Node node = compiler.compile(commandLineOptions.getSources());
        if (node == null) {
            System.err.println("No node found to export.");
            System.exit(-1);
        }

        Target target = null;
        for (Target t : targets) {
            if (t.targetMatches(commandLineOptions.getTargetDevice()))
                target = t;
        }
        if (target == null) {
            System.err.println("Not target named '" + commandLineOptions.getTargetDevice() + "'.");
            System.exit(-1);
        }

        target.generateDriver(commandLineOptions, node, outputDir);
    }

    private ErrorModel errorModel;

    public Compiler() {
        this.errorModel = new ErrorModel();
    }

    public Node compile(List<String> sourceFiles) throws IOException {
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
            if (parser.getNumberOfSyntaxErrors() == 0) {
                NodeCapabilityFileConverter converter = new NodeCapabilityFileConverter();
                NodeCapabilityFile nodeCapabilityFile = converter.visit(context);

                nodeCapabilityFiles.add(nodeCapabilityFile);
                nodeCapabilityFileContexts.add(context);
            } else {
                hasErrors = true;
            }
        }
        if (!hasErrors) {
            int startingErrors = errorModel.getErrorCount();
            NodeCapabilityFileLinker linker = new NodeCapabilityFileLinker(errorModel);
            linker.link(nodeCapabilityFileContexts, nodeCapabilityFiles);

            if (startingErrors == errorModel.getErrorCount()) {
                startingErrors = errorModel.getErrorCount();
                DefaultValidator validator = new DefaultValidator(errorModel);
                validator.validate(nodeCapabilityFiles.get(0).getNode());

                if (startingErrors == errorModel.getErrorCount())
                    return nodeCapabilityFiles.get(0).getNode();
            }
        }
        return null;
    }
}