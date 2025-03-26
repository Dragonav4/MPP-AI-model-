import java.util.List;
import java.util.Scanner;

public class PerceptronRunner {
    public static Perceptron runPerceptron(
            List<IrisData> trainSet,
            List<IrisData> testSet,
            Scanner sc
    ) {
        double[][] trainFeatures = PrepareDataset.toFeatures(trainSet, 1, 2); // sepal_width and // sepal_length
        double[][] testFeatures = PrepareDataset.toFeatures(testSet, 1, 2); // remake from List<IrisData> -> double[][]

        double[] trainLabels = PrepareDataset.toLabels(trainSet); // setosa -> 0 versicolor ->1
        double[] testLabels = PrepareDataset.toLabels(testSet);

        System.out.print("Enter alpha (learning rate): ");
        double userAlpha = sc.nextDouble();
        System.out.print("Enter the number of epochs: ");
        int userEpochs = sc.nextInt();

        Perceptron perceptron = new Perceptron(2, 1.0, userAlpha, userEpochs);

        double[] accPerEpoch =perceptron.train(trainFeatures, trainLabels, userAlpha);
        AccuracyPlot.showAccuracyChart(accPerEpoch);
        DecisionBoundaryPlot.showDecisionBoundary(testFeatures, testLabels, perceptron);

        //going through all examples and save our predictions(0 or 1)
        int[] perceptronPredicted = new int[testFeatures.length];
        for (int i = 0; i < testFeatures.length; i++) {
            perceptronPredicted[i] = perceptron.predictP(testFeatures[i]);
        }

        double accuracy = EvaluationMetrics.measurePerceptronAccuracy(testLabels, perceptronPredicted);
        double fmeasure = EvaluationMetrics.fmeasure(testLabels, perceptronPredicted, 0);
        System.out.printf("Perceptron: accuracy on test set = %.2f%%\n", accuracy * 100.0);
        System.out.printf("Perceptron: fmeasure: %.2f%%\n", fmeasure * 100.0);

        return perceptron;
    }

    public static void predictNewObservation(Perceptron perceptron, Scanner sc) {
        System.out.print("Sepal Length: ");
        double sepalLen = sc.nextDouble();
        System.out.print("Sepal Width : ");
        double sepalWid = sc.nextDouble();

        //(0,1) = (sepal_width, sepal_length)
        double[] newObs = {sepalWid, sepalLen};

        int predictedLabel = perceptron.predictP(newObs);
        String predClass = (predictedLabel == 1) ? "Iris-setosa" : "Iris-versicolor";
        System.out.println("Predicted Iris Class (Perceptron): " + predClass);
    }
}

