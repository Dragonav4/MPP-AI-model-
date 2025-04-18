package Utils;

import Utils.DataLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;

public class GenericCsvLoader<T> implements DataLoader<T> {
    private final String path;
    private final Function<String, T> parser;

    public GenericCsvLoader(String path, Function<String, T> parser) {
        this.path = path;
        this.parser = parser;
    }

    @Override
    public List<T> load() throws Exception {
        List<T> result = new ArrayList<>();
        try (var br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                T obj = parser.apply(line);
                if (obj != null) result.add(obj);
            }
        }
        return result;
    }
    public static String discretize(double value, double min, double max, int bins) {
        double interval = (max - min) / bins;
        int category = (int)((value - min) / interval);
        if (category >= bins) category = bins - 1;
        return "bin_" + category;
    }
}