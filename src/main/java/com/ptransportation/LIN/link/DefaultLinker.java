package com.ptransportation.LIN.link;

import com.ptransportation.LIN.model.*;

import java.util.ArrayList;
import java.util.List;

public class DefaultLinker extends AbstractLinker {
    private List<Node> nodes;

    public DefaultLinker(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void link() {
        for(Node node:nodes) {
            super.link(node);
        }
    }

    @Link
    public void linkMasterSlaves(Master master) {
        List<Slave> slaves = master.getSlaves();
        for(int i=0;i<slaves.size();++i) {
            Slave refSlave = slaves.get(i);
            Slave slave = null;
            for(Node node:nodes) {
                if (node instanceof Slave && node.getName().equals(refSlave.getName())) {
                    slave = (Slave)node;
                }
            }
        }
    }

    /*@Link
    public void linkResponseError(Slave slave) {
        Signal responseError = slave.getResponseError();
        Signal signal = getPublishedSignal(slave,responseError.getName());
        if(signal != null)
            slave.setResponseError(signal); // TODO mark responseError as reference to signal
        else
            error("Signal '"+responseError.getName()+"' was not defined.",slave,"responseError");
    }

    @Link
    public void linkFaultStateSignals(Slave slave) {
        List<Signal> fsSignals = slave.getFaultStateSignals();
        for(int i=0;i<fsSignals.size();++i) {
            Signal fsSignal = fsSignals.get(0);
            Signal signal = getPublishedSignal(slave,fsSignal.getName());
            if(signal != null)
                fsSignals.set(i, signal); // TODO mark fsSignal as reference to signal
            else
                error("Signal '"+fsSignal.getName()+"' was not defined.",slave,"faultStateSignals",i);
        }
    }*/

    private Signal getPublishedSignal(Node node, String name) {
        for(Frame frame:node.getFrames()) {
            if(frame.getPublishes()) {
                for(Signal signal:frame.getSignals())
                    if(signal.getName().equals(name))
                        return signal;
            }
        }
        return null;
    }
}
