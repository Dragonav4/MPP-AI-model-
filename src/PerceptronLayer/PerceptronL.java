package PerceptronLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PerceptronL {
    private final int dimension;
    private final double[] weights;
    private double threshold;
    private final double alpha;
    //private final int maxEpochs;
    private List<Double> accuracyPerEpoch;

    public PerceptronL(int inputSize, double threshold, double alpha) {
        this.dimension = inputSize;
        this.weights = new double[inputSize];
        this.threshold = threshold;
        this.alpha = alpha;
        Random random = new Random();
        for (int i = 0; i < inputSize; i++) {
            this.weights[i] = -1 + 2 * random.nextDouble();
        }
    }

    private double computeWeightedSum(double[] input) {
        double sum = 0.0;
        for (int i = 0; i < dimension; i++) {
            sum += weights[i] * input[i];
        }
        return sum;
    }

    // net: w^T*x - threshold
    public double rawOutput(double[] input) {
        return computeWeightedSum(input) - threshold;
    }

    public int predict(double[] input) {
        return rawOutput(input) >= 0 ? 1 : 0;
    }

    //Update weights by delta rule
    private void updateWeights(double[] input, double error) {
        for (int j = 0; j < dimension; j++) {
            weights[j] += alpha * error * input[j];
        }
        threshold -= alpha * error;
    }

    //delta rule
    public void train(List<double[]> trainInputs, List<Double> labels) {
        List<Double> epochAccuracies = new ArrayList<>();
        int epoch=0;
        int mistakes;
        do {
             mistakes = 0;
            for (int i = 0; i < trainInputs.size(); i++) {
                double[] input = trainInputs.get(i);
                double desired = labels.get(i);
                int output = predict(input);
                double error = desired - output;
                if (error != 0) {
                    updateWeights(input, error);
                    mistakes++;
                }
            }
            epoch++;
            double correlation = (double) (trainInputs.size() - mistakes) / trainInputs.size();
            epochAccuracies.add(correlation);
        } while (epoch < 200 && mistakes != 0);
        this.accuracyPerEpoch = epochAccuracies;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getThreshold() {
        return threshold;
    }

    public List<Double> getAccuracyPerEpoch() {
        return accuracyPerEpoch;
    }
}