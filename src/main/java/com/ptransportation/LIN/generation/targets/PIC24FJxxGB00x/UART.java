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
        return "globalsUART" + version;
    }

    @Override
    public String getInitialization() {
        return "initializationUART" + version;
    }

    @Override
    public String getRxBreakSync() {
        return "rxBreakSyncUART" + version;
    }

    @Override
    public String getRxDataAvailable() {
        return "rxDataAvailableUART" + version;
    }

    @Override
    public String getRxData() {
        return "rxDataUART" + version;
    }

    @Override
    public String getTxData() {
        return "txDataUART" + version;
    }

    @Override
    public String getTxBreakAndSync() {
        return "txBreakAndSyncUART" + version;
    }
}
