package util;

import java.io.*;
import java.util.*;

public class FileHandler {
    private static float[][] readFileWithHead(String filePath) {
        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String firstLine = br.readLine();
            String[] tmp = firstLine.split(",|[ ]");
            int row = Integer.parseInt(tmp[0]);
            int col = Integer.parseInt(tmp[1]);
            float[][] data = new float[row][col];
            String line = br.readLine();
            int i = 0;
            while (null != line) {
                String[] dataLine = line.split(",|[ ]");
                for (int j = 0; j < col; j++)
                    data[i][j] = Float.parseFloat(dataLine[j]);
                i++;
                line = br.readLine();
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * read file from disk, data is separated by ',' or ' '
     *
     * @param filePath file path
     * @param withHead if true -> first line must be: 'rowNum,colNum' or 'rowNum colNum'
     * @return default is float[][]
     */
    public static float[][] readFile(String filePath, boolean withHead) {
        if (withHead)
            return readFileWithHead(filePath);
        File file = new File(filePath);
        return readFile(file);
    }

    public static  float[][] readFile(File file) {
        List<float[]> data = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (null != line) {
                List<Float> lineNum = new ArrayList<>();
                String[] dataLine = line.split(",|[ ]");
                for (int i = 0; i < dataLine.length; i++) {
                    String ele = dataLine[i];
                    if (i == dataLine.length - 1 && ele.length() == 0)
                        continue;
                    if (ele.equals("nan"))
                        lineNum.add(-1f);
                    else
                        lineNum.add(Float.parseFloat(ele));
                }
                float[] lineData = new float[lineNum.size()];
                for (int j = 0; j < lineNum.size(); j++)
                    lineData[j] = lineNum.get(j);
                data.add(lineData);
                line = br.readLine();
            }
            float[][] res = new float[data.size()][data.get(0).length];
            for (int i = 0; i < data.size(); i++)
                res[i] = data.get(i);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <E> void saveFile(E[][] data, String path, boolean writeHead) {
        String delimiter = " ";
        try {
            File file = new File(path);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            if (writeHead)
                writer.write("" + data.length + delimiter + data[0].length + '\n');
            for (E[] datum : data) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < data[0].length; j++) {
                    line.append(datum[j]).append(delimiter);
                }
                line.replace(line.length() - 1, line.length(), "\n");
                writer.write(String.valueOf(line));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] readAttribute(File file) {
        List<String> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (null != line) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    res.add(part.trim());
                }
                line = br.readLine();
            }
            String[] attributes = new String[res.size()];
            return res.toArray(attributes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

    }

}
