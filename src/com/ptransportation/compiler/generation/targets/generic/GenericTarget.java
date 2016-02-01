package com.ptransportation.compiler.generation.targets.generic;

import com.ptransportation.compiler.generation.Interface;
import com.ptransportation.compiler.generation.Target;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class GenericTarget extends Target {
    public boolean targetMatches(String targetDevice) {
        return targetDevice.trim().toLowerCase().equals("generic");
    }

    @Override
    public Interface getInterface(String targetInterface) {
        String clean = targetInterface.trim().toLowerCase();
        if(clean.matches("^(generic\\d)")) {
            return new GenericInterface(Integer.parseInt(clean.replace("generic","")));
        }
        return null;
    }

    @Override
    public void addSourceGroups(STGroup sourceGroup) {
        sourceGroup.importTemplates(new STGroupFile("LIN2/compiler/generation/targets/generic/DriverSource.stg"));
    }
}
