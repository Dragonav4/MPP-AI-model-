package dataClass;

public class IrisData { //storing a data from dataClass.IrisData
    float sepal_length;
    float sepal_width;
    float petal_length;
    float petal_width;
    String irisClass;

    public IrisData(float sepal_length, float sepal_width, float petal_length, float petal_width, String irisClass) {
        this.sepal_length = sepal_length;
        this.sepal_width = sepal_width;
        this.petal_length = petal_length;
        this.petal_width = petal_width;
        this.irisClass = irisClass;
    }

    public float[] getFeaturesOfIris() {
        return new float[] {
                this.petal_length,
                this.petal_width,
                this.sepal_length,
                this.sepal_width,
        };
    }
    public float getFeaturesOfIrisByIndex(int index) {
        return switch (index) {
            case 0 -> sepal_width;
            case 1 -> sepal_length;
            case 2 -> petal_width;
            case 3 -> petal_length;
            default -> throw new IllegalArgumentException("Uncorrected index provided :/");
        };
    }

    public String getIrisClass() {
        return irisClass;
    }
    @Override
    public String toString() {
        return STR."dataClass.IrisData{sepal_length=\{sepal_length}, sepal_width=\{sepal_width}, petal_length=\{petal_length}, petal_width=\{petal_width}, irisClass='\{irisClass}\{'\''}\{'}'}";
    }


}
