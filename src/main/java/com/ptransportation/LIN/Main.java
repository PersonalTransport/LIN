package com.ptransportation.LIN;

import com.ptransportation.LIN.model.NodeCapabilityFile;
import com.ptransportation.LIN.parser.NodeCapabilityFileConverter;
import com.ptransportation.LIN.parser.NodeCapabilityFileLexer;
import com.ptransportation.LIN.parser.NodeCapabilityFileLinker;
import com.ptransportation.LIN.parser.NodeCapabilityFileParser;
import com.ptransportation.LIN.validation.DefaultValidator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        ErrorModel errorModel = new ErrorModel();

        List<NodeCapabilityFile> nodeCapabilityFiles = generateModel(args, errorModel);

        DefaultValidator validator = new DefaultValidator(errorModel);
        validator.validate(nodeCapabilityFiles);
    }

    private static List<NodeCapabilityFile> generateModel(String[] sourceFiles, ErrorModel errorModel) throws IOException {
        List<NodeCapabilityFile> nodeCapabilityFiles = new ArrayList<NodeCapabilityFile>();
        List<NodeCapabilityFileParser.NodeCapabilityFileContext> nodeCapabilityFileContexts = new ArrayList<NodeCapabilityFileParser.NodeCapabilityFileContext>();

        for (String file : sourceFiles) {
            ANTLRInputStream stream = new ANTLRInputStream(new FileInputStream(file));
            stream.name = file;

            NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(stream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

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


}
