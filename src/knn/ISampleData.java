package knn;

import Utils.HasLabel;
import Utils.Quantizer.NumericDataPoint;

public interface ISampleData extends NumericDataPoint {

    double[] getFeatures();

    double getFeatureByIndex(int index);

    String getItemClass();

}
