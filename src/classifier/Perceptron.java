package classifier;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Perceptron {
    private final int dimension; // amount of features
    private final double[] weights;
    private double threshold;
    private double alpha;
    private int epochs;

    public Perceptron(int inputSize, int dimension, double threshold, double alpha, int epochs) {
        this.weights = new double[inputSize];
        this.threshold = threshold;
        this.alpha = alpha;
        this.epochs = epochs;
        this.dimension = inputSize;
        Random random = new Random();
        for (int i = 0; i < inputSize; i++) {
            this.weights[i] = -1 + 2 * random.nextDouble();
        }
    }

    public double[] train(double[][] trainInputs, double[] labels, double alpha) { //delta-rule
        var accuracyPerEpoch = new ArrayList<Double>();
        long epoch = 0;
        long mistakes;

        do {
            mistakes =0;
            for (int i =0; i < trainInputs.length; i++) {
                int y = predictP(trainInputs[i]); //y ? 1 : 0
                int d = (int) labels[i]; // standard result
                if ((d - y) != 0) { // if not right res change weights
                    mistakes++;
                    for (int j = 0; j < dimension; j++) {
                        weights[j] += (d - y) * alpha * trainInputs[i][j];
                    }
                    threshold -= (d - y) * alpha;
                }
            }
            epoch++;
            double correlation = (double) (trainInputs.length-mistakes)/ trainInputs.length;
            accuracyPerEpoch.add(correlation);
            //System.out.printf("Correct predictions for current epoch %d are: %.2f%%%n\n", epoch,correlation * 100);
        }while (epoch < 100 && mistakes !=0);
        return accuracyPerEpoch.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public int predictP(double[] inputs) { //W^TX-O>=0
        double sum = 0.0;
        for (int i = 0; i < dimension; i++) {
            sum += weights[i] * inputs[i];
        }
        sum -= threshold;
        return (sum >= 0.0) ? 1 : 0;
    }
    public double rawOutput(double[] input) {
        double sum = 0;
        for (int i = 0; i < dimension; i++) {
            sum += weights[i] * input[i];
        }
        return sum - threshold;
    }
    public double[] getWeights() {
        return weights;
    }

    public double getThreshold() {
        return threshold;
    }
}
