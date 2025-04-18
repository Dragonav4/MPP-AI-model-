package dataSet;
public class TextProcessor {
    public static double[] textToVector(String text) {
        double[] vector = new double[26];
        text = text.toLowerCase().replaceAll("[^a-z]", "");
        int length = text.length();

        if (length == 0) return vector;

        for (char c : text.toCharArray()) { // frequency of appearance letter in the text
            int index = c - 'a'; //compute the index of letter
            if (index >= 0 && index < 26) {
                vector[index]++;
            }
        }

        for (int i = 0; i < 26; i++) {
            vector[i] /= length; //normalize to get actual % of frequency in text. In total = 1
        }
        return vector;
    }
}
