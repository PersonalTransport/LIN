package com.ptransportation.LIN;

import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ErrorModel extends ConsoleErrorListener {
    private int errorCount;

    public ErrorModel() {
        this.errorCount = 0;
    }

    public void error(String message, Object object, String field) {
        System.err.println(message);
        this.errorCount++;
    }

    public void error(String message, Object object, String field, int index) {
        System.err.println(message);
        this.errorCount++;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        System.err.println("line " + line + ":" + charPositionInLine + " " + msg);
        this.errorCount++;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public void clearAllErrors() {
        this.errorCount = 0;
    }
}
