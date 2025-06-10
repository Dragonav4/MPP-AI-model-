package BruteForce;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class Knapsack {
    private final List<Integer> weights;
    private final List<Integer> values;
    private final int capacity;
    private final int n;

    public Knapsack(List<Integer> itemWeights, List<Integer> itemValues, int capacity) {
        if (itemWeights.size() != itemValues.size()) {
            throw new IllegalArgumentException("Weights and values must have the same length");
        }
        this.weights = itemWeights;
        this.values = itemValues;
        this.capacity = capacity;
        this.n = itemWeights.size();
    }

    public ResultЬMetrics greedyDensityApproach() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);

        indices.sort(
                Comparator
                        .comparingDouble((Integer i) -> (double) values.get(i) / weights.get(i))
                        .reversed()
        );
        int remaining = capacity;
        int totalValue = 0;

        List<Integer> chosen = new ArrayList<>();
        for (int idx : indices) {
            if (weights.get(idx) <= remaining) {
                chosen.add(idx);
                remaining -= weights.get(idx);
                totalValue += values.get(idx);
            }
        }
        List<List<Integer>> sets = new ArrayList<>();
        sets.add(chosen);
        int feasibility = capacity - remaining;
        return new ResultЬMetrics(sets, totalValue, feasibility);
    }

    private MaskMetrics computeMaskMetrics(int mask) {
        int feasibility = 0;
        int objective = 0;
        List<Integer> currentSet = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                feasibility += weights.get(i);
                objective += values.get(i);
                currentSet.add(i);
            }
        }
        return new MaskMetrics(feasibility, objective, currentSet);
    }

    //000 2^3 -> 8
    //001 +1
    //010
    //011
    //100
    //101
    //110
    //111
    public ResultЬMetrics bruteForceBitMask() {
        int bestValue = 0;
        int bestFeasibility = 0;
        List<List<Integer>> bestSets = new ArrayList<>();

        int max = 1 << n;
        for (int mask = 0; mask < max; mask++) {
            MaskMetrics m = computeMaskMetrics(mask);
            if (m.feasibility > capacity) continue;

            if (m.objective > bestValue) {
                bestValue = m.objective;
                bestFeasibility = m.feasibility;

                bestSets.clear();
                bestSets.add(m.set);
            } else if (m.objective == bestValue) {
                bestSets.add(m.set);
                if (m.feasibility < bestFeasibility) {
                    bestFeasibility = m.feasibility;
                }
            }
        }
        return new ResultЬMetrics(bestSets, bestValue, bestFeasibility);
    }
}
