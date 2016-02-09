package com.ptransportation.LIN.compiler.generation.targets.PIC24FJxxGB00x;

import com.ptransportation.LIN.compiler.generation.Interface;
import com.ptransportation.LIN.compiler.generation.Target;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class PIC24FJxxGB00x extends Target {

    public boolean targetMatches(String targetDevice) {
        return targetDevice.trim().toUpperCase().matches("^(PIC24FJ\\d\\dGB00\\d)");
    }

    @Override
    public Interface getInterface(String targetInterface) {
        String clean = targetInterface.trim().toUpperCase();
        if(clean.matches("^(UART\\d)")) {
            int version = Integer.parseInt(clean.replace("UART",""));
            if(version == 1) // TODO add support for more UART modules.
                return new UART(version);
        }
        return null;
    }

    @Override
    public void addSourceGroups(STGroup sourceGroup) {
        sourceGroup.importTemplates(new STGroupFile("com/ptransportation/LIN/compiler/generation/targets/PIC24FJxxGB00x/DriverSource.stg"));
    }

    @Override
    public void addHeaderGroups(STGroup headerGroup) {
        headerGroup.importTemplates(new STGroupFile("com/ptransportation/LIN/compiler/generation/targets/PIC24FJxxGB00x/DriverHeader.stg"));
    }

    @Override
    public String getIncludes() {
        return "includes";
    }

    @Override
    public String getGlobals() {
        return "pic24FJxxGB00xGlobals";
    }
}
