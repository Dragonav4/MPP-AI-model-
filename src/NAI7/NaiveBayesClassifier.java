package NAI7;

import dataSet.Dataset;
import java.io.File;
import java.util.*;

public class NaiveBayesClassifier {
    private boolean applySmoothingAll;
    private List<Observation> trainDataSet;
    private Map<String,Double> priors = new HashMap<>();
    private Map<String,Map<String, Map<String,Double>>> conditionalProbability;
    private Map<String, Set<String>> attributeDomains;


    public NaiveBayesClassifier(boolean applySmoothingAll, List<Observation> trainDataSet, Map<String, Map<String, Map<String, Double>>> conditionalProbability, Map<String, Set<String>> attributeDomains) {
        this.applySmoothingAll = applySmoothingAll;
        this.trainDataSet = trainDataSet;
        this.conditionalProbability = conditionalProbability;
        this.attributeDomains = attributeDomains;

        calculateAttributeDomains();
        calculatePriors();
        calculateConditionalProbabilities();
    }

    public void calculateAttributeDomains() {
        for(Observation observation : trainDataSet) {
            for (Map.Entry<String,String> entry : observation.attributes.entrySet()) {
                String attribute = entry.getKey();
                String value = entry.getValue();
                attributeDomains.putIfAbsent(attribute, new HashSet<>());
                attributeDomains.get(attribute).add(value);
            }
        }
    }


    public void calculatePriors() {
        Map<String,Integer> classCount = new HashMap<>();
        int total = trainDataSet.size();
        for (Observation observation : trainDataSet) {
            classCount.put(observation.label, classCount.getOrDefault(observation.label,0)+1);
        }
        for(Map.Entry<String,Integer> entry : classCount.entrySet()) {
            priors.put(entry.getKey(), (double) entry.getValue()/total);
        }
    }
    public static class Observation {
        public Map<String,String> attributes;
        public String label;

        public Observation(Map<String, String> attributes, String label) {
            this.attributes = attributes;
            this.label = label;
        }
    }
    public double simpleSmoothing(double numerator, double denominator, int classes) {
        return (numerator + 1) / (denominator + classes);
    }

    private void calculateConditionalProbabilities() {
        Map<String,List<Observation>> observationByClass =new HashMap<>();
        for(Observation observation : trainDataSet) {
            observationByClass.putIfAbsent(observation.label, new ArrayList<>());
            observationByClass.get(observation.label).add(observation);
        }

        for(String clasz : observationByClass.keySet()) {
            List<Observation> classObs = observationByClass.get(clasz);
            int classCount = classObs.size();
            Map<String, Map<String, Double>> attrProbForClass =new HashMap<>();
            for(String attr : attributeDomains.keySet()) {
                Set<String> domain = attributeDomains.get(attr);
                Map<String,Integer> valueCounts = new HashMap<>();
                for(String value : domain) {
                    valueCounts.put(value,0);
                }

                for(Observation observation : classObs) {
                    String value = observation.attributes.get(attr);
                    if(value != null) {
                        valueCounts.put(value, valueCounts.get(value) +1);
                    }
                }
                Map<String, Double> probs = new HashMap<>();
                int domainSize = domain.size();
                for(String value : domain) {
                    int count = valueCounts.get(value);
                    double prob;
                    if(applySmoothingAll) {
                        prob = simpleSmoothing(count,classCount,domainSize);
                    } else {
                        if(count ==0) {
                            prob = simpleSmoothing(count,classCount, domainSize);
                        } else {
                            prob = (double) count / classCount;
                        }
                    }
                    probs.put(value,prob);
                }
                attrProbForClass.put(attr,probs);
            }
            conditionalProbability.put(clasz,attrProbForClass);
        }
    }

    public String predict(Map<String, String> inputAttributes) {
        String bestClass = null;
        double bestLogProb = Double.NEGATIVE_INFINITY;

        for (String clazz : priors.keySet()) {
            double logProb = Math.log(priors.get(clazz));
            Map<String, Map<String, Double>> attrProbs = conditionalProbability.get(clazz);

            for (Map.Entry<String, String> entry : inputAttributes.entrySet()) {
                String attr = entry.getKey();
                String value = entry.getValue();

                if (attrProbs.containsKey(attr)) {
                    Map<String, Double> valueProbs = attrProbs.get(attr);
                    double p = valueProbs.getOrDefault(value,
                            simpleSmoothing(0, getCountForClass(clazz), attributeDomains.get(attr).size()));
                    logProb += Math.log(p);
                }
            }

            if (logProb > bestLogProb) {
                bestLogProb = logProb;
                bestClass = clazz;
            }
        }

        return bestClass;
    }

    private int getCountForClass(String clazz) {
        int c = 0;
        for (Observation obs : trainDataSet) {
            if (obs.label.equals(clazz)) {
                c++;
            }
        }
        return c;
    }
}
class EvaluationMetrics {

    public static void evaluateBinary(List<String> predictions, List<String> groundTruth, String positiveClass) {
        // positiveClass = "yes"
        // negativeClass = "no"

        int tp = 0, tn = 0, fp = 0, fn = 0;
        for (int i = 0; i < predictions.size(); i++) {
            String pred = predictions.get(i);
            String real = groundTruth.get(i);

            boolean predPos = pred.equals(positiveClass);
            boolean realPos = real.equals(positiveClass);

            if (predPos && realPos) {
                tp++;
            } else if (predPos && !realPos) {
                fp++;
            } else if (!predPos && !realPos) {
                tn++;
            } else if (!predPos && realPos) {
                fn++;
            }
        }

        double accuracy = ((tp + tn) / (double) (tp + tn + fp + fn)) *100;
        double precision = tp + fp == 0 ? 0 : tp / (double) (tp + fp) * 100;
        double recall = tp + fn == 0 ? 0 : tp / (double) (tp + fn) * 100;
        double f1 = ((precision + recall) == 0 ? 0 : 2 * (precision * recall) / (precision + recall));

        System.out.println("Accuracy:  " + String.format("%.3f", accuracy)+"%");
        System.out.println("Precision: " + String.format("%.3f", precision)+"%");
        System.out.println("Recall:    " + String.format("%.3f", recall)+"%");
        System.out.println("F1 Score:  " + String.format("%.3f", f1)+"%");
    }
    public static List<NaiveBayesClassifier.Observation> readFileContent(File file) {
        String content = Dataset.readFileContent(file);
        if (content.isEmpty()) {
            throw new IllegalArgumentException("Dataset is empty or not found: " + file.getPath());
        }
        String[] rawLines;
        if(content.contains("\n") || content.contains("\r")) {
            rawLines = content.split("\\R");
        } else {
            rawLines=content.split("\\s+");
        }
        if (rawLines.length < 2) {
            throw new IllegalArgumentException("CSV file must contain a header and at least one data line: " + file.getPath());
        }

        String[] headers = rawLines[0].split(",");
        List<NaiveBayesClassifier.Observation> list = new ArrayList<>();

        for (int i = 1; i < rawLines.length; i++) {
            String line = rawLines[i].trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            Map<String, String> attrs = new HashMap<>();
            for (int j = 0; j < parts.length - 1; j++) {
                attrs.put(headers[j].trim(), parts[j].trim());
            }
            String label = parts[parts.length - 1].trim();
            list.add(new NaiveBayesClassifier.Observation(attrs, label));
        }
        return list;
    }


    public static void main(String[] args) {

        List<NaiveBayesClassifier.Observation> dataset =
                readFileContent(new File("/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/outgame"));
        Collections.shuffle(dataset);

        if (dataset.size() < 3) {
            throw new IllegalStateException("Dataset must contain at least 3 rows (found " + dataset.size() + ")");
        }

        List<NaiveBayesClassifier.Observation> testSet  = dataset.subList(0, 2);               // first 2 randomized rows
        List<NaiveBayesClassifier.Observation> trainSet = dataset.subList(2, dataset.size());  // the rest for training

        System.out.println("Train set size: " + trainSet.size());
        System.out.println("Test set size: " + testSet.size());

        Map<String, Map<String, Map<String, Double>>> condProb = new HashMap<>();
        Map<String, Set<String>> attrDomains = new HashMap<>();
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(false, trainSet, condProb, attrDomains);

        List<String> predictions = new ArrayList<>();
        List<String> groundTruth = new ArrayList<>();
        for (NaiveBayesClassifier.Observation obs : testSet) {
            String pred = classifier.predict(obs.attributes);
            predictions.add(pred);
            groundTruth.add(obs.label);
        }

        System.out.println("Predictions: " + predictions);
        System.out.println("Ground truth: " + groundTruth);

        evaluateBinary(predictions, groundTruth, "yes");
    }
}
