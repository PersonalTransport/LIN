package com.ptransportation.LIN.generation.targets.AndroidAccessory;


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

public class AndroidAccessory extends Target {
    @Override
    public boolean targetMatches(String targetDevice) {
        return targetDevice.trim().equalsIgnoreCase("AndroidAccessory");
    }

    @Override
    public Interface getInterface(String targetInterface) {
        return new Package(targetInterface);
    }

    @Override
    public void generateDriver(CommandLineOptions options, Node node, File outputDir) throws FileNotFoundException {
        Package pkg = (Package) getInterface(options.getTargetInterface());

        outputDir = new File(outputDir.getPath() + "/" + pkg.getPath());
        if(!outputDir.exists())
            outputDir.mkdirs();

        STGroup sourceGroup = new STGroupFile("com/ptransportation/LIN/generation/targets/AndroidAccessory/Source.stg");
        addSourceGroups(sourceGroup);
        addModelAdaptors(sourceGroup);

        ST source = sourceGroup.getInstanceOf("source");
        source.add("options", options);
        source.add("node", node);
        source.add("target", this);
        source.add("interface", pkg);

        PrintWriter sourceFile = new PrintWriter(new FileOutputStream(new File(outputDir, node.getName() + ".java")));
        sourceFile.println(source.render());
        sourceFile.close();
    }
}
