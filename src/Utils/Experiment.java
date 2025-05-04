package Utils;

import java.util.ArrayList;
import java.util.List;

public class Experiment<T> {
    private final DataLoader<T> loader;
    private final Classifier<T> classifier;
    private final RatioSplitter<T> splitter;

    public Experiment(DataLoader<T> loader,
                      Classifier<T> classifier,
                      RatioSplitter<T> splitter) {
        this.loader = loader;
        this.classifier = classifier;
        this.splitter = splitter;
    }

    public void run() throws Exception {
        List<T> all = loader.load();
        List<T> train = new ArrayList<>(), test = new ArrayList<>();
        splitter.split(all, train, test);

        classifier.train(train);
        //each object in List cast to HasLabel(Interface) and via Interface gets REAL labels(Interface will be generic for apply any dype of data)
        List<String> real = test.stream().map(t -> ((HasLabel) t).getLabel()).toList(); //real labels|

        List<String> pred = classifier.predictAll(test); // pred labels

        ClassificationMetrics.printAll(real, pred);
    }
}