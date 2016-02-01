package com.ptransportation.compiler;


import com.beust.jcommander.ParameterException;
import com.ptransportation.LIN.Node;
import com.ptransportation.compiler.generation.Interface;
import com.ptransportation.compiler.generation.Target;
import com.ptransportation.compiler.generation.targets.PIC24FJxxGB00x.PIC24FJxxGB00x;
import com.ptransportation.compiler.generation.targets.generic.GenericTarget;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.*;

public class Compiler {
    public static Target[] targets = {
            new GenericTarget(),
            new PIC24FJxxGB00x()
    };

    public static void main(String [] args) throws IOException {
        CompilerOptions compilerOptions = new CompilerOptions();
        try {
            compilerOptions.parse(args);
        }
        catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.out.println();
            compilerOptions.usage();
            System.exit(-1);
        }

        if(args.length ==0 || compilerOptions.getHelp()) {
            compilerOptions.usage();
            return;
        }

        if(compilerOptions.getSlaveDriverOptions() != null) {
            CompilerOptions.SlaveDriverOptions slaveOptions = compilerOptions.getSlaveDriverOptions();
            File outputDir = new File(slaveOptions.getOutputDirectory()).getCanonicalFile();

            /*Cluster cluster = new Cluster();

            for(String sourceFile:slaveOptions.getSources()) {
                if (sourceFile.toLowerCase().endsWith(".nfc")) {
                    parseNodeCapability(sourceFile, cluster);
                    break;
                }
            }

            Slave slave = cluster.getSlaves().iterator().next();

            generateDriver(compilerOptions,outputDir,slave);*/
        }
        else if(compilerOptions.getMasterDriverOptions() != null) {
            /*CompilerOptions.MasterDriverOptions masterOptions = compilerOptions.getMasterDriverOptions();
            File outputDir = new File(masterOptions.getOutputDirectory()).getCanonicalFile();

            Cluster cluster = parseDescriptionFile(masterOptions.getSources().get(0));

            generateDriver(compilerOptions,outputDir,cluster.getMaster());*/
        }
    }

    public static void generateDriver(CompilerOptions options,File outputDir,Node node) throws FileNotFoundException {
        if(!outputDir.exists())
            outputDir.mkdirs();

        Target target = null;
        for(Target t:targets) {
            if(t.targetMatches(options.getTargetDevice()))
                target = t;
        }
        if(target == null) {
            System.err.println("Not target named '"+options.getTargetDevice()+"'.");
            return;
        }

        Interface inf = target.getInterface(options.getTargetInterface());
        if(inf == null) {
            System.err.println(options.getTargetDevice()+" doe not have a interface named '"+options.getTargetInterface()+"'.");
            return;
        }

        STGroup slaveDriverHeader = new STGroupFile("LIN2/compiler/generation/DriverHeader.stg");
        target.addHeaderGroups(slaveDriverHeader);
        addModelAdaptors(slaveDriverHeader);

        STGroup slaveDriverSource = new STGroupFile("LIN2/compiler/generation/DriverSource.stg");
        target.addSourceGroups(slaveDriverSource);
        addModelAdaptors(slaveDriverSource);

        ST headerDriverGroup = slaveDriverHeader.getInstanceOf("driverHeader");
        headerDriverGroup.add("node", node);
        headerDriverGroup.add("target", target);
        headerDriverGroup.add("interface", inf);

        PrintWriter headerFile = new PrintWriter(new FileOutputStream(new File(outputDir,node.getName() + ".h")));
        headerFile.println(headerDriverGroup.render());
        headerFile.close();


        ST sourceDriverGroup = slaveDriverSource.getInstanceOf("driverSource");
        sourceDriverGroup.add("node",node);
        sourceDriverGroup.add("target", target);
        sourceDriverGroup.add("interface", inf);

        PrintWriter sourceFile = new PrintWriter(new FileOutputStream(new File(outputDir,node.getName()+".c")));
        sourceFile.println(sourceDriverGroup.render());
        sourceFile.close();
    }

    private static void addModelAdaptors(STGroup group) {
        /*group.registerModelAdaptor(Node.class,new NodeModelAdaptor());
        group.registerModelAdaptor(Frame.class,new FrameModelAdaptor());
        group.registerModelAdaptor(Entry.class,new PolymorphismModelAdaptor());
        group.registerModelAdaptor(Integer.class,new IntegerModelAdaptor());
        group.registerModelAdaptor(Signal.class,new SignalModelAdaptor());
        group.registerModelAdaptor(SignalValue.class,new SignalValueModelAdaptor());
        group.registerModelAdaptor(EncodedValue.class,new PolymorphismModelAdaptor());
        group.registerModelAdaptor(Bitrate.class,new PolymorphismModelAdaptor());*/
    }
}
