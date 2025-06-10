package BruteForce;

import java.util.List;

public class ResultЬMetrics {
    public final List<List<Integer>> sets;
    public final int bestValue;
    public final int bestFeasiability;
    public ResultЬMetrics(List<List<Integer>> sets, int bestValue, int bestFeasiability) {
        this.sets = sets;
        this.bestValue = bestValue;
        this.bestFeasiability = bestFeasiability;

    }
}

