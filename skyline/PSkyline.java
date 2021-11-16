package skyline;

import util.FileHandler;

import java.util.*;

public class PSkyline {
    protected float[][] data = null;
    protected int row, col;
    protected List<Map<Float, Float>> bigger = new ArrayList<>();
    protected List<Map<Float, Float>> smaller = new ArrayList<>();
    protected float[] probMedian;

    public void initData(float[][] data) {
        this.data = data;
        this.row = data.length;
        this.col = data[0].length;
        probMedian = new float[col];
        bigger = new ArrayList<>();
        smaller = new ArrayList<>();
        for (int i = 0; i < col; i++) {
            bigger.add(new HashMap<>());
            smaller.add(new HashMap<>());
        }
    }

    /***
     * read file according to filePath
     * @param filePath input file path
     * @param withHead true: first line of file is : (num of service, num of attr)
     *                 false: service * attr
     */
    public void readFile(String filePath, boolean withHead) {
        data = new FileHandler().readFile(filePath, withHead);
        row = data.length;
        col = data[0].length;
        probMedian = new float[col];
        for (int i = 0; i < col; i++) {
            bigger.add(new HashMap<>());
            smaller.add(new HashMap<>());
        }
    }

    /***
     * find the duplicate value of data, return num of duplicate value
     */
    private int uniqueAttrNum(float[] data) {
        Set<Float> set = new HashSet<>();
        for (float value : data) {
            if (value < 0)
                continue;
            set.add(value);
        }
        return set.size();
    }

    /**
     * calculate the pdf
     * Because of it's discrete distribution,
     * the probability greater than v is the sum of the value frequencies greater than v
     */
    protected void initPDF() {
        for (int i = 0; i < col; i++) {
            float[] attr = getAttr(i);
            Arrays.sort(attr);

            int startIndex = 0;
            for (int j = startIndex; j < attr.length; j++) {
                if (attr[j] >= -1e-8) {
                    startIndex = j;
                    break;
                }
            }

            float attrNum = attr.length - startIndex;
            float uniqueCount = 1;
            float uniqueAttrNum = uniqueAttrNum(attr);
            float smaller = 0;
            float equals = 1;
            float currentValue = attr[startIndex];
            boolean medianFound = false;

            for (int j = startIndex + 1; j < attr.length; j++) {
                float value = attr[j];

                if (!medianFound && smaller + equals >= 0.5 * attrNum) {
                    probMedian[i] = value;
                    medianFound = true;
                }

                if (Math.abs(value - currentValue) < 1e-10)
                    equals += 1;
                else {
                    this.smaller.get(i).put(currentValue, uniqueCount / uniqueAttrNum);
                    this.bigger.get(i).put(currentValue, (attrNum - smaller - 1) / (attrNum - 1));
                    smaller += equals;
                    equals = 1;
                    uniqueCount++;
                    currentValue = value;
                }
            }
            this.smaller.get(i).put(currentValue, uniqueCount / uniqueAttrNum);
            this.bigger.get(i).put(currentValue, (attrNum - smaller - 1) / (attrNum - 1));
        }
    }

    /***
     * @param index the selected attribute index
     * @return the selected attribute
     */
    protected float[] getAttr(int index) {
        float[] attr = new float[row];
        for (int i = 0; i < row; i++)
            attr[i] = data[i][index];
        return attr;
    }

    /***
     * select parts of the data
     * @param data all data
     * @param index the index chosen
     * @return services selected
     */
    protected float[][] dataSlice(float[][] data, int[] index) {
        float[][] slice = new float[index.length][data[0].length];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = data[index[i]];
        }
        return slice;
    }

}
