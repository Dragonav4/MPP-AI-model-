package BruteForce;

import NativeBayes.StringObservation;
import Utils.DataLoader;
import Utils.GenericCsvLoader;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class BruteForce {
    public static void main(String[] args) throws Exception {
        DataLoader<StringObservation> loader = new GenericCsvLoader<>(
                "resources/knapsack_items",
                (line, lineNumber) -> {
                    if (line == null || line.isBlank() || lineNumber == 1) return null;
                    String[] parts = line.split(",");
                    if (parts.length < 2) return null;
                    try {
                        int w = Integer.parseInt(parts[0].trim());
                        int v = Integer.parseInt(parts[1].trim());
                        Map<String, String> attrs = new HashMap<>();
                        attrs.put("weight", parts[0].trim());
                        attrs.put("value", parts[1].trim());
                        return new StringObservation(attrs, parts[1].trim());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
        );
        List<StringObservation> observations = loader.load();
        List<Integer> weights = observations.stream()
                .map(obs -> Integer.parseInt(obs.attributes.get("weight")))
                .collect(Collectors.toList());
        List<Integer> values = observations.stream()
                .map(obs -> Integer.parseInt(obs.attributes.get("value")))
                .collect(Collectors.toList());
        int capacity = 10;

        Knapsack knap = new Knapsack(weights, values, capacity);


        long start = System.nanoTime();
        ResultЬMetrics resultЬMetrics = knap.greedyDensityApproach();
        long time = System.nanoTime() - start;
        printResult("Greedy Density Based Approach", resultЬMetrics, time);

        long startB = System.nanoTime();
        ResultЬMetrics resultЬMetricsB = knap.bruteForceBitMask();
        long timeB = System.nanoTime() - startB;
        printResult("Brute Force Approach", resultЬMetricsB, timeB);

    }

    private static void printResult(String title, ResultЬMetrics r, long nanoseconds) {
        System.out.println("--- " + title + " ---");
        Object items = (r.sets.size() == 1) ? r.sets.getFirst() : r.sets;
        System.out.println("Chosen items (indices): " + items);
        System.out.println("Obj. Value: " + r.bestValue);
        System.out.println("Feas. Value:" + r.bestFeasiability);
        System.out.printf("Time: %.4f sec%n", nanoseconds / 1e9);
    }
}
