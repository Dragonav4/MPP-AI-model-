package dataSet;
public class TextProcessor {
    public static double[] textToVector(String text) {
        double[] vector = new double[26];
        text = text.toLowerCase().replaceAll("[^a-z]", "");
        int length = text.length();

        if (length == 0) return vector;

        for (char c : text.toCharArray()) {
            int index = c - 'a'; //compute the index of letter
            if (index >= 0 && index < 26) {
                vector[index]++;
            }
        }

        for (int i = 0; i < 26; i++) {
            vector[i] /= length; //normalize(% of each letter in the text)
        }
        //System.out.println("== Text aka vector ==");
        //System.out.println(Arrays.toString(vector));
        return vector;
    }
}
