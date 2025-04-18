package PerceptronLayer;

import dataSet.Dataset;
import knn.ISampleData;

import java.util.*;

public class PLayerRunner {
    private static final String TRAIN_DATA_PATH = "resources/language/";
    private static final double TRAIN_RATIO = 0.8; //0,1
    private static final int INPUT_DIMENSION = 26;
    private static final int PERCEPTRON_EXTRA_PARAM = 2; //for threshold if needed


    private static final Map<String, Double> LANGUAGE_LABELS = Map.of(
            "english", 0.0,
            "french", 1.0,
            "polish", 2.0
    );

    public static void runLanguagePerceptronTraining(Scanner sc) {
        System.out.println("Training language perceptron...");
        var dataset = Dataset.loadSampleTextsFromDirectory(TRAIN_DATA_PATH);
        List<ISampleData> trainSet = new ArrayList<>();
        List<ISampleData> testSet  = new ArrayList<>();
        MultiClassPerceptron.trainTestSplit(dataset, trainSet, testSet, TRAIN_RATIO);

        List<double[]> trainInputs = new ArrayList<>();
        List<Double> trainLabels = new ArrayList<>();
        extractFeaturesAndLabels(trainSet, trainInputs, trainLabels);

        List<double[]> testInputs = new ArrayList<>();
        List<Double> testLabels = new ArrayList<>();
        extractFeaturesAndLabels(testSet, testInputs, testLabels);

        System.out.print("Enter learning rate (alpha): ");
        double alpha = sc.nextDouble();
        //System.out.print("Enter maximum number of epochs: ");
        //int epochs = sc.nextInt();

        // creating perceptrons for each language
        Map<Double, PerceptronL> languagePerceptrons = new HashMap<>();
        for (double label : LANGUAGE_LABELS.values()) {
            languagePerceptrons.put(label, new PerceptronL(INPUT_DIMENSION, PERCEPTRON_EXTRA_PARAM, alpha));
        }

        MultiClassPerceptron mcPerceptron = new MultiClassPerceptron(languagePerceptrons);
        mcPerceptron.trainLayer(trainInputs, trainLabels);

        System.out.println("Training completed!");
        PLayerUtils.demoLanguageClassificationFromFile(mcPerceptron, testInputs, testLabels);
    }

    private static void extractFeaturesAndLabels(
            List<ISampleData> data,
            List<double[]> inputs,
            List<Double> labels) {
        for (var entry : data) { //
            String language = entry.getItemClass();
            double label = LANGUAGE_LABELS.getOrDefault(language, -1.0);
            inputs.add(entry.getFeatures()); // add data to this language(each letter represents a probability being in the text
            labels.add(label); // adds language in digit format
        }
    }
}