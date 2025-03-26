package Plots;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import supervised_algorithms.Perceptron;

import javax.swing.*;
import java.awt.*;

public class DecisionBoundaryPlot {
    public static void showDecisionBoundary(double[][] testFeatures,
                                            double[] testLabels,
                                            Perceptron perceptron)
    {
        XYSeries class0 = new XYSeries("Class 0"); // set of points
        XYSeries class1 = new XYSeries("Class 1");

        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;

        for (int i = 0; i < testFeatures.length; i++) {
            double x = testFeatures[i][0];
            double y = testFeatures[i][1];
            if (x < minX) minX = x; // checking min/max for build a line(to bound it and do not write to infinity)
            if (x > maxX) maxX = x;

            if (testLabels[i] == 0.0) {
                class0.add(x, y);
            } else {
                class1.add(x, y);
            }
        }

        XYSeries boundary = new XYSeries("Decision Boundary");
        double[] w = perceptron.getWeights(); // [w0, w1]
        double thr = perceptron.getThreshold();

        double weights0 = w[0];
        double weights1 = w[1];

        // if Ei(wi*xi) - treshold >= 0;
        // 0 otherwise
        // two inputs x and y than ->
        //w0*x+w1*y = threshold //border where perceptron decide 1 or 0->
        // -> y = (thr - w0*x)/w1
        if (Math.abs(weights1) > 1e-9) { //if w1 - 0 -> vertical line, avoid / by 0
            //from left bottom angle to right
            double xA = minX;
            double yA = (thr - weights0 * xA) / weights1;

            double xB = maxX;
            double yB = (thr - weights0 * xB) / weights1;

            boundary.add(xA, yA);
            boundary.add(xB, yB);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(class0);
        dataset.addSeries(class1);
        dataset.addSeries(boundary);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Decision Boundary",
                "X Feature",
                "Y Feature",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);

        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);

        renderer.setSeriesLinesVisible(2, true);
        renderer.setSeriesShapesVisible(2, false);

        plot.setRenderer(renderer);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JFrame frame = new JFrame("Decision Boundary (Arrays)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}