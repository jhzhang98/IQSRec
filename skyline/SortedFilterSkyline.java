package skyline;

import element.Point;

import java.util.ArrayList;
import java.util.List;

public class SortedFilterSkyline {

    /***
     * sort the inputData by the sum of each row in DESCENDING
     * @return the index after sorting
     */
    protected static int[] sortedBySum(float[][] inputData) {
        List<Point> sortedIndex = new ArrayList<>();
        for (int i = 0; i < inputData.length; i++) {
            float sum = 0;
            for (int j = 0; j < inputData[0].length; j++)
                sum += inputData[i][j];
            sortedIndex.add(new Point(i, sum));
        }

        sortedIndex.sort((o1, o2) -> Float.compare(o2.weight, o1.weight));

        int[] index = new int[sortedIndex.size()];
        for (int i = 0; i < index.length; i++)
            index[i] = sortedIndex.get(i).index;

        return index;
    }

    /***
     * if p dominate q, return true, else false
     */
    private boolean dominate(float[] p, float[] q) {
        for (int i = 0; i < p.length; i++)
            if (p[i] < q[i])
                return false;
        return true;
    }

    /***
     * simple implements of sfs algorithm
     */
    public int[] getSkyline(float[][] data) {
        int[] sortedIndex = sortedBySum(data);
        List<Integer> skyline = new ArrayList<>();
        skyline.add(sortedIndex[0]);

        for (int i = 1; i < data.length; i++) {
            int index = sortedIndex[i];
            boolean continueFlag = false;
            for (int sp : skyline) {
                if (dominate(data[sp], data[index])) {
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag) continue;

            skyline.add(index);
        }

        return skyline.stream().mapToInt(Integer::valueOf).toArray();
    }


}
