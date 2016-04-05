package com.ptransportation.LIN.generation.targets.PIC24FJxxGB00x;


import com.ptransportation.LIN.generation.Interface;

public class UART extends Interface {
    private int version;

    public UART(int version) {
        this.version = version;
    }

    @Override
    public String getName() {
        return "UART" + version;
    }

    @Override
    public String getGlobals() {
        return "interfaceGlobals";
    }

    @Override
    public String getInitialization() {
        return "interfaceInitialization";
    }

    @Override
    public String getRxBreakSync() {
        return "interfaceRxBreakSync";
    }

    @Override
    public String getRxDataAvailable() {
        return "interfaceRxDataAvailable";
    }

    @Override
    public String getRxData() {
        return "interfaceRxData";
    }

    @Override
    public String getTxData() {
        return "interfaceTxData";
    }

    @Override
    public String getTxBreakAndSync() {
        return "interfaceTxBreakAndSync";
    }
}
