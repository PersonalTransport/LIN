package com.ptransportation.LIN.model;

public class SignalReference extends Signal implements Reference {

    private Signal linked;

    public SignalReference(String name) {
        super(name);
    }

    @Override
    public void setName(String name) {
        if (linked != null && !linked.getName().equals(name))
            linked = null;
        super.setName(name);
    }

    public Signal getLinked() {
        return linked;
    }

    public void link(Signal signal) {
        linked = signal;
    }
}
