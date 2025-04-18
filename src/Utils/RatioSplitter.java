package Utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;


public class RatioSplitter<T> {
    private final double testRatio;
    private final Random rnd = new Random();

    public RatioSplitter(double testRatio) {
        if (testRatio <= 0.0 || testRatio >= 1.0) {
            throw new IllegalArgumentException("testRatio must be >0 and <1");
        }
        this.testRatio = testRatio;
    }

    public void split(List<T> all, List<T> train, List<T> test) {
        if (all.isEmpty()) {
            throw new IllegalArgumentException("Dataset is empty");
        }
        Collections.shuffle(all, rnd);
        int testSize = (int) Math.floor(all.size() * testRatio);
        if (testSize < 1 || testSize >= all.size()) {
            throw new IllegalArgumentException("Computed test size out of bounds: " + testSize);
        }
        test.addAll(all.subList(0, testSize));
        train.addAll(all.subList(testSize, all.size()));
    }
}
