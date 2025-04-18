package Utils.Quantizer;

import Utils.HasLabel;

public interface NumericDataPoint extends HasLabel {
    double[] getNumericFeatures();
}
