package knn;

import PerceptronLayer.PerceptronLayerEvaluationMetrics;

import java.util.*;


public abstract class KNNRunner {
    public Map<Integer, Double> runKNN(
            List<ISampleData> trainSet,
            List<ISampleData> testSet
    ) {
        Map<Integer, Double> modelStats = new LinkedHashMap<>();
        int[] kValues = {1, 3, 5, 7, 9};

        for (int k : kValues) {
            KNearestNeighbours knn = new KNearestNeighbours(k, trainSet);

            List<Double> realClasses = new ArrayList<>();
            List<Double> predictedClasses = new ArrayList<>();

            for (ISampleData testObs : testSet) {
                String prediction = knn.predict(testObs);
                realClasses.add(encodeClass(testObs.getItemClass()));
                predictedClasses.add(encodeClass(prediction));
            }

            double accuracy = PerceptronLayerEvaluationMetrics.measureAccuracy(realClasses, predictedClasses);

            modelStats.put(k, accuracy);
        }
        return modelStats;
    }

    public void predictNewObservation(
            List<ISampleData> trainSet,
            Map<Integer, Double> modelStats,
            int k
    ) {
        ISampleData newObs = getSampleData();

        if (!modelStats.containsKey(k)) {
            System.out.println("Invalid k value. Please choose from: " + modelStats.keySet());
        } else {
            KNearestNeighbours knnUser = new KNearestNeighbours(k, trainSet);
            String predictedClass = knnUser.predict(newObs);
            System.out.println("Predicted Iris Class (KNN): " + predictedClass);
        }
    }

    protected abstract ISampleData getSampleData();

    protected abstract double encodeClass(String itemClass);

    public static void printStats(Map<Integer, Double> modelStats) {
        System.out.println("\nModel Statistics (KNN):");
        for (Map.Entry<Integer, Double> entry : modelStats.entrySet()) {
            System.out.printf("k = %d => Accuracy: %.2f %%\n",
                    entry.getKey(), entry.getValue() * 100.0);

        }
    }
}

