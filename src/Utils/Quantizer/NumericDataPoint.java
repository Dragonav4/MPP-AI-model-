package Utils.Quantizer;

import Utils.HasLabel;

public interface NumericDataPoint extends HasLabel {
    double[] getNumericFeatures();
    double getFeatureByIndex(int index);
}
