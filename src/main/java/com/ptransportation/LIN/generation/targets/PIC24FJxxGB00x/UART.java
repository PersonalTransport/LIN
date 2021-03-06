package com.ptransportation.LIN.generation.targets.PIC24FJxxGB00x;


import com.ptransportation.LIN.generation.Interface;

public class UART extends Interface {
    private int version;

    public UART(int version) {
        this.version = version;
    }

    public int getVersion() {
        return this.version;
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

    public String getBaudRateRegister() {
        return "U" + version + "BRG";
    }

    public String getModeRegister() {
        return "U" + version + "MODEbits";
    }

    public String getStatusRegister() {
        return "U" + version + "STAbits";
    }

    public String getRxInterruptFlag() {
        return (version == 1) ? "IFS0bits.U1RXIF" : "IFS1bits.U2RXIF";
    }

    public String getTxInterruptFlag() {
        return (version == 1) ? "IFS0bits.U1TXIF" : "IFS1bits.U2TXIF";
    }

    public String getTxRegister() {
        return "U" + version + "TXREG";
    }

    public String getRxRegister() {
        return "U" + version + "RXREG";
    }

    public String getInputFunction() {
        if (version == 1) {
            return "RPINR18bits.U1RXR";
        } else if (version == 2) {
            return "RPINR19bits.U2RXR";
        }
        return "";
    }

    public String getOutputFunction() {
        if (version == 1) {
            return "3"; // TODO this needs to be different ("1") for the dsPIC33EP MCUs
        } else if (version == 2) {
            return "5"; // TODO this needs to be different ("3") for the dsPIC33EP MCUs
        }
        return "";
    }

}
