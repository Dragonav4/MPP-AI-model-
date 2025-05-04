package classifier;

import NativeBayes.DoubleObservation;
import PerceptronLayer.PerceptronL;
import Plots.AccuracyPlot;
import Plots.DecisionBoundaryPlot;
import Utils.ClassificationMetrics;
import dataSet.Dataset;

import java.util.*;
import java.util.function.Function;

public class PerceptronRunner {
    public static PerceptronL runPerceptron(
            List<DoubleObservation> trainSet,
            List<DoubleObservation> testSet,
            Function<DoubleObservation, Double> mapper,
            Scanner sc)
    {
        List<double[]> trainFeatures = Dataset.toFeatures(trainSet, 0, 1); // sepal_width and // sepal_length
        List<double[]> testFeatures = Dataset.toFeatures(testSet, 0, 1); // remake from List<dataClass.IrisData> -> double[][]

        List<Double> trainLabels = Dataset.toLabels(trainSet, mapper);
        List<Double> testLabels = Dataset.toLabels(testSet, mapper);

        System.out.print("Enter alpha (learning rate): ");
        double userAlpha = sc.nextDouble();
        PerceptronL perceptronl = new PerceptronL(2, 1, userAlpha);

        perceptronl.train(trainFeatures, trainLabels);
        List<Double> accPerEpoch = perceptronl.getAccuracyPerEpoch();
        AccuracyPlot.showAccuracyChart(accPerEpoch);
        DecisionBoundaryPlot.showDecisionBoundary(testFeatures, testLabels, perceptronl);

        //going through all examples and save our predictions(0 or 1)
        List<Double> perceptronPredicted = new ArrayList<>();
        for (var testFeature : testFeatures) {
            perceptronPredicted.add((double) perceptronl.predict(testFeature));
        }

        double accuracy = ClassificationMetrics.accuracy(testLabels, perceptronPredicted);
        double precision = ClassificationMetrics.precision(testLabels, perceptronPredicted,1.0);
        double recall = ClassificationMetrics.recall(testLabels, perceptronPredicted,1.0);
        double fmeasure = ClassificationMetrics.fMeasure(precision,recall);
        System.out.printf("classifier.Perceptron: accuracy on test set = %.2f%%\n", accuracy * 100.0);
        System.out.printf("classifier.Perceptron: fmeasure: %.2f%%\n", fmeasure * 100.0);

        return perceptronl;
    }
}