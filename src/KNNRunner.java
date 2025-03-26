import java.util.*;
public class KNNRunner {
    public static Map<Integer, Double> runKNN(
            List<IrisData> trainSet,
            List<IrisData> testSet
    ) {
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
        }
        return modelStats;
    }

    public static void predictNewObservation(
            List<IrisData> trainSet,
            Map<Integer, Double> modelStats,
            Scanner sc
    ) {
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

        if (!modelStats.containsKey(userK)) {
            System.out.println("Invalid k value. Please choose from: " + modelStats.keySet());
        } else {
            KNearestNeighbours knnUser = new KNearestNeighbours(userK, trainSet);
            String predictedClass = knnUser.predict(newObs);
            System.out.println("Predicted Iris Class (KNN): " + predictedClass);
        }
    }

    public static void printStats(Map<Integer, Double> modelStats) {
        System.out.println("\nModel Statistics (KNN):");
        for (Map.Entry<Integer, Double> entry : modelStats.entrySet()) {
            System.out.printf("k = %d => Accuracy: %.2f %%\n",
                    entry.getKey(), entry.getValue() * 100.0);

        }
    }
}

