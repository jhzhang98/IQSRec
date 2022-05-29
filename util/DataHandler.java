package util;

public class DataHandler {
    public static float[][] dataNormalized(float[][] data, boolean[] smallerFlag) {
        int n = data.length;
        int d = data[0].length;
        float[][] res = new float[n][d];
        for (int j = 0; j < d; j++) {
            float min = Integer.MAX_VALUE;
            float max = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                if (data[i][j] < 0) {
                    continue;
                }
                min = Math.min(data[i][j], min);
                max = Math.max(data[i][j], max);
            }
            for (int i = 0; i < n; i++) {
                if (data[i][j] < 0) {
                    res[i][j] = -1;
                } else if (smallerFlag[j]) {
                    res[i][j] = (max - data[i][j]) / (max - min);
                } else {
                    res[i][j] = (data[i][j] - min) / (max - min);
                }
            }
        }
        return res;
    }

    public static float[][] dataSlice(float[][] data, int scaleStart, int scaleEnd, boolean[] dimension) {
        int dSelect = 0;
        for (boolean check : dimension) {
            if (check) dSelect++;
        }
        float[][] res = new float[scaleEnd - scaleStart + 1][dSelect];

        int resI = 0;
        for (int i = scaleStart - 1; i < scaleEnd; i++) {
            int resJ = 0;
            for (int j = 0; j < data[0].length; j++) {
                if (dimension[j]) {
                    res[resI][resJ++] = data[i][j];
                }
            }
            resI++;
        }
        return res;
    }
}
