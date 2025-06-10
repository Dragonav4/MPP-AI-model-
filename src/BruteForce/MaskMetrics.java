package BruteForce;

import java.util.List;

public class MaskMetrics {
    int feasibility;
    int objective;
    List<Integer> set;

    MaskMetrics(int feasibility, int objective, List<Integer> set) {
        this.feasibility = feasibility;
        this.objective = objective;
        this.set = set;
    }
}

