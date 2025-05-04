package classifier;

import NativeBayes.DoubleObservation;
import PerceptronLayer.MultiClassPerceptron;
import PerceptronLayer.PLayerRunner;
import PerceptronLayer.PLayerUtils;
import PerceptronLayer.PerceptronL;
import knn.IrisData;
import knn.KNNPipeline;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        String csvFilePath = "/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/iris.csv";
        List<DoubleObservation> dataset = MultiClassPerceptron.readDataSet(csvFilePath, IrisData::getSampleData);
        List<DoubleObservation> trainSet = new ArrayList<>();
        List<DoubleObservation> testSet  = new ArrayList<>();
        MultiClassPerceptron.trainTestSplit(dataset, trainSet, testSet, 0.66);
        Map<Integer, Double> modelStats = KNNPipeline.runIrisKnn(trainSet, testSet);

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
                    KNNPipeline.predictIrisObservation(trainSet, modelStats, k);
                }
                case 2 -> KNNPipeline.printStats(modelStats);
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