package skyline;

import element.Point;
import util.Evaluator;

import java.util.Arrays;


public class MSPC extends PSkyline {

    private float[][] smallerMatrix;
    private float[][] biggerMatrix;

    private final float[] runTime = new float[2];
    private static final String[] timeLabel = {"PDF", "Skyline"};

    public float[] getTime() {
        return this.runTime;
    }

    public static String[] getTimeLabel() {
        return timeLabel;
    }

    /***
     * transfer pdf to matrix
     * bigger[i][j] means the prob bigger than data[i][j]
     */
    private void initPDFMatrix() {
        smallerMatrix = new float[data.length][data[0].length];
        biggerMatrix = new float[data.length][data[0].length];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j] > -1e-6) {
                    biggerMatrix[i][j] = bigger.get(j).get(data[i][j]);
                    smallerMatrix[i][j] = smaller.get(j).get(data[i][j]);
                } else {
                    biggerMatrix[i][j] = Integer.MAX_VALUE;
                    smallerMatrix[i][j] = Integer.MIN_VALUE;
                }
            }
        }
    }

    /***
     * calculate the probability of point (index) in final skyline (not dominated by other points)
     * @param index point index
     * @return probability
     */
    private float skyProb(int index) {
        float[][] probAll = new float[row][col];
        float[] p = data[index];

        for (int i = 0; i < row; i++) {
            if (i == index)
                continue;
            float[] q = data[i];
            for (int j = 0; j < col; j++) {
                if (q[j] < 0 && p[j] < 0)
                    probAll[i][j] = 0.5f;
                else if (q[j] > -1e-8 && p[j] > -1e-8) {
                    if (q[j] >= p[j])
                        probAll[i][j] = 1;
                    else {
                        probAll[i][j] = 0;
                        break;
                    }
                } else if (q[j] > -1e-8 && p[j] < 0) {
                    probAll[i][j] = smallerMatrix[i][j];
//                    probAll[i][j] = smaller.get(j).get(q[j]);
                } else if (q[j] < 0 && p[j] > -1e-8) {
                    probAll[i][j] = biggerMatrix[index][j];
//                    probAll[i][j] = bigger.get(j).get(p[j]);
                }
            }
        }

        float domProb = 1;
        for (int i = 0; i < row; i++) {
            if (i == index)
                continue;
            float prob = 1;
            for (int j = 0; j < col; j++)
                prob *= probAll[i][j];
            domProb *= (1 - prob);
        }

        return domProb;
    }


    /**
     * calculate the probability of each service in data
     *
     * @return each service's probability in skyline
     */
    public float[] MSPCProb() {
        initPDF();
        initPDFMatrix();
        float[] prob = new float[row];
        for (int i = 0; i < row; i++) {
            prob[i] = skyProb(i);
        }

        return prob;
    }

    /***
     * calculate all services' probability, return top k biggest
     * @param topK k
     * @return top k services' index
     */
    public int[] getSkyline(int topK) {
        long start = System.currentTimeMillis();
        initPDF();
        initPDFMatrix();
        runTime[0] = (System.currentTimeMillis() - start) / 1000.0f;

        Point[] points = new Point[row];
        start = System.currentTimeMillis();
        for (int i = 0; i < row; i++) {
            points[i] = new Point(i, skyProb(i));
        }

        Arrays.sort(points, (o1, o2) -> Float.compare(o2.weight, o1.weight));

        int[] skyline = new int[topK];
        for (int i = 0; i < topK; i++)
            skyline[i] = points[i].index;
        runTime[1] = (System.currentTimeMillis() - start) / 1000.0f;

        return skyline;
    }

}
