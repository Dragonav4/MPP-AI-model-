package findCloseCluster;

import NativeBayes.DoubleObservation;
import NativeBayes.NaiveBayesClassifier;
import Utils.Classifier;

import java.util.ArrayList;
import java.util.List;

public class ClusterAdapter implements Classifier<DoubleObservation> {

    private final K_means kMeans;

    public ClusterAdapter(K_means kMeans) {
        this.kMeans = kMeans;
    }

    @Override
    public void train(List<DoubleObservation> train) {
        kMeans.fit(train);
    }

    @Override
    public String predict(DoubleObservation x) {
        int clusterId = kMeans.predict(x.getNumericFeatures());
        return Integer.toString(clusterId);
    }

    @Override
    public List<String> predictAll(List<DoubleObservation> xs) {
        return Classifier.super.predictAll(xs);
    }
}
