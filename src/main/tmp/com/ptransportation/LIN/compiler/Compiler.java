package com.ptransportation.LIN.generator;


import com.beust.jcommander.ParameterException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.ptransportation.LIN.generator.generation.Interface;
import com.ptransportation.LIN.generator.generation.Target;
import com.ptransportation.LIN.generator.generation.models.*;
import com.ptransportation.LIN.generator.generation.targets.PIC24FJxxGB00x.PIC24FJxxGB00x;
import com.ptransportation.LIN.generator.generation.targets.generic.GenericTarget;
import com.ptransportation.capability.NodeCapabilityFileStandaloneSetup;
import com.ptransportation.capability.nodeCapabilityFile.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.generator.GeneratorDelegate;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.*;
import java.util.List;

public class Compiler {
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

        Injector injector = new NodeCapabilityFileStandaloneSetup().createInjectorAndDoEMFRegistration();
        Compiler compiler = injector.getInstance(Compiler.class);

        compiler.compile(compilerOptions);
    }

    @Inject
    private Provider<ResourceSet> resourceSetProvider;

    @Inject
    private IResourceValidator validator;

    @Inject
    private GeneratorDelegate generator;

    @Inject
    private JavaIoFileSystemAccess fileAccess;

    public Target[] targets = {
            new GenericTarget(),
            new PIC24FJxxGB00x()
    };

    private void compile(CompilerOptions compilerOptions) throws IOException {
        if(compilerOptions.getSlaveDriverOptions() != null) {
            CompilerOptions.SlaveDriverOptions slaveOptions = compilerOptions.getSlaveDriverOptions();

            ResourceSet set = resourceSetProvider.get();

            for(String source:slaveOptions.getSources())
                set.createResource(URI.createURI(source));

            Resource resource = set.getResource(URI.createURI(slaveOptions.getSources().get(0)),true);

            // Validate the resource
            List<Issue> list = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
            if (!list.isEmpty()) {
                for (Issue issue : list) {
                    System.err.println(issue);
                }
                return;
            }

            /* Configure and start the generator
            fileAccess.setOutputPath("gen/");
            GeneratorContext context = new GeneratorContext();
            context.setCancelIndicator(CancelIndicator.NullImpl);
            generator.generate(resource, fileAccess, context);*/

            File outputDir = new File(slaveOptions.getOutputDirectory()).getCanonicalFile();

            NodeCapabilityFile file = (NodeCapabilityFile)resource.getContents().get(0);
            Slave node = (Slave)file.getNode();
            node.getSupportedSIDS().add("0xB2");
            node.getSupportedSIDS().add("0xB7");
            generateDriver(compilerOptions,outputDir,node);
        }
        else if(compilerOptions.getMasterDriverOptions() != null) {
            CompilerOptions.MasterDriverOptions masterOptions = compilerOptions.getMasterDriverOptions();

            ResourceSet set = resourceSetProvider.get();

            for(String source:masterOptions.getSources())
                set.createResource(URI.createURI(source));

            Resource resource = null;
            for(String source:masterOptions.getSources()) {
                resource = set.getResource(URI.createURI(source),true);
                NodeCapabilityFile file = (NodeCapabilityFile)resource.getContents().get(0);
                if(file.getNode() instanceof Master)
                    break;
            }

            if(resource == null) {
                System.err.println("Missing master node.");
                return;
            }

            // Validate the resource
            List<Issue> list = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
            if (!list.isEmpty()) {
                for (Issue issue : list) {
                    System.err.println(issue);
                }
                return;
            }

            NodeCapabilityFile masterFile = (NodeCapabilityFile)resource.getContents().get(0);
            Master master = (Master)masterFile.getNode();
            File outputDir = new File(masterOptions.getOutputDirectory()).getCanonicalFile();
            generateDriver(compilerOptions,outputDir,master);

            // TODO remove this when Node configuration and identification is implemented!
            for(Slave slave:master.getSlaves()) {
                String ncf = slave.eResource().getURI().toString();
                String ncfRoot = new File(ncf).getParent();
                outputDir = new File(ncfRoot+"/gen");
                generateDriver(compilerOptions,outputDir,slave);
            }
        }
    }

    public void generateDriver(CompilerOptions options,File outputDir,Node node) throws FileNotFoundException {
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

        STGroup slaveDriverHeader = new STGroupFile("com/ptransportation/LIN/compiler/generation/DriverHeader.stg");
        target.addHeaderGroups(slaveDriverHeader);
        addModelAdaptors(slaveDriverHeader);

        STGroup slaveDriverSource = new STGroupFile("com/ptransportation/LIN/compiler/generation/DriverSource.stg");
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
        group.registerModelAdaptor(Integer.class,new IntegerModelAdaptor());

        group.registerModelAdaptor(Master.class, new MasterModelAdaptor());
        group.registerModelAdaptor(Slave.class, new SlaveModelAdaptor());
        group.registerModelAdaptor(Frame.class, new FrameModelAdaptor());
        group.registerModelAdaptor(Signal.class, new SignalModelAdaptor());
        group.registerModelAdaptor(ScheduleTable.class, new ScheduleTableModelAdaptor());
        group.registerModelAdaptor(ScheduleTableEntry.class, new ScheduleEntryModelAdaptor());

        /*group.registerModelAdaptor(Node.class,new NodeModelAdaptor());
        group.registerModelAdaptor(Frame.class,new FrameModelAdaptor());
        group.registerModelAdaptor(Entry.class,new PolymorphismModelAdaptor());
        group.registerModelAdaptor(Signal.class,new SignalModelAdaptor());
        group.registerModelAdaptor(SignalValue.class,new SignalValueModelAdaptor());
        group.registerModelAdaptor(EncodedValue.class,new PolymorphismModelAdaptor());
        group.registerModelAdaptor(Bitrate.class,new PolymorphismModelAdaptor());*/
    }
}