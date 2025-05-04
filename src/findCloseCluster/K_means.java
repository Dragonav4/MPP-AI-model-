package findCloseCluster;

import NativeBayes.DoubleObservation;

import java.util.*;

public class K_means {
    private final int k;
    private final List<double[]> centroids = new ArrayList<>();

    public K_means(int k) {
        if (k <= 0) throw new IllegalArgumentException("K should be grater than 0");
        this.k = k;
    }

    public static double euclideanDistance(double[] vec1, double[] vec2) {
        double sum = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            double diff = vec1[i] - vec2[i];
            sum += diff * diff;
        }
        return sum;
    }

    private int findClosestVector(double[] vector) {
        if (centroids.isEmpty()) {
            throw new IllegalStateException("Centroids are not initialized");
        }
        int bestIdx = 0;
        double bestDist = euclideanDistance(vector, centroids.get(0));
        for (int i = 0; i < centroids.size(); i++) {
            double distance = euclideanDistance(vector, centroids.get(i));
            if (distance < bestDist) {
                bestDist = distance;
                bestIdx = i;
            }
        }
        return bestIdx;
    }


    private double[] computeCentroid(List<DoubleObservation> cluster) {
        //GetFirst just to see dim aka [0.0 ,1.0 ,0.0 ,2.0] - 4
        double[] dimension = cluster.get(0).getNumericFeatures();
        double[] c = new double[dimension.length];
        for (DoubleObservation observation : cluster) {
            //Adding each element and normalize by dividing on amount of objects
            double[] feat = observation.getNumericFeatures();
            for (int i = 0; i < dimension.length; i++) {
                c[i] += feat[i];
            }
        }
        for (int j = 0; j < dimension.length; j++) {
            c[j] /= cluster.size();
        }
        return c;
    }

    public void fit(List<DoubleObservation> data) {
        int n = data.size();
        if (n < k) throw new IllegalArgumentException("N should be grater than number of k's");
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i); // list of indices of all dots from 0 to n-1

        Collections.shuffle(indices, new Random());

        List<List<DoubleObservation>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) clusters.add(new ArrayList<>());

        //round-robin splitting points into clusters
        for (int i = 0; i < n; i++) clusters.get(i % k).add(data.get(indices.get(i)));
        centroids.clear();
        for (List<DoubleObservation> cluster : clusters) centroids.add(computeCentroid(cluster));

        boolean changed;
        do {
            changed=false;
            clusters = new ArrayList<>();
            for (int i = 0; i < k; i++) clusters.add(new ArrayList<>());

            //redistribute all dots to nearest centroid
            for (DoubleObservation doubleObservation : data) {
                int closest = findClosestVector(doubleObservation.getNumericFeatures());
                clusters.get(closest).add(doubleObservation);
            }
            //recompute centroids and check on changing
            for (int i = 0; i < k; i++) {
                double[] newCentroid = computeCentroid(clusters.get(i));
                if (!Arrays.equals(newCentroid, centroids.get(i))) {
                    changed = true;
                    centroids.set(i, newCentroid);
                }
            }
        } while (changed);
    }

    public List<double[]> getCentroids() {
        return centroids;
    }

    public List<Integer> predictAll(List<DoubleObservation> data) {
        List<Integer> labels = new ArrayList<>(data.size());
        for (DoubleObservation obs : data) {
            labels.add(findClosestVector(obs.getNumericFeatures()));
        }
        return labels;
    }
    int predict(double[] observation) {
        return findClosestVector(observation);
    }
}
