package Plots;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;
import java.awt.*;

public class AccuracyPlot {

    public static void showAccuracyChart(List<Double> accuracies) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < accuracies.size(); i++) {
            dataset.addValue(accuracies.get(i), "Accuracy", "Epoch " + (i + 1));
        }

        StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
        theme.setTitlePaint(Color.DARK_GRAY);
        theme.setExtraLargeFont(new Font("SansSerif", Font.BOLD, 16));
        theme.setLargeFont(new Font("SansSerif", Font.BOLD, 14));
        theme.setRegularFont(new Font("SansSerif", Font.PLAIN, 12));
        ChartFactory.setChartTheme(theme);

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Accuracy per Epoch",
                "Epoch",                // X
                "Accuracy",                             // Y
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlinePaint(Color.GRAY);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesPaint(0, new Color(14, 215, 0));

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 450));

        JFrame frame = new JFrame("Accuracy Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}