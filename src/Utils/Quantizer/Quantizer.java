package Utils.Quantizer;

public class Quantizer {

    private final double[] min, max;
    private final int bins;

    public Quantizer(double[] min, double[] max, int bins) {
        this.min = min;
        this.max = max;
        this.bins = bins;
    }

    public String[] quantize(double[] features) {
        String[] categories = new String[features.length];
        for (int i = 0; i < features.length; i++) {
            double interval = (max[i] - min[i]) / bins;
            int bin = (int) ((features[i] - min[i]) / interval);
            if (bin >= bins) bin = bins - 1;
            if (bin < 0) bin = 0;
            categories[i] = "bin_" + bin;
        }
        return categories;
    }
}