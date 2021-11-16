package skyline;

import element.Point;

import java.util.Arrays;

public class Native extends PSkyline {

    /***
     * @param topK top k probability services
     */
    public int[] getSkyline(int topK) {
        Point[] points = new Point[row];

        for (int i = 0; i < row; i++) {
            points[i] = new Point(i, nativeProb(i));
        }

        Arrays.sort(points, (o1, o2) -> Float.compare(o2.weight, o1.weight));

        int[] skyline = new int[topK];
        for (int i = 0; i < topK; i++)
            skyline[i] = points[i].index;

        return skyline;
    }

    /***
     * different from MSPC.skyProb, its pdf recalculated every time
     */
    private float nativeProb(int index) {
        float[] p = data[index];
        float skyProb = 1;
        for (int i = 0; i < row; i++) {
            float domProb = 1;
            if (index == i)
                continue;
            float[] q = data[i];

            for (int j = 0; j < col; j++) {
                if (q[j] < 0 && p[j] < 0)
                    domProb *= 0.5f;
                else if (q[j] > -1e-8 && p[j] > -1e-8) {
                    if (q[j] >= p[j])
                        domProb *= 1;
                    else {
                        domProb = 0;
                        break;
                    }
                } else if (q[j] > -1e-8 && p[j] < 0) {
                    domProb *= getSmaller(i, j);
//                    probAll[i][j] = smaller.get(j).get(q[j]);
                } else if (q[j] < 0 && p[j] > -1e-8) {
                    domProb *= getBigger(index, j);
//                    probAll[i][j] = bigger.get(j).get(p[j]);
                }
            }
            skyProb *= (1 - domProb);
        }

        return skyProb;
    }

    /***
     * find prob of bigger than data[i][j] in attr[j]
     */
    private float getBigger(int i, int j) {
        float[] attr = getAttr(j);
        float value = data[i][j];
        int count = 0;
        for (float v : attr) {
            if (value <= v)
                count++;
        }
        return (count - 1.0f) / (attr.length - 1);
    }

    /***
     * find prob of smaller than data[i][j] in attr[j]
     */
    private float getSmaller(int i, int j) {
        float[] attr = getAttr(j);
        float value = data[i][j];
        int count = 0;
        for (float v : attr) {
            if (value >= v)
                count++;
        }
        return (count - 1.0f) / (attr.length - 1);
    }

}
