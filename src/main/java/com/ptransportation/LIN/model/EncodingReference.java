package com.ptransportation.LIN.model;

public class EncodingReference extends Encoding implements Reference {
    private Encoding linked;

    public EncodingReference(String name) {
        super(name);
    }

    @Override
    public void setName(String name) {
        if(linked != null && !linked.getName().equals(name))
            linked = null;
        super.setName(name);
    }

    public Encoding getLinked() {
        return this.linked;
    }

    public void link(Encoding encoding) {
        this.linked = encoding;
    }
}
