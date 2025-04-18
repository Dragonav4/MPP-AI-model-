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


    public void train(List<Observation> newTrainData) {
        // replace the training dataset
        this.trainDataSet.clear();
        this.trainDataSet.addAll(newTrainData);
        // recalculate domains, priors and conditional probabilities
        calculateAttributeDomains();
        calculatePriors();
        calculateConditionalProbabilities();
    }
}

