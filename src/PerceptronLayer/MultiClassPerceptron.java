package PerceptronLayer;

import NativeBayes.DoubleObservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class MultiClassPerceptron {
    //            "english", 0.0 -> Map<0.0, PerceptronEnglish>
    private final Map<Double, PerceptronL> perceptronMap;

    public MultiClassPerceptron(Map<Double, PerceptronL> perceptronMap) {
        this.perceptronMap = perceptronMap;
    }


    //checking which perceptron is better to use for this language
    //all perceptron gives answer on one request and we peek the most precise one
    public double predict(double[] input) {
        double bestOutput = Double.NEGATIVE_INFINITY;
        double bestLabel = -1;

        for (var entry : perceptronMap.entrySet()) {
            double output = entry.getValue().rawOutput(input);
            if (output > bestOutput) {
                bestOutput = output;
                bestLabel = entry.getKey();
            }
        }
        System.out.println("Best label: " + bestLabel + ", Best output: " + bestOutput);
        return bestLabel;
    }


    public void trainLayer(List<double[]> inputs, List<Double> labels) {
        for (double classLabel : perceptronMap.keySet()) {
            List<double[]> classBinaryVectors = new ArrayList<>();
            List<Double> classBinaryLabels = new ArrayList<>();

            for (int i = 0; i < inputs.size(); i++) {
                classBinaryVectors.add(inputs.get(i));
                classBinaryLabels.add(labels.get(i) == classLabel ? 1d : 0);
            }
            perceptronMap.get(classLabel).train(classBinaryVectors, classBinaryLabels); // train it by binary classification
            // each perceptron will know only 1 language(get language and train it for 1 language)
        }
    }

    public Map<Double, PerceptronL> getPerceptronMap() {
        return perceptronMap;
    }

    public static void trainTestSplit(List<DoubleObservation> dataSet, // whole dataSet
                                      List<DoubleObservation> trainSet, // teach model
                                      List<DoubleObservation> testSet, // test model
                                      double trainRatio) { // 66% train and 34% test
        trainSet.clear();
        testSet.clear();
        List<String> uniqueClasses = new ArrayList<>();
        for (var ISampleData : dataSet) { // Finding all unique classes which are not in our list
            if (!uniqueClasses.contains(ISampleData.getLabel())) {
                uniqueClasses.add(ISampleData.getLabel());
            }
        }
        for (String irisClass : uniqueClasses) { // adds data to correspond class
            List<DoubleObservation> irisClassData = new ArrayList<>();
            for (var data : dataSet) {
                if (data.getLabel().equals(irisClass)) {
                    irisClassData.add(data);
                }
            }
            Collections.shuffle(irisClassData, new Random());
            int trainSize = (int) Math.floor(irisClassData.size() * trainRatio);

            for (int i = 0; i < trainSize; i++) { //first part will go to trainSet
                trainSet.add(irisClassData.get(i));
            }

            for (int i = trainSize; i < irisClassData.size(); i++) { // and rest of it to testSet
                testSet.add(irisClassData.get(i));
            }

            Collections.shuffle(trainSet, new Random());
            Collections.shuffle(testSet, new Random());
        }
    }


    public static List<DoubleObservation> readDataSet(String path, Function<String, DoubleObservation> inputParser) {
        List<DoubleObservation> result = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bf.readLine()) != null) {
                var data = inputParser.apply(line);
                if (data != null)
                    result.add(data);

            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return result;
    }

}