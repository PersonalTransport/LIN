package com.ptransportation.LIN.link;

import com.ptransportation.LIN.model.Reference;

public class DefaultLinker extends AbstractLinker {
    @Link
    public void linksdf(Reference reference) {
        System.out.println(reference.getClass().getSimpleName());
    }
}
