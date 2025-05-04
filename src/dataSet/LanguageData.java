package dataSet;

import NativeBayes.DoubleObservation;

import java.util.Map;

public class LanguageData extends DoubleObservation {
    public LanguageData(Map<String, Double> attributes, String label) {
        super(attributes, label);
    }
}
