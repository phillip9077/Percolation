import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // TODO: Throw an IllegalArgumentException if any argument to open(), isOpen(), or isFull() is outside its
    //  prescribed range. Throw an IllegalArgumentException in the constructor if n ≤ 0.

    private WeightedQuickUnionUF unionUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be less than or equal to 0");
        }
        this.unionUF = new WeightedQuickUnionUF(n * n);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return -1;
    }

    // does the system percolate?
    public boolean percolates() {
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(10);
    }

}
