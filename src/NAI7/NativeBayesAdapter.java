package NAI7;

import Utils.Classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NativeBayesAdapter implements Classifier<Observation> {
    private final NaiveBayesClassifier naiveBayesClassifier;
    public NativeBayesAdapter(boolean applySmoothingAll) {
        this.naiveBayesClassifier = new NaiveBayesClassifier(applySmoothingAll, new ArrayList<>(),new HashMap<>(),new HashMap<>());
    }

    @Override
    public void train(List<Observation> train) {
        naiveBayesClassifier.train(train);
    }

    @Override
    public String predict(Observation x) {
        return naiveBayesClassifier.predict(x.attributes);
    }

    @Override
    public List<String> predictAll(List<Observation> xs) {
        return Classifier.super.predictAll(xs);
    }
}
