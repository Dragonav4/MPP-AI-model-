package Huffman;

import Utils.DataLoader;
import Utils.GenericCsvLoader;


public class HuffmanMain {
    public static void main(String[] args) throws Exception {
        DataLoader<String> loader = new GenericCsvLoader<>(
            "/Users/dragonav/Desktop/Study/4thSemestr/MiniProject/MPP/resources/language/english/MobyDyk_part1.txt",
            (line, lineNumber) -> {
                if (line == null || line.isBlank()) return null;
                return line;
            }
        );
        StringBuilder builder = new StringBuilder();
        for (String line : loader.load()) {
            builder.append(line);
        }
        String trainData = builder.toString();
        String testData = "AbraCadaBRa";
        String testDataWithNotExistingSymbols = "0123456789!@#$%^&*()xvw";
        HuffmanEncoder encoder = new HuffmanEncoder(trainData);
        String encoded = encoder.encode(testData);
        String decoded = encoder.decode(encoded);
        System.out.println("Coded string " + encoded);
        System.out.println("DeCoded string:  " + decoded);
    }
}
