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
    public static void IrisCluster() throws Exception {
        GenericCsvLoader<DoubleObservation> loader = new GenericCsvLoader<>(
                "resources/iris.csv",
                (line, _) -> IrisData.getSampleData(line)
        );
        List<DoubleObservation> data = loader.load();

        K_means km = new K_means(3);
        ClusterAdapter adapter = new ClusterAdapter(km);
        adapter.train(data);

        List<Integer> labels = km.predictAll(data);
        List<double[]> centroids = km.getCentroids();

        double score = ClassificationMetrics.wcss(data, labels, centroids);
        System.out.printf("WCSS = %.4f%n", score);
        IrisClusterPlot.generatePlot(data, labels, centroids, "iris_clustered.csv",score);
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