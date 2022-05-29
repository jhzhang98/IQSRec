package skyline;

import element.Point;

import java.util.*;

/**
 * Dimension-based partition
 */
public class Partition {
    /***
     * calculate the score of attr, bigger is better
     */
    private Map<Float, Float> calculateScore(float[] attr) {
        Map<Float, Float> score = new HashMap<>();
        // calculate unique elements, sort them by ascending order
        Set<Float> attrSet = new HashSet<>();
        for (float value : attr)
            attrSet.add(value);
        List<Float> attrList = new ArrayList<>(attrSet);

        attrList.sort(Float::compare);

        if (attrList.get(0) < 0)
            score.put(-1f, 0f);

        // because of bigger is better, the later the attr, the higher the score
        for (int i = 0; i < attrList.size(); i++)
            score.put(attrList.get(i), (1 + i) * 1.0f / attrList.size());

        return score;
    }


    /***
     * execute the data partition
     */
    protected List<List<Integer>> clusterSort(float[][] data) {
        // initialize a cluster for each dimension
        List<List<Point>> clusters = new ArrayList<>();
        for (int i = 0; i < data[0].length; i++) {
            List<Point> cluster = new ArrayList<>();
            clusters.add(cluster);
        }

        // calculate the score of each dimension
        List<Map<Float, Float>> scores = new ArrayList<>();
        for (int j = 0; j < data[0].length; j++) {
            float[] dataAttr = new float[data.length];
            for (int i = 0; i < data.length; i++)
                dataAttr[i] = data[i][j];

            scores.add(calculateScore(dataAttr));
        }

        // for each point, classify by its score
        for (int i = 0; i < data.length; i++) {
            float maxScore = Float.MIN_VALUE;
            int index = -1;
            for (int j = 0; j < data[0].length; j++) {
                float score = scores.get(j).get(data[i][j]);
                if (score > maxScore) {
                    maxScore = score;
                    index = j;
                }
            }
            // put data[i] by its index of maxScore
            clusters.get(index).add(new Point(i, maxScore));
        }

        // after classify all data, sort each cluster by its weight in descending order
        for (List<Point> cluster : clusters) {
            cluster.sort((o1, o2) -> Float.compare(o2.weight, o1.weight));
        }

        // transfer List<Point> to List<Integer>, only keep index
        List<List<Integer>> clustersIndex = new ArrayList<>();
        for (List<Point> cluster : clusters) {
            List<Integer> clusterIndex = new ArrayList<>();
            for (Point point : cluster) {
                clusterIndex.add(point.index);
            }
            clustersIndex.add(clusterIndex);
        }

        return clustersIndex;
    }
}
