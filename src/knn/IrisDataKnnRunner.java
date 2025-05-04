package knn;

import java.util.Scanner;

public class IrisDataKnnRunner extends KNNRunner{
    @Override
    protected IrisData getSampleData() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Sepal Length: ");
        float sepalLen = sc.nextFloat();
        System.out.print("Sepal Width : ");
        float sepalWid = sc.nextFloat();
        System.out.print("Petal Length: ");
        float petalLen = sc.nextFloat();
        System.out.print("Petal Width : ");
        float petalWid = sc.nextFloat();
        return new IrisData(sepalLen, sepalWid, petalLen, petalWid, "Unknown");
    }

    @Override
    protected double encodeClass(String dataClass) {
        if (dataClass == null) return -1.0;

        return switch (dataClass.toLowerCase()) {
            case "setosa" -> 0.0;
            case "versicolor" -> 1.0;
            case "virginica" -> 2.0;
            default -> -1.0; // неизвестный класс
        };

    }
}
