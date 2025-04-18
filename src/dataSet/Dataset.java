package dataSet;

import knn.ISampleData;
import knn.LanguageData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Dataset {
    public static List<double[]> toFeatures(List<ISampleData> dataSet, int f1, int f2) {
        List<double[]> features = new ArrayList<>();
        for (ISampleData data : dataSet) {
            double[] pair = new double[2];
            pair[0] = data.getFeatureByIndex(f1); //sepal_width
            pair[1] = data.getFeatureByIndex(f2); //sepal_length
            features.add(pair);
        }
        return features;
    }

    public static List<Double> toLabels(List<ISampleData> dataSet, Function<ISampleData, Double> mapper) {
        List<Double> labels = new ArrayList<>();
        for(ISampleData data : dataSet) {
            labels.add(mapper.apply(data));
        }
        return labels;
    }

    public static List<ISampleData> loadSampleTextsFromDirectory(String folderPath) {
        List<ISampleData> result = new ArrayList<>();
        File mainFolder = new File(folderPath);

        if (!mainFolder.exists() || !mainFolder.isDirectory()) {
            System.out.println("The specified path is not a folder or doesn't exist: " + folderPath);
            return result;
        }

        File[] subFolders = mainFolder.listFiles(File::isDirectory);
        if (subFolders == null || subFolders.length == 0) {
            System.out.println("No subfolders found in: " + folderPath);
            return result;
        }

        for (File subFolder : subFolders) {
            String folderName = subFolder.getName().toLowerCase(); //

            File[] textFiles = subFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
            if (textFiles == null) {
                continue;
            }

            for (File txtFile : textFiles) {
                String content = readFileContent(txtFile);
                if (!content.isEmpty()) {
                    //samples.add(TextProcessor.textToVector(content));
                    result.add(new LanguageData(TextProcessor.textToVector(content), folderName));

                }
            }

        }

        if (result.isEmpty()) {
            System.out.println("No text data was loaded from folder: " + folderPath);
        }

        return result;
    }

    public static String readFileContent(File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(" ");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return sb.toString().trim();
    }
}
