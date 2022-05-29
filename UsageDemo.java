import skyline.IQSRec;
import skyline.SortedFilterSkyline;
import util.DataHandler;
import util.Evaluator;
import util.FileHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UsageDemo {

    public static void main(String[] args) {
        // rec service number = m * d
        int m = 3;
        // method 0: choose top candidateNum after sorting in descending, when dataset is large and m is small, it can be close to true point
        //        1: choose top candidateNum after calculating probability and sorting in descending, more accurate
        // default method = 0
        int method = 0;

        // Step 1. Read Origin File
        float[][] dataOrigin = FileHandler.readFile("data/qws_rate_0.2.txt", false);
        int n = dataOrigin.length, d = dataOrigin[0].length;

        // Step 2. Normalization
        boolean[] smallerBetter = new boolean[d];
        smallerBetter[0] = true;
        smallerBetter[8] = true;
        float[][] dataOriginNorm = DataHandler.dataNormalized(dataOrigin, smallerBetter);

        // Step 3. Get Skyline
        IQSRec iqsRec = new IQSRec();
        iqsRec.initData(dataOriginNorm);
        int[] skylineOrigin = iqsRec.getSkyline(m, method);
        tablePrint(dataOrigin, skylineOrigin);

        // Or you maybe want to slice this data
        // We prove a slice method: DataHandler.dataSlice(data, start, end, dimension)

        // Step 1. Tell Us the QoS Selected
        boolean[] dimensionSelected = new boolean[9];
        dimensionSelected[0] = true;
        dimensionSelected[1] = true;
        dimensionSelected[3] = true;
        dimensionSelected[6] = true;
        dimensionSelected[7] = true;

        int start = 1001, end = 2000;
        float[][] dataSlice = DataHandler.dataSlice(dataOrigin, start, end, dimensionSelected);
        // Step 2. Do Normalization
        smallerBetter = new boolean[]{true, false, false, false, true};
        float[][] dataSliceNorm = DataHandler.dataNormalized(dataSlice, smallerBetter);
        iqsRec.initData(dataSliceNorm);
        int[] skylineIQ2 = iqsRec.getSkyline(m, 0);
        System.out.println("\n==================================================\n");
        tablePrint(dataSlice, skylineIQ2);
    }

    private static void tablePrint(float[][] data, int[] indexes) {
        for (int index : indexes) {
            System.out.print((index + 1) + ": ");
            for (int j = 0; j < data[0].length; j++) {
                System.out.print(data[index][j] + "\t");
            }
            System.out.println();
        }
    }
}
