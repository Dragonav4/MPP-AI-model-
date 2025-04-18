package Utils;

import java.util.List;

public interface DataLoader<T> {
    List<T> load() throws Exception;
}
