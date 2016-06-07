package com.ptransportation.LIN.generation.targets.AndroidAccessory;

import com.ptransportation.LIN.generation.Interface;

public class Package extends Interface {
    private String packageName;

    public Package(String name) {
        this.packageName = name;
    }

    @Override
    public String getName() {
        return packageName;
    }

    @Override
    public String getInitialization() {
        return null;
    }

    @Override
    public String getRxDataAvailable() {
        return null;
    }

    @Override
    public String getRxData() {
        return null;
    }

    @Override
    public String getTxData() {
        return null;
    }

    @Override
    public String getTxBreakAndSync() {
        return null;
    }

    public String getPath() {
        return packageName.replaceAll("\\.","//");
    }
}
