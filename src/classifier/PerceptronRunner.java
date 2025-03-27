package classifier;

import Plots.AccuracyPlot;
import Plots.DecisionBoundaryPlot;
import dataSet.PrepareDataset;
import dataSet.TextProcessor;
import knn.IrisData;
import java.util.*;

public class PerceptronRunner {
    public static Perceptron runPerceptron(
            List<IrisData> trainSet,
            List<IrisData> testSet,
            Scanner sc
    ) {
        double[][] trainFeatures = PrepareDataset.toFeatures(trainSet, 1, 2); // sepal_width and // sepal_length
        double[][] testFeatures = PrepareDataset.toFeatures(testSet, 1, 2); // remake from List<dataClass.IrisData> -> double[][]

        double[] trainLabels = PrepareDataset.toLabels(trainSet); // setosa -> 0 versicolor ->1
        double[] testLabels = PrepareDataset.toLabels(testSet);

        System.out.print("Enter alpha (learning rate): ");
        double userAlpha = sc.nextDouble();
        System.out.print("Enter the number of epochs: ");
        int userEpochs = sc.nextInt();

        //Perceptron perceptron = new Perceptron(2, 1.0, userAlpha, userEpochs);
        Perceptron perceptron = new Perceptron(2, 2, 1, userAlpha, userEpochs);

        double[] accPerEpoch = perceptron.train(trainFeatures, trainLabels, userAlpha);
        AccuracyPlot.showAccuracyChart(accPerEpoch);
        DecisionBoundaryPlot.showDecisionBoundary(testFeatures, testLabels, perceptron);

        //going through all examples and save our predictions(0 or 1)
        int[] perceptronPredicted = new int[testFeatures.length];
        for (int i = 0; i < testFeatures.length; i++) {
            perceptronPredicted[i] = perceptron.predictP(testFeatures[i]);
        }

        double accuracy = EvaluationMetrics.measurePerceptronAccuracy(testLabels, perceptronPredicted);
        double fmeasure = EvaluationMetrics.fmeasure(testLabels, perceptronPredicted, 0);
        System.out.printf("classifier.Perceptron: accuracy on test set = %.2f%%\n", accuracy * 100.0);
        System.out.printf("classifier.Perceptron: fmeasure: %.2f%%\n", fmeasure * 100.0);

        return perceptron;
    }

    public static void runLanguagePerceptronTraining(Scanner sc, Map<Double, String> labelMap, Map<String, String> sampleTexts) {
        double loss;
        double accuracy;
        System.out.println("Training language perceptron...");

        String trainSetPath = "/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/src/data/language_dataSet";
        Map<String, String> trainTexts = PerceptronUtils.loadSampleTextsFromFile(trainSetPath);

        int totalSamples = trainTexts.size();
        double[][] trainInputs = new double[totalSamples][26];
        double[] trainLabels = new double[totalSamples];

        Map<String, Double> stringToLabel = Map.of(
                "en", 0.0,
                "fr", 1.0,
                "pl", 2.0
        );

        int idx = 0;
        for (Map.Entry<String, String> entry : trainTexts.entrySet()) {
            trainInputs[idx] = TextProcessor.textToVector(entry.getValue());
            trainLabels[idx] = stringToLabel.getOrDefault(entry.getKey(), -1.0);
            idx++;
        }

        System.out.print("Write speed of learning(alpha): ");
        double alpha = sc.nextDouble();
        System.out.print("Amount of epochs: ");
        int epochs = sc.nextInt();

        Map<Double, Perceptron> languagePerceptrons = new HashMap<>();
        languagePerceptrons.put(0.0, new Perceptron(26, 2, 1, alpha, epochs));
        languagePerceptrons.put(1.0, new Perceptron(26, 2, 1, alpha, epochs));
        languagePerceptrons.put(2.0, new Perceptron(26, 2, 1, alpha, epochs));

        MultiClassPerceptron mcPerceptron = new MultiClassPerceptron(languagePerceptrons, alpha);

        for (int epoch = 0; epoch < epochs; epoch++) {
             loss = mcPerceptron.trainLayer(Arrays.asList(trainInputs), trainLabels);
             accuracy = mcPerceptron.evaluate(Arrays.asList(trainInputs), trainLabels);
            System.out.printf("Epoch %d: Loss = %.4f, Accuracy = %.2f%%%n", epoch + 1, loss, accuracy);
        }


        System.out.println("Learning is end!");
        PerceptronUtils.demoLanguageClassificationFromFile(mcPerceptron, labelMap, sampleTexts, alpha, epochs);
    }
}