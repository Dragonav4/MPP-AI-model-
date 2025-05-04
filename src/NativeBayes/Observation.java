package NativeBayes;

import Utils.HasLabel;

import java.util.Map;

public class Observation<T> implements HasLabel{
    public Map<String,T> attributes; //numeric or categoral attrs
    public String label;

    public Observation(Map<String, T> attributes, String label) {
        this.attributes = attributes;
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

}

