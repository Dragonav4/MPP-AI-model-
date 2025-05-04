package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.function.BiFunction;

public class GenericCsvLoader<T> implements DataLoader<T> {
    private final String path;
    private final BiFunction<String, Integer, T> parser;

    public GenericCsvLoader(String path, BiFunction<String, Integer, T> parser) {
        this.path = path;
        this.parser = parser;
    }

    @Override
    public List<T> load() throws Exception {
        List<T> result = new ArrayList<>();
        int lineNumber = 1;
        try (var br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                T obj = parser.apply(line, lineNumber++);
                if (obj != null) result.add(obj);
            }
        }
        return result;
    }
}