package com.ptransportation.LIN.generation;

import com.ptransportation.LIN.CommandLineOptions;
import com.ptransportation.LIN.generation.adaptors.*;
import com.ptransportation.LIN.model.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public abstract class Target {

    public abstract boolean targetMatches(String targetDevice);

    public abstract Interface getInterface(String targetInterface);

    public void addSourceGroups(STGroup sourceGroup) {
    }

    public void addHeaderGroups(STGroup headerGroup) {
    }

    public String getIncludes() {
        return null;
    }

    public String getGlobals() {
        return null;
    }

    public void generateDriver(CommandLineOptions options, Node node, File outputDir) throws FileNotFoundException {
        Interface inf = getInterface(options.getTargetInterface());
        if (inf == null) {
            System.err.println("'" + options.getTargetDevice() + "'" + " does not have a interface named '" + options.getTargetInterface() + "'.");
            System.exit(-1);
        }

        STGroup slaveDriverHeader = new STGroupFile("com/ptransportation/LIN/generation/targets/DriverHeader.stg");
        addHeaderGroups(slaveDriverHeader);
        addModelAdaptors(slaveDriverHeader);

        ST headerDriverGroup = slaveDriverHeader.getInstanceOf("driverHeader");
        headerDriverGroup.add("options", options);
        headerDriverGroup.add("node", node);
        headerDriverGroup.add("target", this);
        headerDriverGroup.add("interface", inf);

        PrintWriter headerFile = new PrintWriter(new FileOutputStream(new File(outputDir, node.getName() + ".h")));
        headerFile.println(headerDriverGroup.render());
        headerFile.close();

        STGroup slaveDriverSource = new STGroupFile("com/ptransportation/LIN/generation/targets/DriverSource.stg");
        addSourceGroups(slaveDriverSource);
        addModelAdaptors(slaveDriverSource);

        ST sourceDriverGroup = slaveDriverSource.getInstanceOf("driverSource");
        sourceDriverGroup.add("options", options);
        sourceDriverGroup.add("node", node);
        sourceDriverGroup.add("target", this);
        sourceDriverGroup.add("interface", inf);

        PrintWriter sourceFile = new PrintWriter(new FileOutputStream(new File(outputDir, node.getName() + ".c")));
        sourceFile.println(sourceDriverGroup.render());
        sourceFile.close();
    }

    protected void addModelAdaptors(STGroup group) {
        group.registerModelAdaptor(Number.class, new IntegerModelAdaptor());
        group.registerModelAdaptor(Node.class, new NodeModelAdaptor());
        group.registerModelAdaptor(Slave.class, new SlaveModelAdaptor());
        group.registerModelAdaptor(EncodedValue.class, new EncodedValueModelAdaptor());
        group.registerModelAdaptor(Bitrate.class, new BitrateModelAdaptor());
        group.registerModelAdaptor(Frame.class, new FrameModelAdaptor());
        group.registerModelAdaptor(Signal.class, new SignalModelAdaptor());
        group.registerModelAdaptor(SignalValue.class, new SignalValueModelAdaptor());
        group.registerModelAdaptor(ScheduleTable.class, new ScheduleTableModelAdaptor());
        group.registerModelAdaptor(ScheduleTableEntry.class, new ScheduleEntryModelAdaptor());
    }
}
