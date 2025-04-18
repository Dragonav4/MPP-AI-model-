package classifier;

import PerceptronLayer.MultiClassPerceptron;
import PerceptronLayer.PLayerRunner;
import PerceptronLayer.PLayerUtils;
import PerceptronLayer.PerceptronL;
import knn.ISampleData;
import knn.IrisData;
import knn.IrisDataKnnRunner;
import knn.KNNRunner;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        String csvFilePath = "/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/iris.csv";
        List<ISampleData> dataset = MultiClassPerceptron.readDataSet(csvFilePath, IrisData::getSampleData);
        List<ISampleData> trainSet = new ArrayList<>();
        List<ISampleData> testSet  = new ArrayList<>();
        MultiClassPerceptron.trainTestSplit(dataset, trainSet, testSet, 0.66);
        KNNRunner runner = new IrisDataKnnRunner(); // todo change
        Map<Integer, Double> modelStats = runner.runKNN(trainSet, testSet);

        PerceptronL perceptronl = null; // save perceptron
        boolean running = true;

        while (running) {
            System.out.println("\nChoose option:");
            System.out.println("1 - Predict new Observation (KNN)");
            System.out.println("2 - Statistics KNN");
            System.out.println("3 - Train perceptron");
            System.out.println("4 - Predict new Observation (classifier.Perceptron)");
            System.out.println("5 - Predict language");
            System.out.println("6 - Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("Input count of nearest points (k)");
                    var k = scanner.nextInt();
                    runner.predictNewObservation(trainSet, modelStats, k);
                }
                case 2 -> KNNRunner.printStats(modelStats);
                case 3 -> perceptronl = PerceptronRunner.runPerceptron(trainSet, testSet, IrisData::getBinaryClass, scanner);
                case 4 -> {
                    if (perceptronl == null) {
                        System.out.println("Firstly create a perceptron (выберите пункт 3).");
                    } else {
                        PLayerUtils.predictNewObservation(perceptronl, scanner);
                    }
                }
                case 6 -> {
                    System.out.println("Exiting..");
                    running = false;
                }
                case 5 -> PLayerRunner.runLanguagePerceptronTraining(scanner);
                default -> {System.out.println("Unknown case");}
            }
        }
    }
}