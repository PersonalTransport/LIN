package com.ptransportation.LIN.generation.targets.PIC24FJxxGB00x;

import com.ptransportation.LIN.CommandLineOptions;
import com.ptransportation.LIN.generation.Interface;
import com.ptransportation.LIN.generation.Target;
import com.ptransportation.LIN.model.Node;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class PIC24FJxxGB00x extends Target {
    public String device;
    public boolean targetMatches(String targetDevice) {
        device = targetDevice.trim().toUpperCase();
        return device.matches("^(PIC24FJ\\d\\dG[A|B]00\\d)") || device.matches("^(DSPIC33EP64MC502)");
    }

    @Override
    public Interface getInterface(String targetInterface) {
        String clean = targetInterface.trim().toUpperCase();
        if (clean.matches("^(UART\\d)")) {
            int version = Integer.parseInt(clean.replace("UART", ""));
            if (version == 1 || version == 2) // TODO add support for more UART modules.
                return new UART(version);
        }
        return null;
    }

    @Override
    public void addSourceGroups(STGroup sourceGroup) {
        sourceGroup.importTemplates(new STGroupFile("com/ptransportation/LIN/generation/targets/PIC24FJxxGB00x/DriverSource.stg"));
    }

    @Override
    public void addHeaderGroups(STGroup headerGroup) {
        headerGroup.importTemplates(new STGroupFile("com/ptransportation/LIN/generation/targets/PIC24FJxxGB00x/DriverHeader.stg"));
    }

    @Override
    public String getIncludes() {
        return "targetIncludes";
    }

    @Override
    public String getGlobals() {
        return "targetGlobals";
    }

    public void generateDriver(CommandLineOptions options, Node node, File outputDir) throws FileNotFoundException {
        super.generateDriver(options, node, outputDir);

        if (options.isAOA()) {
            STGroup aoaHeader = new STGroupFile("com/ptransportation/LIN/generation/targets/PIC24FJxxGB00x/AOAHeader.stg");
            addHeaderGroups(aoaHeader);
            addModelAdaptors(aoaHeader);

            ST aoaHeaderGroup = aoaHeader.getInstanceOf("aoaHeader");
            aoaHeaderGroup.add("options", options);
            aoaHeaderGroup.add("node", node);
            aoaHeaderGroup.add("target", this);

            PrintWriter headerFile = new PrintWriter(new FileOutputStream(new File(outputDir, node.getName() + "_aoa.h")));
            headerFile.println(aoaHeaderGroup.render());
            headerFile.close();

            STGroup aoaSource = new STGroupFile("com/ptransportation/LIN/generation/targets/PIC24FJxxGB00x/AOASource.stg");
            addSourceGroups(aoaSource);
            addModelAdaptors(aoaSource);

            ST aoaSourceGroup = aoaSource.getInstanceOf("aoaSource");
            aoaSourceGroup.add("options", options);
            aoaSourceGroup.add("node", node);
            aoaSourceGroup.add("target", this);

            PrintWriter sourceFile = new PrintWriter(new FileOutputStream(new File(outputDir, node.getName() + "_aoa.c")));
            sourceFile.println(aoaSourceGroup.render());
            sourceFile.close();
        }
    }
}
