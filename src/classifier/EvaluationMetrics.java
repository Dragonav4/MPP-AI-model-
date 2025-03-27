package classifier;

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

    public static double precision(double[] realLabels, int[] predictedLabels, double positiveClass) {
        int truePositive = 0;
        int falsePositive = 0;
        for (int i = 0; i < predictedLabels.length; i++){
            if(predictedLabels[i] == positiveClass){
                if(realLabels[i] == positiveClass){
                    truePositive++;
                } else {
                    falsePositive++;
                }
            }
        }
        return (truePositive + falsePositive) == 0 ? 0.0 : (double) truePositive / (truePositive + falsePositive);
    }

    public static double recall(double[] realLabels, int[] predictedLabels, double positiveClass) {
        int truePositive = 0;
        int falseNegative = 0;
        for (int i = 0; i < realLabels.length; i++){
            if(realLabels[i] == positiveClass){
                if(predictedLabels[i] == positiveClass){
                    truePositive++;
                } else {
                    falseNegative++;
                }
            }
        }
        return (truePositive + falseNegative) == 0 ? 0.0 : (double) truePositive / (truePositive + falseNegative);
    }

    public static double fmeasure(double[] realLabels, int[] predictedLabels, double positiveClass) {
        double prec = precision(realLabels, predictedLabels, positiveClass);
        double rec = recall(realLabels, predictedLabels, positiveClass);
        return (prec + rec) == 0 ? 0.0 : 2 * prec * rec / (prec + rec);
    }

}
