package NativeBayes;

import Utils.Classifier;

import java.util.ArrayList;
import java.util.List;

public class NativeBayesAdapter implements Classifier<StringObservation> {
    private final NaiveBayesClassifier naiveBayesClassifier;
    public NativeBayesAdapter(boolean applySmoothingAll) {
        this.naiveBayesClassifier = new NaiveBayesClassifier(applySmoothingAll, new ArrayList<>());
    }

    @Override
    public void train(List<StringObservation> train) {
        naiveBayesClassifier.train(train);
    }

    @Override
    public String predict(StringObservation x) {
        return naiveBayesClassifier.predict(x.attributes);
    }

    @Override
    public List<String> predictAll(List<StringObservation> xs) {
        return Classifier.super.predictAll(xs);
    }
}
