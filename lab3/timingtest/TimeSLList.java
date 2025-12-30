package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Integer> opCounts = new AList<>();
        AList<Double> times = new AList<>();
        int TestSize[] = new  int[]{1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};

        SLList<Integer> SL = new SLList<>();
        for (int i = 0; i < 8; ++i){
            for (int j = 0; j < TestSize[0]; j++) {
                SL.addLast(1);
            }

            Stopwatch stopwatch = new Stopwatch();
            for(int j = 0; j < TestSize[i] ; j++){
                int N = SL.getLast();
            }
            double timeInSeconds = stopwatch.elapsedTime();
            Ns.addLast(TestSize[i]);
            times.addLast(timeInSeconds);
            opCounts.addLast(TestSize[i]);
        }
        printTimingTable(Ns, times, opCounts);

    }

}
