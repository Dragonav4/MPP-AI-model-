import java.util.*;

public class Main {
    public static void main(String[] args) {
        String csvFilePath = "src/data/iris.csv";
        List<IrisData> dataset = PrepareDataset.readDataSet(csvFilePath);

        List<IrisData> trainSet = new ArrayList<>();
        List<IrisData> testSet  = new ArrayList<>();
        PrepareDataset.trainTestSplit(dataset, trainSet, testSet, 0.66);

        System.out.println("Train set size: " + trainSet.size());
        System.out.println("Test set size : " + testSet.size());

        Map<Integer, Double> modelStats = new LinkedHashMap<>();
        int[] kValues = {1, 3, 5, 7, 9};

        for (int k : kValues) {
            KNearestNeighbours knn = new KNearestNeighbours(k, trainSet);

            List<String> realClasses = new ArrayList<>();
            List<String> predictedClasses = new ArrayList<>();

            for (IrisData testObs : testSet) {
                String prediction = knn.predict(testObs);
                realClasses.add(testObs.getIrisClass());
                predictedClasses.add(prediction);
            }

            double accuracy = EvaluationMetrics.measureAccuracy(realClasses, predictedClasses);
            modelStats.put(k, accuracy);
            //System.out.println("k = " + k + " => Accuracy: " + (accuracy * 100.0) + " %");
        }

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1 - Enter a new observation for prediction");
            System.out.println("2 - View model statistics");
            System.out.println("3 - Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    // Input new observation
                    sc.useLocale(Locale.US);
                    System.out.print("Sepal Length: ");
                    float sepalLen = sc.nextFloat();
                    System.out.print("Sepal Width : ");
                    float sepalWid = sc.nextFloat();
                    System.out.print("Petal Length: ");
                    float petalLen = sc.nextFloat();
                    System.out.print("Petal Width : ");
                    float petalWid = sc.nextFloat();

                    IrisData newObs = new IrisData(sepalLen, sepalWid, petalLen, petalWid, "Unknown");

                    System.out.print("Choose k for KNN (e.g. 3): ");
                    int userK = sc.nextInt();

                    // Predict
                    if (!modelStats.containsKey(userK)) {
                        System.out.println("Invalid k value. Please choose from: " + modelStats.keySet());
                    } else {
                        KNearestNeighbours knnUser = new KNearestNeighbours(userK, trainSet);
                        String predictedClass = knnUser.predict(newObs);
                        System.out.println("Predicted Iris Class: " + predictedClass);
                    }
                    break;

                case 2:
                    // Display model statistics
                    System.out.println("\nModel Statistics:");
                    for (Map.Entry<Integer, Double> entry : modelStats.entrySet()) {
                        System.out.println("k = " + entry.getKey() + " => Accuracy: " + (entry.getValue() * 100.0) + " %");
                    }
                    break;

                case 3:
                    System.out.println("Exiting...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        sc.close();
    }
}
