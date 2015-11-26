package LIN.compiler;

import LIN.compiler.capability.parser.NodeCapabilityFileLexer;
import LIN.compiler.capability.parser.NodeCapabilityFileParser;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.NodeCapabilityFileContext;
import LIN.compiler.capability.parser.NodeCapabilityFileParser.NodeDefinitionContext;
import LIN.compiler.capability.parser.NodeDefinitionVisitor;
import LIN.compiler.generation.SlaveDriverGenerator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.IOException;

public class Compiler {
    public static void main(String [] args) throws IOException {
        for(String arg : args) {
            System.out.println(arg);

            NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(new ANTLRInputStream(new FileInputStream(arg)));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

            NodeCapabilityFileContext context = parser.nodeCapabilityFile();
            if(parser.getNumberOfSyntaxErrors() <= 0) {
                NodeDefinitionVisitor nodeDefinitionVisitor = new NodeDefinitionVisitor();
                for(NodeDefinitionContext nodeCtx : context.nodeDefinition())
                    new SlaveDriverGenerator().generate(nodeDefinitionVisitor.visit(nodeCtx), System.out);
            }
        }
    }
}
