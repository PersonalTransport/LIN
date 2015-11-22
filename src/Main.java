import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String [] args) throws IOException {
        for(String fileName : args) {
            if(fileName.endsWith(".ncf")) {
                SlaveCompiler compiler = new SlaveCompiler(new FileInputStream(fileName));
            }
        }
    }
}
