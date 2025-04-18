package PerceptronLayer;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class PerceptronLayerEvaluationMetrics {
    public static double measureAccuracy(List<Double> realClasses, List<Double> predictedClasses) {
        if (realClasses.size() != predictedClasses.size()) {
            throw new IllegalArgumentException("Lists must be of same size. Incorrect result :(");
        }
        long correctCount = IntStream.range(0, realClasses.size())
                .filter(i -> Objects.equals(realClasses.get(i), predictedClasses.get(i)))
                .count();
        return (double) correctCount / realClasses.size();
    }

    //Precision how many of those positive predictions - true?
    public static double precision(List<Double> realLabels, List<Double> predictedLabels, double positiveClass) {
        long truePositive = IntStream.range(0, predictedLabels.size())
                .filter(i -> Objects.equals(predictedLabels.get(i), positiveClass) &&
                        Objects.equals(realLabels.get(i), positiveClass))
                .count();
        long falsePositive = IntStream.range(0, predictedLabels.size())
                .filter(i -> Objects.equals(predictedLabels.get(i), positiveClass) &&
                        !Objects.equals(realLabels.get(i), positiveClass))
                .count();
        return (truePositive + falsePositive) == 0 ? 0.0 :
                (double) truePositive / (truePositive + falsePositive);
    }

    //Recall how many of real positive we find?
    public static double recall(List<Double> realLabels, List<Double> predictedLabels, double positiveClass) {
        long truePositive = IntStream.range(0, realLabels.size())
                .filter(i -> Objects.equals(realLabels.get(i), positiveClass) &&
                        Objects.equals(predictedLabels.get(i), positiveClass))
                .count();
        long falseNegative = IntStream.range(0, realLabels.size())
                .filter(i -> Objects.equals(realLabels.get(i), positiveClass) &&
                        !Objects.equals(predictedLabels.get(i), positiveClass))
                .count();
        return (truePositive + falseNegative) == 0 ? 0.0 :
                (double) truePositive / (truePositive + falseNegative);
    }

    public static double fMeasure(double precision, double recall) {
        return (precision + recall) == 0 ? 0.0 : 2 * precision * recall / (precision + recall);
    }
}