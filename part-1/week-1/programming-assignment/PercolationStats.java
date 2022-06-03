import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private final double mean;
    private final double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        this.trials = trials;
        double[] results = new double[this.trials];

        for (int i = 0; i < this.trials; ++i) {
            Percolation expr = new Percolation(n);

            while (!expr.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!expr.isOpen(row, col)) expr.open(row, col);
            }

            results[i] =  ((double) expr.numberOfOpenSites() / (n * n));
        }

        this.mean = StdStats.mean(results);
        this.stddev = StdStats.stddev(results);
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (this.mean - (1.96 * this.stddev) / Math.sqrt(this.trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (this.mean + (1.96 * this.stddev) / Math.sqrt(this.trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n;
        int trials;

        if (args.length == 0) {
            n = StdIn.readInt();
            trials = StdIn.readInt();
        } else {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }

        PercolationStats stats = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

}