package com.ptransportation.LIN.generator.generation;

public abstract  class Interface {
    public abstract String getName();

    public String getIncludes() { return null; }

    public String getGlobals() { return null; }

    public abstract String getInitialization();

    public abstract String getRxBreakSync();

    public abstract String getRxDataAvailable();

    public abstract String getRxData();

    public abstract String getTxData();

    public abstract String getTxBreakAndSync();
}
