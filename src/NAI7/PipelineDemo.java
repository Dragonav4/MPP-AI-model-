package NAI7;

import Utils.*;
import Utils.Quantizer.MinMaxFinder;
import Utils.Quantizer.Quantizer;
import knn.ISampleData;
import knn.IrisData;

import java.util.*;


public class PipelineDemo {

    public static void outGameObservation() throws Exception {
        DataLoader<Observation> loader = new GenericCsvLoader<>(
                "/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/outgame",
                line -> {
                    if (line == null || line.isBlank() || line.startsWith("outlook")) return null;

                    String[] parts = line.split(",");
                    if (parts.length < 5) return null;

                    String label = parts[4].trim();
                    if (label.isEmpty()) return null;

                    Map<String, String> attrs = new HashMap<>();
                    attrs.put("outlook", parts[0].trim());
                    attrs.put("temperature", parts[1].trim());
                    attrs.put("humidity", parts[2].trim());
                    attrs.put("windy", parts[3].trim());

                    return new Observation(attrs, label);
                }
        );

        List<Observation> allNumeric = loader.load();

        List<Observation> allObservations = new ArrayList<>(allNumeric);

        Classifier<Observation> bayes = new NativeBayesAdapter(false);
        RatioSplitter<Observation> splitter = new RatioSplitter<>(0.2);
        DataLoader<Observation> staticLoader = () -> allObservations;
        Experiment<Observation> exp = new Experiment<>(staticLoader, bayes, splitter);
        exp.run();
    }
    public static void irisDataObservation() throws Exception {
        DataLoader<ISampleData> loader = new GenericCsvLoader<>("/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/iris.csv", IrisData::getSampleData);

        List<ISampleData> allNumeric = loader.load();

        MinMaxFinder finder = new MinMaxFinder();
        finder.fit(allNumeric);

        Quantizer quantizer = new Quantizer(finder.getMin(), finder.getMax(), 6);

        List<Observation> allObservations = new ArrayList<>();
        for (ISampleData d : allNumeric) {
            double[] features = d.getFeatures();
            String[] categories = quantizer.quantize(features);

            Map<String, String> attrs = new HashMap<>();
            for (int i = 0; i < categories.length; i++)
                attrs.put("feature_" + i, categories[i]);

            allObservations.add(new Observation(attrs, d.getLabel()));
        }

        Classifier<Observation> bayes = new NativeBayesAdapter(false);
        bayes.train(allObservations);

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter sepal length: ");
        double sl = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Enter sepal width: ");
        double sw = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Enter petal length: ");
        double pl = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Enter petal width: ");
        double pw = Double.parseDouble(sc.nextLine().trim());

        double[] inputFeatures = new double[]{sl, sw, pl, pw};
        String[] inputCats = quantizer.quantize(inputFeatures);

        Map<String, String> inputAttrs = new HashMap<>();
        for (int i = 0; i < inputCats.length; i++) {
            inputAttrs.put("feature_" + i, inputCats[i]);
        }
        String prediction = bayes.predict(new Observation(inputAttrs, ""));
        System.out.println("Predicted class: " + prediction);
    }


    public static void main(String[] args) throws Exception {
    }
}