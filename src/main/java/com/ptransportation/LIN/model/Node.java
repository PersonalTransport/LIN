package com.ptransportation.LIN.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    private String name;
    private double protocolVersion;
    private int supplier;
    private int function;
    private int variant;
    private Bitrate bitrate;
    private List<Frame> frames;
    private List<Encoding> encodings;
    private String freeText;

    public Node(String name) {
        this.name = name;
        this.frames = new ArrayList<Frame>();
        this.encodings = new ArrayList<Encoding>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(double version) {
        this.protocolVersion = version;
    }

    public int getSupplier() {
        return this.supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public int getFunction() {
        return this.function;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public int getVariant() {
        return this.variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public Bitrate getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(Bitrate bitrate) {
        this.bitrate = bitrate;
    }

    public List<Frame> getFrames() {
        return this.frames;
    }

    public List<Frame> getPublishFrames() {
        ArrayList<Frame> publishFrames = new ArrayList<>();
        for(Frame frame:this.frames) {
            if(frame.getPublisher() == this)
                publishFrames.add(frame);
        }
        return publishFrames;
    }

    public List<Frame> getSubscribeFrames() {
        ArrayList<Frame> subscribeFrames = new ArrayList<>();
        for(Frame frame:this.frames) {
            if(frame.getPublisher() != this)
                subscribeFrames.add(frame);
        }
        return subscribeFrames;
    }

    public List<Encoding> getEncodings() {
        return this.encodings;
    }

    public String getFreeText() {
        return this.freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }
}
