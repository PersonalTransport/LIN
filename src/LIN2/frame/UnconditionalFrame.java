package LIN2.frame;

import LIN2.Node;
import LIN2.encoding.Encoding;
import LIN2.signal.Signal;

import java.util.ArrayList;

public class UnconditionalFrame extends Frame {
    private Node publisher;
    private ArrayList<Signal> signals;
    private ArrayList<Node> subscribers;

    public UnconditionalFrame(String name) {
        super(name);
        this.signals = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public void setPublisher(Node publisher) {
        this.publisher = publisher;
    }

    @Override
    public boolean publishedBy(Node node) {
        return node == publisher;
    }

    public Node getPublisher() {
        return publisher;
    }

    public void addSignal(Signal signal) {
        if (!signals.contains(signal)) { // Do not add the same signal
            signals.add(signal);
            signal.setFrame(this);
        }
    }

    public void removeSignal(Signal signal) {
        if(signals.contains(signal)) {
            this.signals.remove(signal);
            signal.setFrame(null);
        }
    }

    public Signal getSignal(String signalName) {
        for(Signal signal:signals) {
            if(signal.getName().equals(signalName))
                return  signal;
        }
        return null;
    }

    public ArrayList<Signal> getSignals() {
        return signals;
    }

    public Encoding getEncoding(String encodingName) {
        for(Signal signal:signals) {
            Encoding encoding = signal.getEncoding();
            if(encoding != null && encoding.getName().equals(encodingName))
                return encoding;
        }
        return null;
    }

    public ArrayList<Encoding> getEncodings() {
        ArrayList<Encoding> encodings = new ArrayList<>();
        for(Signal signal:signals) {
            Encoding encoding = signal.getEncoding();
            if(encoding != null && !encodings.contains(encoding))
                encodings.add(encoding);
        }
        return encodings;
    }

    public void addSubscriber(Node subscriber) {
        if(!this.subscribers.contains(subscriber))
            this.subscribers.add(subscriber);
    }

    public ArrayList<Node> getSubscribers() {
        return subscribers;
    }
}
