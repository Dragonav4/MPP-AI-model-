package Utils;

import java.util.List;

public interface Classifier<T>{
    void train(List<T> train);
    String predict(T x);
    default List<String> predictAll(List<T> xs) {
        return xs.stream().map(this::predict).toList();
    }
}
