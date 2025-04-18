package knn;

public interface ISampleData {

    double[] getFeatures();

    double getFeatureByIndex(int index);

    String getItemClass();

}
