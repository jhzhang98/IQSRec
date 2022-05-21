import skyline.IQSRec;
import skyline.MSPC;
import skyline.Native;
import skyline.SortedFilterSkyline;
import util.Evaluator;
import util.FileHandler;

public class UsageDemo {
    public static void main(String[] args) {
        String path = "data/qws_normal.txt";
        float[][] data = new FileHandler().readFile(path, false);

        String sparsePath = "data/qws_m_rate_0.2_time_0.txt";
        float[][] dataSparse = new FileHandler().readFile(sparsePath, false);

        int[] skylineRight = new SortedFilterSkyline().getSkyline(data);

        // rec size = m * d
        int m = 5;

        Evaluator ev = new Evaluator();

        long start = System.currentTimeMillis();
        Native nat = new Native();
        nat.initData(dataSparse);
        int[] skylineNative = nat.getSkyline(data[0].length * m);
        long end = System.currentTimeMillis();
        System.out.println("native precision: " + ev.precision(skylineRight, skylineNative));
        System.out.println("native cost: " + (end - start) / 1000.0 + "s");

        start = System.currentTimeMillis();
        MSPC mspc = new MSPC();
        mspc.initData(dataSparse);
        int[] skylineMSPC = mspc.getSkyline(data[0].length * m);
        end = System.currentTimeMillis();
        System.out.println("mspc precision: " + ev.precision(skylineRight, skylineMSPC));
        System.out.println("mspc cost: " + (end - start) / 1000.0 + "s");

        start = System.currentTimeMillis();
        IQSRec iqsRec = new IQSRec();
        iqsRec.initData(dataSparse);
        int[] skylineIQSRec = iqsRec.getSkyline(m, 1);
        end = System.currentTimeMillis();
        System.out.println("iqsrec precision: " + ev.precision(skylineRight, skylineIQSRec));
        System.out.println("iqsrec cost: " + (end - start) / 1000.0 + "s");
    }
}
