import java.util.*;

public class KNearestNeighbours {
    private double k; // amount of neighbours
    private List<IrisData> trainDataSet;

    public KNearestNeighbours(double k, List<IrisData> trainDataSet) {
        this.k = k;
        this.trainDataSet = trainDataSet;
    }

    private static double calculateEuclideanDistance(IrisData data, IrisData data2) {
        float[] feature1 = data.getFeaturesOfIris();
        float[] feature2 = data.getFeaturesOfIris();
        double sum=0.0;

        for (int i = 0; i < feature1.length; i++) {
            double diffBetweenVectors = feature1[i]-feature2[i];
            sum+=diffBetweenVectors;
        }
        return Math.sqrt(sum);

    }

    private static void sortDistances(List<double[]> distances) {
        for (int i = 0; i < distances.size(); i++) {
            double[] current = distances.get(i);
        double currentDistance = current[0];
            int j = i-1;
            while(j>=0 && distances.get(j)[0] > currentDistance) {
                distances.set(j+1,distances.get(j));
                j--;
            }
            distances.set(j + 1, current);
        }
        //*
        //QuickSort algo
        // start with 1(thinking that firtst elem stands correct)
        // [ 4.2, 1.5, 3.7, 2.0 ]  | for (int i = 0; i < distances.size(); i++) // going through distances
        // [ 4.2, (1.5), 3.7, 2.0 ] | first index = 1 and distance = 1.5 so we will try to find correct place for him
        // [ 4.2, (1.5), 3.7, 2.0 ] | j=i-1 last elem of sorted part of arr now its 0
        // [ 4.2, (1.5), 3.7, 2.0 ] |  //if distance[j][0] > currentDistance <<current>> should stand before him
        // [ (4.2), 4.2, 3.7, 2.0 ] | //move 4.2 to +1 index to the right and decrease j--
        // [ (1.5), 4.2, 3.7, 2.0 ] | //cause of j-- >= j-1, we know that is right place and set here our distance
    }

    public String findPredictedClass(List<IrisData> closestNeighbours) { //finding in closestNeighbours the most frequently class(count)
        HashMap<String, Integer> entry = new HashMap<>(); //String - class, Integer - count
        for(IrisData neighbour : closestNeighbours) {
            String cls = neighbour.getIrisClass();
            entry.put(cls, entry.getOrDefault(cls,0) +1); // met fitst -> 0 after that +1
        }

        //find most frequently class among others
        int maxCount = 0;
        List<String> mostFrequentClasses = new ArrayList<>();
        for (int c : entry.values()) {if(maxCount < c ) { maxCount = c;}}
        //collect all names of those who has maxCount
        for(String s : entry.keySet()) {if(entry.get(s) == maxCount) {mostFrequentClasses.add(s);}}


        if(mostFrequentClasses.size() == 1) { // if only 1 or the same amount of classes in the most fre... we will peek at random
            return mostFrequentClasses.get(0);
        } else {
            Random random = new Random();
            return mostFrequentClasses.get(random.nextInt(mostFrequentClasses.size()));
        }
    }

    public String predict(IrisData newObservation) {
        List<double[]> distances = new ArrayList<>(); //distances[distance,index(in trainDataSet)]
        for (int i = 0; i < trainDataSet.size(); i++) { // where distance its distance from newObs to trainDataSet
            IrisData trainObs = trainDataSet.get(i);
            double distance = calculateEuclideanDistance(newObservation,trainObs);
            distances.add(new double[]{distance,i});
        }
        sortDistances(distances);

        List<IrisData> closestNeighbours = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int idx = (int) distances.get(i)[1];
            closestNeighbours.add(trainDataSet.get(idx));
        }
        return findPredictedClass(closestNeighbours);
    }
}
