package NativeBayes;
import java.util.Scanner;

import Utils.*;
import Utils.Quantizer.MinMaxFinder;
import Utils.Quantizer.Quantizer;
import knn.IrisData;

import java.util.*;

public class PipelineDemo {

    public static void outGameObservation() throws Exception {
        DataLoader<StringObservation> loader = new GenericCsvLoader<>(
                "/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/outgame",
                (line, lineNumber) -> {
                    if (line == null || line.isBlank() || lineNumber == 1) return null;

                    String[] parts = line.split(",");
                    if (parts.length < 5) return null;

                    String label = parts[4].trim();
                    if (label.isEmpty()) return null;

                    Map<String, String> attrs = new HashMap<>();
                    attrs.put("outlook", parts[0].trim());
                    attrs.put("temperature", parts[1].trim());
                    attrs.put("humidity", parts[2].trim());
                    attrs.put("windy", parts[3].trim());

                    return new StringObservation(attrs, label);
                }
        );

        List<StringObservation> allNumeric = loader.load();

        List<StringObservation> allStringObservations = new ArrayList<>(allNumeric); //copy of allnumeric just to be sure that its changeable(by size)

        Classifier<StringObservation> bayes = new NativeBayesAdapter(false);
        RatioSplitter<StringObservation> splitter = new RatioSplitter<>(0.2);
        DataLoader<StringObservation> staticLoader = () -> allStringObservations;
        //Experiment<StringObservation> exp = new Experiment<>(allStringObservations, bayes, splitter); Experiment implements DataLoader -> we can use any DataType, but not allStringObservations
        Experiment<StringObservation> exp = new Experiment<>(staticLoader, bayes, splitter);
        exp.run();
    }
    public static void irisDataObservation() throws Exception {
        DataLoader<DoubleObservation> loader = new GenericCsvLoader<>(
                "/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/iris.csv",
                (line, _) -> IrisData.getSampleData(line));

        var allNumeric = loader.load();

        MinMaxFinder finder = new MinMaxFinder();
        finder.fit(allNumeric);

        Quantizer quantizer = new Quantizer(finder.getMin(), finder.getMax(), 6);

        List<StringObservation> allStringObservations = new ArrayList<>();
        for (var d : allNumeric) {
            double[] features = d.getNumericFeatures();
            String[] categories = quantizer.quantize(features);

            Map<String, String> attrs = new HashMap<>();
            for (int i = 0; i < categories.length; i++)
                attrs.put("feature_" + i, categories[i]);

            allStringObservations.add(new StringObservation(attrs, d.getLabel()));
        }

        Classifier<StringObservation> bayes = new NativeBayesAdapter(false);
        bayes.train(allStringObservations);

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
        String prediction = bayes.predict(new StringObservation(inputAttrs, ""));
        System.out.println("Predicted class: " + prediction);
    }

    public static void main(String[] args) throws Exception {
        outGameObservation();
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
}