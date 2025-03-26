import java.util.List;

public class EvaluationMetrics {
    public static double measureAccuracy(List<String> realClasses, List<String>predictedClasses) {
        if (realClasses.size() != predictedClasses.size()) {
            throw new IllegalArgumentException("Lists must be of same size. Incorrect result :(");
        }
        int amountOfCorrect= 0;

        for (int i = 0; i < realClasses.size(); i++) {
            if(realClasses.get(i).equals(predictedClasses.get(i))) {
                amountOfCorrect++;
            }
        }
        return (double) amountOfCorrect / realClasses.size();
    }

    public static double measurePerceptronAccuracy(double[] realLabels, int[] predictedLabels) {
        if (realLabels.length != predictedLabels.length) {
            throw new IllegalArgumentException("Arrays must be of the same length.");
        }
        int correct = 0;
        for (int i = 0; i < realLabels.length; i++) {
            if ((int) realLabels[i] == predictedLabels[i]) {
                correct++;
            }
        }
        return (double) correct / realLabels.length;
    }

    public static double precision(double[] realClasses, int[] predictedClasses, double positiveClass) {
        int truePositive = 0;
        int falsePositive = 0;
        for (int i = 0; i < predictedClasses.length; i++) {
            if (predictedClasses[i] == positiveClass) {
                if (realClasses[i] == positiveClass) {
                    truePositive++;
                } else {
                    falsePositive++;
                }
            }
        }
        return (truePositive + falsePositive) == 0 ? 0.0 : (double) truePositive / (truePositive + falsePositive);
    }

    public static double recall(double[] realClasses, int[] predictedClasses, double positiveClass) {
        int truePositive = 0;
        int falseNegative = 0;
        for (int i = 0; i < realClasses.length; i++) {
            if (realClasses[i] ==positiveClass) {
                if (predictedClasses[i] == positiveClass) {
                    truePositive++;
                } else {
                    falseNegative++;
                }
            }
        }
        return (truePositive + falseNegative) == 0 ? 0.0 : (double) truePositive / (truePositive + falseNegative);
    }

    public static double fmeasure(double[] realClasses, int[] predictedClasses, double positiveClass) {
        double precision = precision(realClasses, predictedClasses, positiveClass);
        double recall = recall(realClasses, predictedClasses, positiveClass);
        return (precision + recall) == 0 ? 0.0 : 2 * precision * recall / (precision + recall);
    }
}
