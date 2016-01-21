package LIN2.compiler.generation.PIC24FJxxGB00x;

import LIN2.compiler.generation.Interface;

public class UART1 extends Interface {

    @Override
    public String getName() {
        return "UART1";
    }

    @Override
    public String getGlobals() { return "globalsUART1"; }

    @Override
    public String getInitialization() {
        return "initializationUART1";
    }

    @Override
    public String getRxDataAvailable() {
        return "rxDataAvailableUART1";
    }

    @Override
    public String getRxData() {
        return "rxDataUART1";
    }

    @Override
    public String getTxData() {
        return "txDataUART1";
    }

    @Override
    public String getTxBreakAndSync() {
        return "txBreakAndSyncUART1";
    }
}
