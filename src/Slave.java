import java.util.ArrayList;

public class Slave {
    private String name;

    private String protocol;
    private int supplier;
    private int function;
    private int variant;
    private boolean sendsWakeUp;
    private Bitrate bitrate;

    private ArrayList<Integer> nads;
    private int diagnosticClass;

    public Slave(String name) {
        this.name = name;
        this.nads = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public int getFunction() {
        return function;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public int getVariant() {
        return variant;
    }

    public void setSendsWakeUp(boolean sendsWakeUp) {
        this.sendsWakeUp = sendsWakeUp;
    }

    public void setBitrate(Bitrate bitrate) {
        this.bitrate = bitrate;
    }

    public Bitrate getBitrate() {
        return bitrate;
    }

    public void addPossibleNAD(int NAD) {
        this.nads.add(NAD);
    }

    public void setDiagnosticClass(int diagnosticClass) {
        this.diagnosticClass = diagnosticClass;
    }

    public int getDiagnosticClass() {
        return diagnosticClass;
    }

    //node_name
    //general_definition
    //diagnostic_definition
    //frame_definition
    //encoding_definition
    //status_management
    //free_text_definition?
}
