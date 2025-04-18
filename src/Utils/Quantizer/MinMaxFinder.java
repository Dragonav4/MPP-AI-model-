package Utils.Quantizer;

import java.util.Arrays;
import java.util.List;

public class MinMaxFinder {

    private double[] min;
    private double[] max;

    public void fit(List<? extends NumericDataPoint> data) {
        int dim = data.getFirst().getNumericFeatures().length;
        min = new double[dim];
        max = new double[dim];
        Arrays.fill(min, Double.MAX_VALUE);
        Arrays.fill(max, -Double.MAX_VALUE);

        for (NumericDataPoint dp : data) {
            double[] values = dp.getNumericFeatures();
            for (int i = 0; i < dim; i++) {
                min[i] = Math.min(min[i], values[i]);
                max[i] = Math.max(max[i], values[i]);
            }
        }
    }

    public double[] getMin() {
        return min;
    }

    public double[] getMax() {
        return max;
    }
}