package knn;

public class LanguageData implements ISampleData{
    private final double[] features;
    private final String itemClass;

    public LanguageData(double[] features, String itemClass) {
        this.features = features;
        this.itemClass = itemClass;
    }


    @Override
    public double[] getFeatures() {
        return this.features;
    }

    @Override
    public double getFeatureByIndex(int index) {
        return this.features[index];
    }

    @Override
    public String getItemClass() {
        return this.itemClass;
    }

    @Override
    public String getLabel() {
        return this.itemClass;
    }

    @Override
    public double[] getNumericFeatures() {
        return this.features;
    }
}
