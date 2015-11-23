package LIN.compiler.description;

import LIN.Master;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DescriptionCompiler {
    public Master compile(InputStream file) throws IOException {
        DescriptionFileLexer lexer = new DescriptionFileLexer(new ANTLRInputStream(file));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DescriptionFileParser parser = new DescriptionFileParser(tokens);

        DescriptionFileParser.DescriptionFileContext context = parser.descriptionFile();
        if(parser.getNumberOfSyntaxErrors() > 0)
            return null;

        return new MasterVisitor().visit(context);
    }

    public static void main(String [] args) throws IOException {
        DescriptionCompiler compiler = new DescriptionCompiler();

        for(String arg : args) {
            System.out.println(arg);
            Master master = compiler.compile(new FileInputStream(arg));
            System.out.println(master);
        }
    }
}
