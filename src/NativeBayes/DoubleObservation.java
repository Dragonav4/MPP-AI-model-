package NativeBayes;

import Utils.HasLabel;
import Utils.Quantizer.NumericDataPoint;
//import knn.ISampleData;

import java.util.Map;

public class DoubleObservation extends Observation<Double>
        implements HasLabel, NumericDataPoint {

    public DoubleObservation(Map<String, Double> attributes, String label) {
        super(attributes, label);
    }

    @Override
    public double[] getNumericFeatures() {
        return this.attributes.values().stream().mapToDouble(Double::doubleValue).toArray();

    }
    @Override
    public double getFeatureByIndex(int index) {
        try {
            return getNumericFeatures()[index];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
