package Plots;

import NativeBayes.DoubleObservation;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class IrisClusterPlot {
    public static void generatePlot(
            List<DoubleObservation> data,
            List<Integer> labels,
            List<double[]> centroids,
            String outputCsvPath,
            double wcss) throws Exception {
        try (var pw = new java.io.PrintWriter(outputCsvPath)) {
            pw.println("sepal_length,sepal_width,petal_length,petal_width,cluster");
            for (int i = 0; i < data.size(); i++) {
                double[] f = data.get(i).getNumericFeatures();
                pw.printf(Locale.US, "%.3f,%.3f,%.3f,%.3f,%d%n",
                        f[0], f[1], f[2], f[3], labels.get(i));
            }
        }
        String wcssArg = String.format(Locale.US,"%.4f",wcss);
        // Invoke Python plotting script
        ProcessBuilder pb = new ProcessBuilder(
                "./venv/bin/python",
                "resources/scripts/iris_plot.py",
                "--wcss",wcssArg,
                outputCsvPath);
        pb.directory(new File(System.getProperty("user.dir")));
        pb.redirectErrorStream(true);
        pb.inheritIO();

        Process p = pb.start();
        int exitCode = p.waitFor();
        if (exitCode != 0) {
            System.err.println("Script ended with error: " + exitCode + ")");
        } else {
            System.out.println("Check your plots!");
        }
    }
}