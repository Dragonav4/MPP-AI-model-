public class IrisData { //storing a data from IrisData
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
    public String getIrisClass() {
        return irisClass;
    }
    @Override
    public String toString() {
        return STR."IrisData{sepal_length=\{sepal_length}, sepal_width=\{sepal_width}, petal_length=\{petal_length}, petal_width=\{petal_width}, irisClass='\{irisClass}\{'\''}\{'}'}";
    }


}
