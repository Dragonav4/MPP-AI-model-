package PerceptronLayer;
import java.util.*;

public class PLayerUtils {

    private static final Map<String, String> FOLDER_NAME_TO_LANG_CODE = Map.of(
            "english", "en",
            "french", "fr",
            "polish", "pl"
    );

    private static final Map<Double, String> LANGUAGE_DECODER = Map.of(
            0.0, "English",
            1.0, "French",
            2.0, "Polish"
    );

    public static void demoLanguageClassificationFromFile(
            MultiClassPerceptron mcp,
            List<double[]> testInputs,
            List<Double> testLabels) {
        List<Double> predictedLabels = new ArrayList<>();
        for (double[] testInput : testInputs) {
            predictedLabels.add(mcp.predict(testInput));

        }

        System.out.println("                    Each perceptron metrics:");
        System.out.println("=================================================================");
        System.out.println("| Language | Precision | Recall | F1Measure |");
        calculatePerceptronsMetrics(mcp, predictedLabels, testLabels);
        System.out.println("=================================================================");
        System.out.println("                        Overall metrics:");

        double accuracy = PerceptronLayerEvaluationMetrics.measureAccuracy(predictedLabels, testLabels);
        System.out.printf("Final accuracy: %.2f%%%n", accuracy * 100);

        System.out.println("=================================================================");
    }

    private static void calculatePerceptronsMetrics(
            MultiClassPerceptron mcp,
            List<Double> predictedLabels,
            List<Double> testLabels) {
        for (double classCoded = 0; classCoded < mcp.getPerceptronMap().size(); classCoded++) {
            String language = LANGUAGE_DECODER.getOrDefault(classCoded, "Unknown");
            double precision = PerceptronLayerEvaluationMetrics.precision(testLabels, predictedLabels, classCoded);
            double recall = PerceptronLayerEvaluationMetrics.recall(testLabels, predictedLabels, classCoded);
            double fmeasure = PerceptronLayerEvaluationMetrics.fMeasure(precision, recall);

            String formattedRow = String.format("| %-8s | %8.2f%% | %6.2f%% | %7.2f%% |",
                    language, precision * 100, recall * 100, fmeasure * 100);
            System.out.println(formattedRow);
        }
    }


    public static void predictNewObservation(PerceptronL perceptron, Scanner sc) {
        System.out.print("Sepal Length: ");
        double sepalLen = sc.nextDouble();
        System.out.print("Sepal Width : ");
        double sepalWid = sc.nextDouble();

        //(0,1) = (sepal_width, sepal_length)
        double[] newObs = {sepalLen, sepalWid};

        double predictedLabel = perceptron.predict(newObs);
        String predClass = (predictedLabel == 1) ? "Iris-setosa" : "Iris-versicolor";
        //String predClass = (predictedLabel == 1) ? "fr" : "en";
        System.out.println("Predicted Iris Class (classifier.Perceptron): " + predClass);
    }
}