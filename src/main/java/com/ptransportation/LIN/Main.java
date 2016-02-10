package com.ptransportation.LIN;

import com.ptransportation.LIN.model.NodeCapabilityFile;
import com.ptransportation.LIN.model.Slave;
import com.ptransportation.LIN.parser.NodeCapabilityFileConverter;
import com.ptransportation.LIN.parser.NodeCapabilityFileLexer;
import com.ptransportation.LIN.parser.NodeCapabilityFileParser;
import com.ptransportation.LIN.validation.DefaultValidator;
import org.antlr.v4.runtime.*;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String [] args) throws IOException {
        ANTLRInputStream file = new ANTLRInputStream(new FileInputStream(args[0]));
        file.name = args[0];

        NodeCapabilityFileLexer lexer = new NodeCapabilityFileLexer(file);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        NodeCapabilityFileParser parser = new NodeCapabilityFileParser(tokens);

        //parser.removeErrorListeners();
        /*parser.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line,
                                    int charPositionInLine,
                                    String msg,
                                    RecognitionException e)
            {
                System.err.println(recognizer.getInputStream().getSourceName()+":"+line+":"+charPositionInLine+": "+msg);
            }

        });*/

        NodeCapabilityFileParser.NodeCapabilityFileContext context = parser.nodeCapabilityFile();

        NodeCapabilityFileConverter converter = new NodeCapabilityFileConverter();
        NodeCapabilityFile nodeCapabilityFile = converter.visit(context);

        DefaultValidator validator = new DefaultValidator();
        validator.validate(nodeCapabilityFile);
    }
}
