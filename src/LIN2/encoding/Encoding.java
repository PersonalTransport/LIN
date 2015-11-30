package LIN2.encoding;

import java.util.ArrayList;

public class Encoding {
    private String name;
    private ArrayList<EncodedValue> encodedValues;

    public Encoding(String name) {
        this.name = name;
        this.encodedValues = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addEncodedValue(EncodedValue value) {
        encodedValues.add(value);
    }

    public void removeEncodedValue(EncodedValue value) {
        encodedValues.remove(value);
    }

    public ArrayList<EncodedValue> getEncodedValues() {
        return encodedValues;
    }
}
