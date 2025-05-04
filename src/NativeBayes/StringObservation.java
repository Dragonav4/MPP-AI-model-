package NativeBayes;

import java.util.Map;

public class StringObservation extends Observation<String> {
    public StringObservation(Map<String, String> attributes, String label) {
        super(attributes, label);
    }
}
