package Huffman;

import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanEncoder {
    private final HuffmanNode root;
    private final HashMap<Character, String> characterCodes = new HashMap<>();


    public HuffmanEncoder(String trainData) {
        var charsFrequencies = new HashMap<Character, Integer>();
        for (int i = 0; i < trainData.length(); i++) { //?Count charsFrequencies
            char c = trainData.charAt(i);
            charsFrequencies.put(c, charsFrequencies.getOrDefault(c, 0) + 1);
        }

        for (var entry : charsFrequencies.entrySet()) {
            System.out.println(entry.getKey() + " occurs: " + entry.getValue() + " times");
        }

        var queue = new PriorityQueue<HuffmanNode>();//? will get by priority(asc amount)
        for (var entry : charsFrequencies.entrySet()) {
            var node = new HuffmanNode(entry.getKey(), entry.getValue());
            queue.add(node);
        }
        while (queue.size() > 1) {

            var leftNode = queue.poll(); //? queue.poll get leftNode
            var rightNode = queue.poll();
            var combinedNode = new HuffmanNode(leftNode, rightNode);
            queue.add(combinedNode);
        }

        root = queue.poll(); //? got combined root
        calculateCodes(root,"");
        for (var entry : characterCodes.entrySet()) {
            System.out.println("'" + entry.getKey() + "' => " + entry.getValue());
        }
    }
    private void calculateCodes(HuffmanNode node,String currentPath) {
        if(node.isLeaf()) {
            characterCodes.put(node.getSymbol(), currentPath);
        }
        else {
            calculateCodes(node.getLeftChild(),currentPath+"0");
            calculateCodes(node.getRightChild(),currentPath+"1");
        }
    }
    String encode(String data) {
        if(data.isEmpty()) {
            return "";
        }
        StringBuilder encodedData = new StringBuilder();
        for (char ch : data.toCharArray()) {
            encodedData.append(characterCodes.get(ch));
        }

        int originalBits = data.length() * 8; //? "aaab" ->  4 *8 = 32bits
        int encodedBits = encodedData.length(); //? 'a' -> 0, 'b' -> 10 => "00010" -> 5 bits, 1-5/32=0.84 albo 84%
        double compressionRatio = 1.0 - (double) encodedBits / originalBits;
        System.out.printf("Original bits: %d, Encoded bits: %d, Compression ratio: %.2f%n",
                originalBits, encodedBits, compressionRatio);

        return encodedData.toString();
    }
    public String decode(String data) { //? find leaf add symbol, update root, repeat
        if(data.isEmpty()) {
            return "";
        }
        HuffmanNode currentNode = root;
        StringBuilder decodedData = new StringBuilder();
        for (char bit : data.toCharArray()) {
            currentNode = (bit == '0') ? currentNode.getLeftChild() : currentNode.getRightChild();
            if(currentNode.isLeaf()) {
                decodedData.append(currentNode.getSymbol());
                currentNode = root;
            }
        }
        return decodedData.toString();
    }
}
