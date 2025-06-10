package Huffman;

public class HuffmanNode implements Comparable<HuffmanNode> {
    private char symbol;
    private HuffmanNode leftChild;
    private HuffmanNode rightChild;
    private int frequency;

    public HuffmanNode(char symbol, int frequency) {
        this.symbol = symbol;
        this.frequency = frequency;
    }

    public HuffmanNode(HuffmanNode leftChild, HuffmanNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.frequency = leftChild.frequency + rightChild.frequency;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }

    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public HuffmanNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(HuffmanNode leftChild) {
        this.leftChild = leftChild;
    }

    public HuffmanNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(HuffmanNode rightChild) {
        this.rightChild = rightChild;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
