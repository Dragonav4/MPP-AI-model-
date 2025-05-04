package dataSet;

import java.util.LinkedHashMap;
import java.util.Map;

public class TextProcessor {
    public static Map<String, Double> textToVector(String text) {
        //double[] vector = new double[26];
        var vector = new LinkedHashMap<String, Double>();
        for(var c='a'; c<='z'; c++)
            vector.put(c+"", 0.0);
        text = text.toLowerCase().replaceAll("[^a-z]", "");
        int length = text.length();

        if (length == 0) return vector;

        for (char c : text.toCharArray()) { // frequency of appearance letter in the text
      //      int index = c - 'a'; //compute the index of letter
            if (c >= 'a' && c<='z') {
                vector.put(String.valueOf(c), vector.get(String.valueOf(c))+1);
            }
        }

        for (var kvp: vector.entrySet()) {
            kvp.setValue(kvp.getValue()/length);
//            vector.put(String.valueOf(c), vector.get(String.valueOf(c))+1);
//            vector[i] /= length; //normalize to get actual % of frequency in text. In total = 1
        }
        return vector;
    }
}
