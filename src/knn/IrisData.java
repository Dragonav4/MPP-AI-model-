package knn;

import NativeBayes.DoubleObservation;
import Utils.HasLabel;

import java.util.LinkedHashMap;
import java.util.Map;

public class IrisData extends DoubleObservation
        implements HasLabel { //storing a data from knn.IrisData

    public IrisData(
            double sepalLen,
            double sepalWid,
            double petalLen,
            double petalWid,
            String itemClass
            ) {
        this(new LinkedHashMap<>() {{
            put("sepal_length", sepalLen);
            put("sepal_width", sepalWid);
            put("petal_length", petalLen);
            put("petal_width",petalWid);
        }}, itemClass);
    }
    public IrisData(
            Map<String, Double> features,
            String itemClass) {
        super(features, itemClass);
    }
//    @Override
//    public String toString() {
//        var features = getNumericFeatures();
//        var itemClass = this.label;
//        return STR."knn.IrisData{sepal_length=\{features[2]}, sepal_width=\{features[3]}, petal_length=\{features[0]}, petal_width=\{features[1]}, irisClass='\{itemClass}\{'\''}\{'}'}";
//    }

    public static DoubleObservation getSampleData(String line) {
        if (line.trim().isEmpty())
            return null;
        try {
            String[] values = line.split(",");

            var sepalLen = Double.parseDouble(values[0]);
            var sepalWid = Double.parseDouble(values[1]);
            var petalLen = Double.parseDouble(values[2]);
            var petalWid = Double.parseDouble(values[3]);
            String irisType = values[4];
            return new IrisData(sepalLen, sepalWid, petalLen, petalWid, irisType);
        }
        catch (Exception ex) {
            return null;
        }
    }
    public static double getBinaryClass(DoubleObservation data) {
        return data.getLabel().contains("setosa") ? 1.0 : 0.0;
    }

}
