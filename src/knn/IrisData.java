package knn;

import Utils.HasLabel;

public class IrisData implements ISampleData, HasLabel { //storing a data from knn.IrisData
    double[] features;
    String itemClass;

    public IrisData(
            double sepal_length,
            double sepal_width,
            double petal_length,
            double petal_width,
            String itemClass) {
        this.features = new double[] {
                sepal_length,
                sepal_width,
                petal_length,
                petal_width,
        };
        this.itemClass = itemClass;
    }

    @Override
    public double[] getFeatures() {
        return features;
    }
    @Override
    public double getFeatureByIndex(int index) {
        return features[index];
    }

    @Override
    public String getItemClass() {
        return itemClass;
    }
    @Override
    public String toString() {
        return STR."knn.IrisData{sepal_length=\{features[2]}, sepal_width=\{features[3]}, petal_length=\{features[0]}, petal_width=\{features[1]}, irisClass='\{itemClass}\{'\''}\{'}'}";
    }

    public static ISampleData getSampleData(String line) {
        if (line.trim().isEmpty())
            return null;
        try {
            String[] values = line.split(",");

            float sepalLen = Float.parseFloat(values[0]);
            float sepalWid = Float.parseFloat(values[1]);
            float petalLen = Float.parseFloat(values[2]);
            float petalWid = Float.parseFloat(values[3]);
            String irisType = values[4];

            return new IrisData(sepalLen, sepalWid, petalLen, petalWid, irisType);
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static double getBinaryClass(ISampleData data) {
        return data.getItemClass().contains("setosa") ? 1.0 : 0.0;
    }

    @Override
    public String getLabel() {
        return itemClass;
    }

    @Override
    public double[] getNumericFeatures() {
        return features;
    }
}
