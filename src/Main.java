import java.util.*;

public class Main {
    public static void main(String[] args) {
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
            System.out.println("4 - Predict new Observation (Perceptron)");
            System.out.println("5 - Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    KNNRunner.predictNewObservation(trainSet, modelStats, sc);
                }
                case 2 -> {
                    KNNRunner.printStats(modelStats);
                }
                case 3 -> {
                    perceptron = PerceptronRunner.runPerceptron(trainSet, testSet, sc);
                }
                case 4 -> {
                    if (perceptron == null) {
                        System.out.println("Firstly create a perceptron (выберите пункт 3).");
                    } else {
                        PerceptronRunner.predictNewObservation(perceptron, sc);
                    }
                }
                case 5 -> {
                    System.out.println("Exiting..");
                    running = false;
                }


                default -> {System.out.println("Unknown case");}
            }
        }
        sc.close();
    }
}