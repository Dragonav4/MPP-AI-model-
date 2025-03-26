import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PrepareDataset {
    public static List<IrisData> readDataSet(String path) {
        List<IrisData> irisDataSet = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bf.readLine()) != null) {
                if (line.trim().isEmpty() || line.toLowerCase().startsWith("sepal")) continue;
                String[] values = line.split(",");
                if (values.length < 5) continue;

                float sepalLen = Float.parseFloat(values[0]);
                float sepalWid = Float.parseFloat(values[1]);
                float petalLen = Float.parseFloat(values[2]);
                float petalWid = Float.parseFloat(values[3]);
                String irisType = values[4];

                irisDataSet.add(new IrisData(sepalLen, sepalWid, petalLen, petalWid, irisType));
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return irisDataSet;
    }
    public static void trainTestSplit(List<IrisData> dataSet, // whole dataSet
                                      List<IrisData> trainSet, // teach model
                                      List<IrisData> testSet, // test model
                                      double trainRatio) { // 66% train and 34% test

        List<String> uniqueClasses = new ArrayList<>();
        for(IrisData irisData : dataSet) { // Finding all unique classes which are not in our list
            if(!uniqueClasses.contains(irisData.getIrisClass())) {
                uniqueClasses.add(irisData.getIrisClass());
            }
        }
        for (String irisClass : uniqueClasses) { // adds data to correspond class
            List<IrisData> irisClassData = new ArrayList<>();
            for (IrisData data : dataSet) {
                if(data.getIrisClass().equals(irisClass)) {
                    irisClassData.add(data);
                }
            }
            Collections.shuffle(irisClassData, new Random());
            int trainSize = (int) Math.floor(irisClassData.size() * trainRatio);

            for (int i = 0; i < trainSize; i++) { //first part will go to trainSet
                trainSet.add(irisClassData.get(i));
            }

            for (int i = trainSize; i < irisClassData.size(); i++) { // and rest of it to testSet
                testSet.add(irisClassData.get(i));
            }

            Collections.shuffle(trainSet, new Random());
            Collections.shuffle(testSet, new Random());
        }
    }

    //Perceptron
    public static double[][] toFeatures(List<IrisData> dataSet, int f1, int f2) {
        double[][] features = new double[dataSet.size()][2]; // size as dataSet and 2 columns
        for(int i =0; i< dataSet.size(); i++) {
            IrisData irisData = dataSet.get(i);
            features[i][0] = irisData.getFeaturesOfIrisByIndex(f1); //sepal_width
            features[i][1] = irisData.getFeaturesOfIrisByIndex(f2); //sepal_length
        }
        return features;
    }

    public static double[] toLabels(List<IrisData> dataSet) {
        double[] labels = new double[dataSet.size()];
        for (int i =0; i < dataSet.size(); i++) {
            String irisClass = dataSet.get(i).getIrisClass().toLowerCase();
            labels[i] = irisClass.contains("setosa") ? 1 : 0;
        }
        return labels;
    }

}
