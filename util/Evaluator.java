package util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Evaluator {
    public float precision(int[] right, int[] predicted) {
        Set<Integer> rightSet = Arrays.stream(right).boxed().collect(Collectors.toSet());
        Set<Integer> predictedSet = Arrays.stream(predicted).boxed().collect(Collectors.toSet());
        rightSet.retainAll(predictedSet);
        return (float) (1.0 * rightSet.size() / predicted.length);
    }

    public float recall(int[] right, int[] predicted) {
        Set<Integer> rightSet = Arrays.stream(right).boxed().collect(Collectors.toSet());
        Set<Integer> predictedSet = Arrays.stream(predicted).boxed().collect(Collectors.toSet());
        rightSet.retainAll(predictedSet);
        return (float) (1.0 * rightSet.size() / right.length);
    }

    public float diversity(int[] predicted, float[][] data) {
        float[][] dataCp = meanValueFill(data);
        float[][] slice = dataSlice(dataCp, predicted);

        float diversity = 0;
        int count = 0;

        for (int i = 0; i < slice.length; i++) {
            for (int j = i + 1; j < slice.length; j++) {
                diversity += distance(slice[i], slice[j]);
                count += 1;
            }
        }

        return diversity / count;
    }
    
    private float[][] meanValueFill(float[][] data) {
        float[] meanValue = new float[data[0].length];
        for (int i = 0; i < data[0].length; i++) {
            float sum = 0;
            int count = 0;
            for (float[] line : data) {
                if (line[i] >= -1e-8) {
                    sum += line[i];
                    count += 1;
                }
            }
            meanValue[i] = sum / count;
        }
        float[][] dataCp = new float[data.length][data[0].length];
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j] < 0)
                    dataCp[i][j] = meanValue[j];
                else
                    dataCp[i][j] = data[i][j];
            }
        return dataCp;
    }

    private float[][] dataSlice(float[][] data, int[] index) {
        float[][] slice = new float[index.length][data[0].length];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = data[index[i]];
        }
        return slice;
    }

    private float distance(float[] vec1, float[] vec2) {
        float dis = 0;
        for (int i = 0; i < vec1.length; i++)
            dis += (vec1[i] - vec2[i]) * (vec1[i] - vec2[i]);
        return (float) Math.sqrt(dis);
    }

}
