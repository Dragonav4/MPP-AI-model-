package Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class LanguageClusterUtils {
    private LanguageClusterUtils() {}

    //Counts how many times each language appears in each cluster(ex. for claster number 1: 0-polish, 10-english and 15 french texts
    //-> french
    public static Map<Integer, Double> learnClusterMapping(List<Integer> clusters,
                                                           List<Double> labels) {
        Map<Integer, Map<Double, Integer>> count = new HashMap<>();
        for (int i = 0; i < clusters.size(); i++) {
            int c = clusters.get(i);
            double l = labels.get(i);
            count.computeIfAbsent(c, k -> new HashMap<>())
                    .merge(l, 1, Integer::sum);
        }
        Map<Integer, Double> best = new HashMap<>();
        for (var e : count.entrySet()) {
            double majority = e.getValue().entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get().getKey();
            best.put(e.getKey(), majority);
        }
        return best;
    }
}