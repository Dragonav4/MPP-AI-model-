package NAI7;

import Utils.HasLabel;

import java.util.Map;

public class Observation implements HasLabel {
    public Map<String,String> attributes;
    public String label;

    public Observation(Map<String, String> attributes, String label) {
        this.attributes = attributes;
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}