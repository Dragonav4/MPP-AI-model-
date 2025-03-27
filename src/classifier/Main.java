package classifier;

import knn.KNNRunner;
import dataSet.PrepareDataset;
import knn.IrisData;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/src/data/language_dataSet";

        Map<String, String> sampleTexts = PerceptronUtils.loadSampleTextsFromFile(filePath);
        Map<Double, String> labelMap = Map.of(
                0.0, "English",
                1.0, "French",
                2.0, "Polish"
        );
        String csvFilePath = "src/data/iris.csv";
        List<IrisData> dataset = PrepareDataset.readDataSet(csvFilePath);

        List<IrisData> trainSet = new ArrayList<>();
        List<IrisData> testSet  = new ArrayList<>();
        PrepareDataset.trainTestSplit(dataset, trainSet, testSet, 0.66);


        Map<Integer, Double> modelStats = KNNRunner.runKNN(trainSet, testSet);

        Scanner sc = new Scanner(System.in);
        Perceptron perceptron = null; // save perceptron
        boolean running = true;

        while (running) {
            System.out.println("\nChoose option:");
            System.out.println("1 - Predict new Observation (KNN)");
            System.out.println("2 - Statistics KNN");
            System.out.println("3 - Train perceptron");
            System.out.println("4 - Predict new Observation (classifier.Perceptron)");
            System.out.println("5 - Predict language");
            System.out.println("6 - Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> KNNRunner.predictNewObservation(trainSet, modelStats, sc);
                case 2 -> KNNRunner.printStats(modelStats);
                case 3 -> perceptron = PerceptronRunner.runPerceptron(trainSet, testSet, sc);
                case 4 -> {
                    if (perceptron == null) {
                        System.out.println("Firstly create a perceptron (выберите пункт 3).");
                    } else {
                        PerceptronUtils.predictNewObservation(perceptron, sc);
                    }
                }
                case 6 -> {
                    System.out.println("Exiting..");
                    running = false;
                }
                case 5 -> PerceptronRunner.runLanguagePerceptronTraining(sc, labelMap, sampleTexts);
                default -> {System.out.println("Unknown case");}
            }
        }
        sc.close();
    }
}