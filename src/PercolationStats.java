import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double numTrials;
    private final double[] thresholdArr;
    private static final double CONFIDENCE_CONST = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N or trials cannot be less than or equal to 0");
        }
        this.numTrials = trials;
        this.thresholdArr = new double[trials];
        while (trials > 0) {
            Percolation perc = new Percolation(n);
            double totalSites = n * n;
            int openSites = 0;
            while(!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                    openSites++;
                }
            }
            trials--;
            this.thresholdArr[trials] = openSites / totalSites;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.thresholdArr);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.thresholdArr);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - ((CONFIDENCE_CONST * this.stddev()) / Math.sqrt(this.numTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + ((CONFIDENCE_CONST * this.stddev()) / Math.sqrt(this.numTrials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, T);
        System.out.println("mean: " + percStats.mean());
        System.out.println("stddev: " + percStats.stddev());
        System.out.println("95% confidence interval: [" + percStats.confidenceLo() + ", "
                + percStats.confidenceHi() + "]");
    }

}
