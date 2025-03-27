package classifier;

import dataSet.TextProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;

public class PerceptronUtils {

    public static void predictNewObservation(Perceptron perceptron, Scanner sc) {
        System.out.print("Sepal Length: ");
        double sepalLen = sc.nextDouble();
        System.out.print("Sepal Width : ");
        double sepalWid = sc.nextDouble();

        //(0,1) = (sepal_width, sepal_length)
        double[] newObs = {sepalWid, sepalLen};

        int predictedLabel = perceptron.predictP(newObs);
        String predClass = (predictedLabel == 1) ? "Iris-setosa" : "Iris-versicolor";
        System.out.println("Predicted Iris Class (classifier.Perceptron): " + predClass);
    }

    public static String predictLanguageFromText(MultiClassPerceptron mcPerceptron, String text, Map<Double, String> labelMap) {
        double[] vector = TextProcessor.textToVector(text);
        double predictedLabel = mcPerceptron.predict(vector);
        String predictedLanguage = labelMap.getOrDefault(predictedLabel, "Unknown language");
        return predictedLanguage;
    }

    public static void demoLanguageClassificationFromFile(MultiClassPerceptron mcPerceptron, Map<Double, String> labelMap, Map<String, String> sampleTexts, double alpha, int epochs) {
        System.out.println("=================================================================");
        System.out.println("| Language | Predicted         | Alpha      | Epochs | Accuracy |");
        System.out.println("=================================================================");

        for (Map.Entry<String, String> entry : sampleTexts.entrySet()) {
            String langLabel = entry.getKey();
            String text = entry.getValue();
            String predictedLanguage = predictLanguageFromText(mcPerceptron, text, labelMap);

            double[] vector = TextProcessor.textToVector(text);
            double trueLabel = switch (langLabel) {
                case "en" -> 0.0;
                case "fr" -> 1.0;
                case "pl" -> 2.0;
                default -> -1.0;
            };
            double predicted = mcPerceptron.predict(vector);
            double accuracy = predicted == trueLabel ? 100.0 : 0.0;
            System.out.print(formatLanguagePrediction(langLabel, predictedLanguage, alpha, epochs, accuracy));
        }
        System.out.println("=================================================================");
    }

    public static Map<String, String> loadSampleTextsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Map<String, String> result = new java.util.HashMap<>();
            String line;
            String currentLabel = null;
            StringBuilder currentText = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.endsWith(":") && line.length() == 3) {
                    if (currentLabel != null && !currentText.isEmpty()) {
                        result.put(currentLabel, currentText.toString().trim());
                    }
                    currentLabel = line.substring(0, line.length() - 1);
                    currentText = new StringBuilder();
                } else if (!line.isEmpty()) {
                    currentText.append(line).append(" ");
                }
            }

            if (currentLabel != null && !currentText.isEmpty()) {
                result.put(currentLabel, currentText.toString().trim());
            }

            if (result.isEmpty()) {
                System.out.println("MISTAKE THERE ARE NO FILE'S: " + filePath);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of();
        }
    }
    public static String formatLanguagePrediction(String testLabel, String language, double confidence, int epochs, double accuracy) {
        return String.format("| %-10s | %-15s | %10.2f%% | %5d | %8.2f%% |\n", testLabel, language, confidence, epochs, accuracy);
    }
}
