package PerceptronLayer;

import java.util.List;
import java.util.Map;

public class PerceptronData {
    Map<String, List<double[]>> trainMap;
    Map<String, List<double[]>> testMap;

    public PerceptronData(Map<String, List<double[]>> trainMap, Map<String, List<double[]>> testMap) {
        this.trainMap = trainMap;
        this.testMap = testMap;
    }

    public Map<String, List<double[]>> getTrainMap() {
        return trainMap;
    }

    public Map<String, List<double[]>> getTestMap() {
        return testMap;
    }
}
