
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] thresholdResults;
    private int trials;

    public PercolationStats(int n, int trials) { // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        thresholdResults = new double[trials];

        for (int t = 0; t < trials; t++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                percolation.open(row, col);
            }

            thresholdResults[t] = (double) percolation.numberOfOpenSites() / (double) (n * n);
        }
    }

    public double mean() { // sample mean of percolation threshold
        return StdStats.mean(thresholdResults);
    }

    public double stddev() { // sample standard deviation of percolation threshold
        return StdStats.stddev(thresholdResults);
    }

    public double confidenceLo() { // low  endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt((double) trials);
    }

    public double confidenceHi() { // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt((double) trials);
    }

    public static void main(String[] args) { // test client (described below)
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi() + "]");
    }
}
