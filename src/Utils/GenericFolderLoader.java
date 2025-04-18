package pipeline;

import Utils.DataLoader;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;


public class GenericFolderLoader<T> implements DataLoader<T> {
    private final String folderPath;
    private final BiFunction<File, String, T> fileParser;
    private final String[] extensions;

    public GenericFolderLoader(String folderPath,
                               BiFunction<File,String,T> fileParser,
                               String... extensions) {
        this.folderPath = folderPath;
        this.fileParser = fileParser;
        this.extensions = extensions;
    }

    @Override
    public List<T> load() throws Exception {
        List<T> out = new ArrayList<>();
        File dir = new File(folderPath);
        try (Stream<File> files = Files.walk(dir.toPath())
                .map(p -> p.toFile())
                .filter(File::isFile)) {
            files.filter(f -> {
                for (var ext : extensions)
                    if (f.getName().endsWith(ext)) return true;
                return false;
            }).forEach(f -> {
                // folder name as label, e.g. ".../english/file1.txt"
                String label = f.getParentFile().getName();
                T obj = fileParser.apply(f, label);
                if (obj != null) out.add(obj);
            });
        }
        return out;
    }
}