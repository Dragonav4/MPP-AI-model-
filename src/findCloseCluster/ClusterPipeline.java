package findCloseCluster;

import NativeBayes.DoubleObservation;
import PerceptronLayer.MultiClassPerceptron;
import Plots.IrisClusterPlot;
import Utils.ClassificationMetrics;
import Utils.GenericCsvLoader;
import Utils.LanguageClusterUtils;
import dataSet.Dataset;
import dataSet.LanguageData;
import dataSet.TextProcessor;
import knn.IrisData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


public class ClusterPipeline {
    private static class ClusteringResult {
        final List<Integer> labels;
        final List<double[]> centroids;
        final double score;
        ClusteringResult(List<Integer> labels, List<double[]> centroids, double score) {
            this.labels = labels;
            this.centroids = centroids;
            this.score = score;
        }
    }

    private static ClusteringResult findBestClustering(List<DoubleObservation> data, int k, int runs) {
        double bestScore = Double.POSITIVE_INFINITY;
        List<Integer> bestLabels = null;
        List<double[]> bestCentroids = null;
        for (int run = 0; run < runs; run++) {
            K_means kmRun = new K_means(k);
            ClusterAdapter adapterRun = new ClusterAdapter(kmRun);
            adapterRun.train(data);
            List<Integer> labelsRun = kmRun.predictAll(data);
            List<double[]> centroidsRun = kmRun.getCentroids();
            double scoreRun = ClassificationMetrics.wcss(data, labelsRun, centroidsRun);
            if (scoreRun < bestScore) {
                bestScore = scoreRun;
                bestLabels = labelsRun;
                bestCentroids = centroidsRun;
            }
        }
        return new ClusteringResult(bestLabels, bestCentroids, bestScore);
    }

    public static void IrisCluster() throws Exception {
        GenericCsvLoader<DoubleObservation> loader = new GenericCsvLoader<>(
                "resources/iris.csv",
                (line, _) -> IrisData.getSampleData(line)
        );
        List<DoubleObservation> data = loader.load();
        ClusteringResult result = findBestClustering(data, 3, 50);
        System.out.printf("Best WCSS after %d runs = %.4f%n", 50, result.score);
        IrisClusterPlot.generatePlot(data, result.labels, result.centroids, "iris_clustered.csv", result.score);
    }

    public static void LanguageCluster() {
        List<DoubleObservation> all = Dataset.loadSampleTextsFromDirectory("resources/language/");
        List<DoubleObservation> train = new ArrayList<>(), test = new ArrayList<>();
        MultiClassPerceptron.trainTestSplit(all, train, test, 0.1);

        K_means km = new K_means(3);
        km.fit(train);

        Map<String, Double> lang2code = Map.of("english", 0.0, "french", 1.0, "polish", 2.0);

        //Correct traincodes([0.0,2.0,1,0]) this is the same as the original text.
        List<Double> trainCodes = Dataset.toLabels(train, obs -> lang2code.get(obs.getLabel()));

        //Map cluster to correspond language(from amount of texts in each cluster)
        Map<Integer, Double> cluster2code =
                LanguageClusterUtils.learnClusterMapping(km.predictAll(train), trainCodes);

        List<Double> testCodes = Dataset.toLabels(test, obs -> lang2code.get(obs.getLabel()));
        double purity = ClassificationMetrics.accuracy(
                km.predictAll(test).stream().map(cluster2code::get).toList(),
                testCodes);
        System.out.printf("Purity: %.2f%%%n", purity * 100);

        System.out.print("Enter text: ");
        String raw = new Scanner(System.in).nextLine();
        DoubleObservation sample = new LanguageData(TextProcessor.textToVector(raw), "");
        int cl = km.predictAll(List.of(sample)).get(0);

        Map<Double, String> code2lang = lang2code.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        System.out.println("Predicted language: " + code2lang.get(cluster2code.get(cl)));
    }
    public static void main(String[] args) throws Exception {
        //LanguageCluster();
        IrisCluster();
    }
}