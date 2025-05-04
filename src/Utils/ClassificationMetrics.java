package Utils;

import NativeBayes.DoubleObservation;

import java.util.*;
import java.util.stream.IntStream;
import java.util.Objects;

public class ClassificationMetrics {
    public static <T> double accuracy(List<T> real, List<T> pred) {
        long correct = IntStream.range(0, real.size())
                .filter(i -> Objects.equals(real.get(i), pred.get(i)))
                .count();
        return (double) correct / real.size();
    }

    public static <T> double precision(List<T> real, List<T> pred, T cls) {
        long tp = IntStream.range(0, pred.size())
                .filter(i -> Objects.equals(pred.get(i), cls) && Objects.equals(real.get(i), cls))
                .count();
        long fp = IntStream.range(0, pred.size())
                .filter(i -> Objects.equals(pred.get(i), cls) && !Objects.equals(real.get(i), cls))
                .count();
        return tp + fp == 0 ? 0 : (double) tp / (tp + fp);
    }

    public static <T> double recall(List<T> real, List<T> pred, T cls) {
        long tp = IntStream.range(0, real.size())
                .filter(i -> Objects.equals(real.get(i), cls) && Objects.equals(pred.get(i), cls))
                .count();
        long fn = IntStream.range(0, real.size())
                .filter(i -> Objects.equals(real.get(i), cls) && !Objects.equals(pred.get(i), cls))
                .count();
        return tp + fn == 0 ? 0 : (double) tp / (tp + fn);
    }

    public static double f1(double p, double r) {
        return (p + r) == 0 ? 0 : 2 * p * r / (p + r);
    }

    public static double fMeasure(double precision, double recall) {
        return f1(precision, recall);
    }

    public static void printAll(List<String> real, List<String> pred) {
        Set<String> classes = new HashSet<>(real);
        System.out.printf("Accuracy: %.2f%%%n", accuracy(real, pred) * 100);
        for (String cls : classes) {
            double p = precision(real, pred, cls);
            double r = recall(real, pred, cls);
            System.out.printf("Class %s: P=%.2f%%, R=%.2f%%, F1=%.2f%%%n",
                    cls, p * 100, r * 100, f1(p, r) * 100);
        }
    }

    public static double wcss(List<DoubleObservation> data, List<Integer> labels, List<double[]> centroids) {
        double sum = 0;
        for (int i = 0; i < data.size(); i++) {
            double[] feat = data.get(i).getNumericFeatures();
            double[] c = centroids.get(labels.get(i));
            for (int j = 0; j < feat.length; j++) {
                double d = feat[j] - c[j];
                sum += d*d;
            }
        }
        return sum;
    }
}