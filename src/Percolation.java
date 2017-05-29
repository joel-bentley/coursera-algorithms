
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] grid;
    private int n;
    private WeightedQuickUnionUF unionFind;
    private int[][] neighbors = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };

    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        grid = new boolean[n * n + 1]; // grid[1] to grid[n*n] (grid[0] is unused)
        this.n = n;

        // create union-find data structure
        unionFind = new WeightedQuickUnionUF(n * n + 2);
        int topVirtualSite = 0;
        int bottomVirtualSite = n * n + 1;

        // connect top virtual site to top row, and bottom virtual site to bottom row
        for (int i = 1; i <= n; i++) {
            int topRowGridLocation = xyTo1D(1, i);
            int bottomRowGridLocation = xyTo1D(n, i);
            unionFind.union(topVirtualSite, topRowGridLocation);
            unionFind.union(bottomVirtualSite, bottomRowGridLocation);
        }
    }

    private void checkBounds(int row, int col) {
        if (row < 1 || row > n)
            throw new IndexOutOfBoundsException("row index out of bounds");
        if (col < 1 || col > n)
            throw new IndexOutOfBoundsException("col index out of bounds");
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * n + col;
    }

    public void open(int row, int col) { // open site (row, col) if it is not open already
        checkBounds(row, col);
        int gridLocation = xyTo1D(row, col);
        if (!grid[gridLocation]) {
            grid[gridLocation] = true;

            for (int[] neighborOffset : neighbors) {
                int neighborRow = row + neighborOffset[0];
                int neighborCol = col + neighborOffset[1];
                if (neighborRow >= 1 && neighborRow <= n && neighborCol >= 1 && neighborCol <= n) {
                    int neighborLocation = xyTo1D(neighborRow, neighborCol);
                    if (grid[neighborLocation]) {
                        unionFind.union(gridLocation, neighborLocation);
                    }
                }
            }
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        checkBounds(row, col);
        int gridLocation = xyTo1D(row, col);
        return grid[gridLocation];
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        checkBounds(row, col);
        int gridLocation = xyTo1D(row, col);
        return grid[gridLocation] && unionFind.connected(gridLocation, 0);
    }

    public int numberOfOpenSites() { // number of open sites
        int count = 0;
        for (int i = 0; i < n * n + 1; i++) {
            if (grid[i]) {
                count++;
            }
        }
        return count;
    }

    public boolean percolates() { // does the system percolate?
        if (n == 1 && !grid[1])
            return false; // handle special case

        return unionFind.connected(0, n * n + 1);
    }

    public static void main(String[] args) { // test client (optional)
        int n = Integer.parseInt(args[0]);
        Percolation percolation = new Percolation(n);
        System.out.println(percolation.percolates());
        // percolation.open(1, 1);
        // System.out.println(percolation.isFull(1, 2));
    }
}
