package com.ptransportation.compiler.generation.targets.generic;


import com.ptransportation.compiler.generation.Interface;

public class GenericInterface extends Interface {
    private int number;

    public GenericInterface(int number) {
        this.number = number;
    }

    @Override
    public String getName() {
        return "Generic"+ this.number;
    }

    @Override
    public String getInitialization() {
        return "initializationGenericInterface";
    }

    @Override
    public String getRxBreakSync() {
        return "rxBreakSyncGenericInterface";
    }

    @Override
    public String getRxDataAvailable() {
        return "rxDataAvailableGenericInterface";
    }

    @Override
    public String getRxData() {
        return "rxDataGenericInterface";
    }

    @Override
    public String getTxData() {
        return "txDataGenericInterface";
    }

    @Override
    public String getTxBreakAndSync() {
        return "txBreakAndSyncGenericInterface";
    }
}
