import skyline.IQSRec;
import skyline.SortedFilterSkyline;
import util.Evaluator;
import util.FileHandler;

import javax.swing.*;
import java.io.File;
import java.util.Locale;

public class Main {
    public static File[] readFileGUI(String title) {
        JFileChooser fDialog = new JFileChooser();
        fDialog.setCurrentDirectory(new File("./"));
        // set title
        fDialog.setDialogTitle(title);
        // multiple selectoin
        fDialog.setMultiSelectionEnabled(true);
        int returnVal = fDialog.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == returnVal) {
            return fDialog.getSelectedFiles();
        } else {
            System.out.println("Choose File Stopped!");
            System.exit(0);
            return null;
        }
    }

    /***
     * @param m for each dimension, choose m service, for example m = 1, dimension = 9, IQSRec return size = 9
     * @param method 0: choose top candidateNum after sorting in descending, when dataset is large and m is small, it can be close to true point
     *               1: choose top candidateNum after calculating probability and sorting in descending, more accurate
     * @return precision
     */
    private static double guiDemo(int m, int method) {
        Locale.setDefault(Locale.ENGLISH);

        String message = "Please Choose One Complete Dataset";
        System.out.println(message);
        File[] fileComplete = readFileGUI(message);
        float[][] dataComplete = new FileHandler().readFile(fileComplete[0]);

        message = "Please Choose Incomplete Dataset";
        System.out.println(message);
        File[] filesNative = readFileGUI(message);
        float[][] dataSparse = new FileHandler().readFile(filesNative[0]);

        IQSRec iqsRec = new IQSRec();
        iqsRec.initData(dataSparse);
        int[] skylinePred = iqsRec.getSkyline(m, method);

        int[] rightSkyline = new SortedFilterSkyline().getSkyline(dataComplete);

        return new Evaluator().precision(rightSkyline, skylinePred);
    }

    public static void main(String[] args) {
        double precision = guiDemo(1, 1);
        System.out.println("precision: " + precision);
    }
}
