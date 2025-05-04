package NativeBayes;

import java.util.*;

public class NaiveBayesClassifier {
    private boolean applySmoothingAll;
    private final List<StringObservation> trainDataSet = new ArrayList<>();
    private final Map<String,Double> priors = new HashMap<>();
    private final Map<String,Map<String, Map<String,Double>>> conditionalProbability = new HashMap<>();
    //conditionalProbability = {
    //  "yes" -> {
    //      "outlook" -> {
    //          "sunny" -> 1/3,
    //          "overcast" -> 1/3,
    //          "rainy" -> 1/3
    //      },
    //      "humidity" -> {
    //          "high" -> 2/3,
    //          "normal" -> 1/3
    //      },
    //      ...
    //  },
    //  "no" -> {
    //      "outlook" -> {
    //          "sunny" -> 1.0,
    //          "overcast" -> 0.0,
    //          "rainy" -> 0.0
    //      },
    //      ...
    //  }
    private final Map<String, Set<String>> attributeDomains = new HashMap<>();


    public NaiveBayesClassifier(boolean applySmoothingAll, List<StringObservation> trainDataSet) {
        this.applySmoothingAll = applySmoothingAll;
        train(trainDataSet);
    }

    //attributeDomains = {
    //    "outlook" -> ["sunny", "overcast", "rainy"],
    //    "humidity" -> ["high", "normal"]
    public void calculateAttributeDomains() { // just calculcate all amount of unique attributes such like "humidity", "windy" etc
        attributeDomains.clear();
        for(StringObservation stringObservation : trainDataSet) {
            for (Map.Entry<String,String> entry : stringObservation.attributes.entrySet()) {
                String attribute = entry.getKey();
                String value = entry.getValue();
                attributeDomains.putIfAbsent(attribute, new HashSet<>());
                attributeDomains.get(attribute).add(value);

            }
        }
    }


    //    priors = {
    //        "yes" -> 4 / 6 = 0.666...,
    //        "no"  -> 2 / 6 = 0.333...
    //    }
    public void calculatePriors() {
        Map<String,Integer> classCount = new HashMap<>(); // frequncy of clsses in trainDataSet
        int total = trainDataSet.size();

        for (StringObservation stringObservation : trainDataSet) {
            //for each class count+=1
            classCount.put(stringObservation.label, classCount.getOrDefault(stringObservation.label,0)+1);
        }
        priors.clear();
        for(Map.Entry<String,Integer> entry : classCount.entrySet()) {
            priors.put(entry.getKey(), (double) entry.getValue()/total);
        }
    }

    public double simpleSmoothing(double numerator, double denominator, int classes) {
        return (numerator + 1) / (denominator + classes);
    }

    private void calculateConditionalProbabilities() {
        Map<String,List<StringObservation>> observationByClass =new HashMap<>(); //yes -> "sunny,"humidity"
        for(StringObservation stringObservation : trainDataSet) {
            //fill obsClass
            observationByClass.putIfAbsent(stringObservation.label, new ArrayList<>());
            observationByClass.get(stringObservation.label).add(stringObservation);
        }
        conditionalProbability.clear();
        for(String clasz : observationByClass.keySet()) {
            List<StringObservation> classObs = observationByClass.get(clasz);
            int classCount = classObs.size();
            Map<String, Map<String, Double>> attrProbForClass =new HashMap<>();
            for(String attr : attributeDomains.keySet()) {
                Set<String> domain = attributeDomains.get(attr);
                Map<String,Integer> valueCounts = new HashMap<>();
                //valueCounts = {
                //    "sunny" -> 3,
                //    "rainy" -> 2,
                //    "overcast" -> 1
                //}
                for(String value : domain) {
                    valueCounts.put(value,0);
                }

                for(StringObservation stringObservation : classObs) {
                    String value = stringObservation.attributes.get(attr);
                    if(value != null) {
                        valueCounts.put(value, valueCounts.get(value) +1);
                    }
                }
                Map<String, Double> probs = new HashMap<>();
                int domainSize = domain.size();
                for(String value : domain) {
                    int count = valueCounts.get(value);
                    double prob;
                    if(count == 0 || applySmoothingAll) {
                        prob = simpleSmoothing(count,classCount,domainSize);
                    } else {
                        prob = (double) count / classCount;
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
        for (StringObservation obs : trainDataSet) {
            if (obs.label.equals(clazz)) {
                c++;
            }
        }
        return c;
    }


    public void train(List<StringObservation> newTrainData) {
        // replace the training dataset
        this.trainDataSet.clear();

        this.trainDataSet.addAll(newTrainData);
        // recalculate domains, priors and conditional probabilities
        calculateAttributeDomains();
        calculatePriors();
        calculateConditionalProbabilities();
    }
}

