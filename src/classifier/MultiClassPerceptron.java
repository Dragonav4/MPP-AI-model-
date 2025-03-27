package classifier;


import java.util.*;

public class MultiClassPerceptron {
    private final Map<Double, Perceptron> perceptronMap;
    private final double alpha;

    public MultiClassPerceptron(Map<Double, Perceptron> perceptronMap, double alpha) {
        this.perceptronMap = perceptronMap;
        this.alpha = alpha;
    }

    public double predict(double[] input) {
        double bestOutput = Double.NEGATIVE_INFINITY;
        double bestLabel = -1;

        for (Map.Entry<Double, Perceptron> entry : perceptronMap.entrySet()) {
            double output = entry.getValue().rawOutput(input);
            //System.out.printf("[PREDICT] Perceptron %.0f: rawOutput = %.4f\n", entry.getKey(), output);
            if (output > bestOutput) {
                bestOutput = output;
                bestLabel = entry.getKey();
            }
        }
        return bestLabel;
    }
    public double trainLayer(List<double[]> inputs, double[] labels) {
        double totalLoss = 0;
        for (int i = 0; i < inputs.size(); i++) {
            double[] input = inputs.get(i);
            double trueLabel = labels[i];

            for (Map.Entry<Double, Perceptron> entry : perceptronMap.entrySet()) {
                double expected = (entry.getKey() == trueLabel) ? 1.0 : 0.0;
                entry.getValue().train(new double[][]{input}, new double[]{expected}, alpha);
                double rawOutput = entry.getValue().rawOutput(input);
                double sigmoidOutput = 1.0 / (1.0 + Math.exp(-rawOutput));
                totalLoss += Math.pow(expected - sigmoidOutput, 2);
            }
        }
        return totalLoss;
    }

    public double evaluate(List<double[]> inputs, double[] labels) {
        int correct = 0;
        for (int i = 0; i < inputs.size(); i++) {
            double predicted = predict(inputs.get(i));
            if (predicted == labels[i]) {
                correct++;
            }
        }
        return 100.0 * correct / inputs.size();
    }

}