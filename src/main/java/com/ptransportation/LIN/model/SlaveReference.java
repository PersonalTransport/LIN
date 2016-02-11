package com.ptransportation.LIN.model;

public class SlaveReference extends Slave implements Reference {
    private Slave linked;

    public SlaveReference(String name) {
        super(name);
    }

    @Override
    public void setName(String name) {
        if(linked != null && !linked.getName().equals(name))
            linked = null;
        super.setName(name);
    }

    public Slave getLinked() {
        return linked;
    }

    public void link(Slave slave) {
        this.linked = slave;
    }
}
