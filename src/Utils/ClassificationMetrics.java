package Utils;

import java.util.*;
import java.util.stream.IntStream;

public class ClassificationMetrics {
    public static double accuracy(List<String> real, List<String> pred) {

        long correct = IntStream.range(0, real.size())
                .filter(i -> pred.get(i) != null && real.get(i).equals(pred.get(i)))
                .count();
        return (double) correct / real.size();
    }

    public static double precision(List<String> real, List<String> pred, String cls) {
        long tp = IntStream.range(0, pred.size())
                .filter(i -> cls.equals(pred.get(i)) && cls.equals(real.get(i)))
                .count();
        long fp = IntStream.range(0, pred.size())
                .filter(i -> cls.equals(pred.get(i)) && !cls.equals(real.get(i)))
                .count();
        return tp + fp == 0 ? 0 : (double) tp / (tp + fp);
    }

    public static double recall(List<String> real, List<String> pred, String cls) {
        long tp = IntStream.range(0, real.size())
                .filter(i -> cls.equals(real.get(i)) && cls.equals(pred.get(i)))
                .count();
        long fn = IntStream.range(0, real.size())
                .filter(i -> cls.equals(real.get(i)) && !cls.equals(pred.get(i)))
                .count();
        return tp + fn == 0 ? 0 : (double) tp / (tp + fn);
    }

    public static double f1(double p, double r) {
        return (p + r) == 0 ? 0 : 2 * p * r / (p + r);
    }

    public static void printAll(List<String> real, List<String> pred) {
        Set<String> classes = new HashSet<>(real);
        System.out.printf("Accuracy: %.2f%%%n", accuracy(real, pred)*100);
        for (String cls : classes) {
            double p = precision(real, pred, cls);
            double r = recall(real, pred, cls);
            System.out.printf("Class %s: P=%.2f%%, R=%.2f%%, F1=%.2f%%%n",
                    cls, p*100, r*100, f1(p,r)*100);
        }
    }
}