package knn;

import NativeBayes.DoubleObservation;
import Utils.ClassificationMetrics;

import java.util.*;

public class KNNPipeline {
    public static Map<Integer, Double> runIrisKnn(
            List<DoubleObservation> trainSet,
            List<DoubleObservation> testSet
    ) {
        Map<Integer, Double> modelStats = new LinkedHashMap<>();
        int[] kValues = {1, 3, 5, 7, 9};

        for (int k : kValues) {
            KNearestNeighbours knn = new KNearestNeighbours(k, trainSet);

            List<Double> realClasses = new ArrayList<>();
            List<Double> predictedClasses = new ArrayList<>();

            for (var testObs : testSet) {
                String prediction = knn.predict(testObs);
                realClasses.add(encodeClass(testObs.getLabel().toString()));
                predictedClasses.add(encodeClass(prediction));
            }

            double accuracy = ClassificationMetrics.accuracy(realClasses, predictedClasses);
            modelStats.put(k, accuracy);
        }
        return modelStats;
    }

    public static void predictIrisObservation(
            List<DoubleObservation> trainSet,
            Map<Integer, Double> modelStats,
            int k
    ) {
        DoubleObservation newObs = getSampleIrisData();

        if (!modelStats.containsKey(k)) {
            System.out.println("Invalid k value. Please choose from: " + modelStats.keySet());
        } else {
            KNearestNeighbours knnUser = new KNearestNeighbours(k, trainSet);
            String predictedClass = knnUser.predict(newObs);
            System.out.println("Predicted Iris Class (KNN): " + predictedClass);
        }
    }

    private static DoubleObservation getSampleIrisData() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Sepal Length: ");
        double sepalLen = sc.nextDouble();
        System.out.print("Sepal Width : ");
        double sepalWid = sc.nextDouble();
        System.out.print("Petal Length: ");
        double petalLen = sc.nextDouble();
        System.out.print("Petal Width : ");
        double petalWid = sc.nextDouble();
        return new IrisData(sepalLen, sepalWid, petalLen, petalWid, "Unknown");
    }

    private static double encodeClass(String dataClass) {
        if (dataClass == null) return -1.0;
        switch (dataClass.toLowerCase()) {
            case "setosa": return 0.0;
            case "versicolor": return 1.0;
            case "virginica": return 2.0;
            default: return -1.0;
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
