package LIN.compiler.generation;

import LIN.Slave;
import LIN.signal.ArraySignalValue;
import LIN.signal.ScalarSignalValue;
import LIN.signal.Signal;
import LIN.signal.SignalValue;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.io.*;

public class SlaveDriverGenerator {
    private Slave slave;
    private PrintStream output;

    public void generate(Slave slave, PrintStream output) throws FileNotFoundException {
        this.slave = slave;
        this.output = output;

        STGroup group = new STGroupFile("LIN/compiler/generation/template/SlaveHeader.stg");
        group.importTemplates(new STGroupFile("LIN/compiler/generation/template/SlaveSource.stg"));
        group.registerModelAdaptor(Signal.class,new ObjectModelAdaptor() {
            @Override
            public Object getProperty(Interpreter interpreter, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
                Signal signal = (Signal)o;
                if(propertyName.equals("mask")) {
                    String s = "";
                    for(int i=0;i<signal.getSize();++i)
                        s+= "1";
                    return "0x"+Long.toHexString(Long.parseLong(s,2))+"ULL";
                }
                else if(propertyName.equals("type")) {
                    if(signal.getInitialValue() instanceof ArraySignalValue)
                        return "bytes";
                    else if(signal.getSize() == 1 )
                        return "bool";
                    else if(signal.getSize() <= 8)
                        return "u8";
                    else if(signal.getSize() <= 16)
                        return "u16";
                }
                else if(propertyName.equals("value")) {
                    SignalValue value = signal.getInitialValue();
                    if(value instanceof ScalarSignalValue)
                        return ((ScalarSignalValue) value).getValue();
                    else
                        return ((ArraySignalValue)value).getValues();
                }
                return super.getProperty(interpreter,self,o,property,propertyName);
            }
        });

        PrintWriter header = new PrintWriter(new FileOutputStream(new File("C:\\Users\\aaron\\Desktop\\android-master\\include\\"+slave.getName()+".h")));
        ST slaveHeader = group.getInstanceOf("slaveHeader");
        slaveHeader.add("interface","UART2");
        slaveHeader.add("slave", slave);
        output.println("//========================"+slave.getName()+".h========================/");
        output.println(slaveHeader.render());
        output.println();
        header.println(slaveHeader.render());
        header.flush();
        header.close();

        PrintWriter source = new PrintWriter(new FileOutputStream("C:\\Users\\aaron\\Desktop\\android-master\\src\\"+slave.getName()+".c"));
        ST slaveSource = group.getInstanceOf("slaveSource");
        slaveSource.add("interface","UART2");
        slaveSource.add("slave", slave);
        output.println("//========================"+slave.getName()+".c========================/");
        output.println(slaveSource.render());

        source.println(slaveSource.render());
        source.flush();
        source.close();
    }
}
