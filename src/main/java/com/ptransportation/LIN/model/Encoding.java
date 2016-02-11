package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Encoding {
    private Node node;
    private String name;
    private List<EncodedValue> encodedValues;

    public Encoding(String name) {
        this.name = name;
        this.encodedValues = new ArrayList<EncodedValue>();
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EncodedValue> getEncodedValues() {
        return this.encodedValues;
    }
}
