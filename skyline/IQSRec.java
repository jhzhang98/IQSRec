package skyline;

import element.Point;

import java.util.*;

public class IQSRec extends PSkyline {
    private final float[] runTime = new float[5];
    private static final String[] timeLabel = {"PDF\", \"PAR\", \"Candidate\", \"Global\", \"Final Select"};

    public float[] getTime() {
        return this.runTime;
    }

    public static String[] getTimeLabel() {
        return timeLabel;
    }

    /***
     * @param inputData: input data
     * @param index: the data selected to calculate skyline num
     * @return skyline num estimated
     */
    private int calCandiNum(float[][] inputData, List<Integer> index) {
        float[][] data = dataSlice(inputData, index.stream().mapToInt(Integer::valueOf).toArray());
        double[][] F = new double[data.length][data[0].length];
        // 1. calculate the prob of smaller than each value
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j] < 0)
                    F[i][j] = 0.5;
                else
                    F[i][j] = smaller.get(j).get(data[i][j]);
            }
        // 2. for F, multiply by row, and subtract the result by 1
        double[] score = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            double tmp = 1.0;
            for (int j = 0; j < data[0].length; j++) {
                tmp *= F[i][j];
            }
            score[i] = 1 - tmp;
        }
        // 3.  score[i] = 1/n * score[i]^(n-1)
        int n = data.length - 1;
        for (int i = 0; i < score.length; i++) {
            score[i] = (1.0 / n) * Math.pow(score[i], n - 1);
        }
        // 4. sum, round
        double sum = n * Arrays.stream(score).sum();
        return (int) Math.ceil(sum);
    }

    /***
     * IQSRec
     * @param m for each dimension, choose m service
     * @param method 0: choose top candidateNum after sorting in descending, when dataset is large and m is small, it can be close to true point
     *               1: choose top candidateNum after calculating probability and sorting in descending, more accurate
     * @return skyline index
     */
    public int[] getSkyline(int m, int method) {
        long start = System.currentTimeMillis();
        this.initPDF();
        runTime[0] = (System.currentTimeMillis() - start) / 1000.0f;
        // 1. cluster
        start = System.currentTimeMillis();
        List<List<Integer>> clusters = new Partition().clusterSort(this.data);
        runTime[1] = (System.currentTimeMillis() - start) / 1000.0f;

        // 2. ensure the skyline in each cluster
        start = System.currentTimeMillis();
        List<Integer> candidateSkyline = new ArrayList<>();
        List<Integer> candidateLabel = new ArrayList<>();

        List<List<Point>> resClusters = new ArrayList<>();
        for (int i = 0; i < col; i++)
            resClusters.add(new ArrayList<>());

        // take the top candidateNum services directly
        if (method == 0) {
            for (int i = 0; i < clusters.size(); i++) {
                List<Integer> cluster = clusters.get(i);
                int candidateNum = calCandiNum(data, cluster);
                candidateNum = Math.min(candidateNum, m * col);
                candidateNum = Math.min(candidateNum, cluster.size());
                for (int j = 0; j < candidateNum; j++) {
                    candidateSkyline.add(cluster.get(j));
                    candidateLabel.add(i);
                }
            }
        }

        // calculate the prob, take the candidateNum services with biggest prob
        else if (method == 1) {
            for (int i = 0; i < clusters.size(); i++) {
                List<Integer> cluster = clusters.get(i);
                int candidateNum = calCandiNum(data, cluster); // 这里估算一下应该有多少skyline点
                candidateNum = Math.min(candidateNum, m * col);   // 取二者较小值
                candidateNum = Math.min(candidateNum, cluster.size());
                MSPC mspc = new MSPC();
                mspc.initData(dataSlice(data, cluster.stream().mapToInt(Integer::valueOf).toArray()));
                int[] skylineIndex = mspc.getSkyline(candidateNum);

                for (int j = 0; j < candidateNum; j++) {
                    candidateSkyline.add(cluster.get(skylineIndex[j]));
                    candidateLabel.add(i);
                }
            }
        }
        runTime[2] = (System.currentTimeMillis() - start) / 1000.0f;

        start = System.currentTimeMillis();
        // 3. calculate p-skyline
        float[][] dataCandidate = dataSlice(data, candidateSkyline.stream().mapToInt(Integer::valueOf).toArray());
        MSPC mspc = new MSPC();
        mspc.initData(dataCandidate);
        float[] candiProb = mspc.MSPCProb();
        runTime[3] = (System.currentTimeMillis() - start) / 1000.0f;

        start = System.currentTimeMillis();
        for (int i = 0; i < candidateSkyline.size(); i++) {
            int index = candidateSkyline.get(i);
            resClusters.get(candidateLabel.get(i)).add(new Point(index, candiProb[i]));
        }

        // 4. sorted in descending
        for (List<Point> resCluster : resClusters) {
            resCluster.sort((o1, o2) -> Float.compare(o2.weight, o1.weight));
        }

        List<Integer> skyline = new ArrayList<>();
        for (List<Point> resCluster : resClusters) {
            for (int j = 0; j < m && j < resCluster.size(); j++)
                skyline.add(resCluster.get(j).index);
        }
        runTime[4] = (System.currentTimeMillis() - start) / 1000.0f;
        return skyline.stream().mapToInt(Integer::valueOf).toArray();
    }

}
