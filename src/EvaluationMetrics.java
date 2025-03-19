import java.util.List;

public class EvaluationMetrics {
    public static double measureAccuracy(List<String> realClasses, List<String>predictedClasses) {
        if (realClasses.size() != predictedClasses.size()) {
            throw new IllegalArgumentException("Lists must be of same size. Incorrect result :(");
        }
        int amountOfCorrect= 0;

        for (int i = 0; i < realClasses.size(); i++) {
            if(realClasses.get(i).equals(predictedClasses.get(i))) {
                amountOfCorrect++;
            }
        }
        return (double) amountOfCorrect / realClasses.size();
    }
}
