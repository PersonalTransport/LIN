package LIN.compiler;

import LIN.Slave;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NodeCapabilityFileCompiler {
    public ArrayList<Slave> compile(InputStream file) throws IOException {
        ArrayList<Slave> slaves = new ArrayList<>();

        NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(new ANTLRInputStream(file));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

        NodeCapabilityFileParser.NodeCapabilityFileContext context = parser.nodeCapabilityFile();
        if(parser.getNumberOfSyntaxErrors() <= 0) {

            NodeDefinitionVisitor nodeDefinitionVisitor = new NodeDefinitionVisitor();
            for (NodeCapabilityFileParser.NodeDefinitionContext nodeCtx : context.nodeDefinition())
                slaves.add(nodeDefinitionVisitor.visit(nodeCtx));
        }

        return slaves;
    }

    public static void main(String [] args) throws IOException {
        NodeCapabilityFileCompiler compiler = new NodeCapabilityFileCompiler();

        for(String arg : args) {
            System.out.println(arg);
            ArrayList<Slave> slaves = compiler.compile(new FileInputStream(arg));
            for (Slave slave : slaves) {
                System.out.println(slave);
            }
        }
    }
}
