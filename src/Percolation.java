import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] grid;
    private WeightedQuickUnionUF unionUF;
    private int openSites;
    private final int n;

    // creates n-by-n grid, with **all sites initially blocked**
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be less than or equal to 0");
        }
        this.grid = new int[n][n];
        // last two indices are the virtual sites
        this.unionUF = new WeightedQuickUnionUF(n * n + 2);
        this.openSites = 0;
        this.n = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > this.n) {
            throw new IllegalArgumentException("Row number cannot be less than 1 or greater than n");
        } else if (col < 1 || col > this.n) {
            throw new IllegalArgumentException("Col number cannot be less than 1 or greater than n");
        }
        // row and col start at 1, so adjusted to be 0-indexed in the method
        if (!this.isOpen(row,col)) {
            this.grid[row-1][col-1] = 1;
            this.openHelper(row, col);
            this.openSites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > this.n) {
            throw new IllegalArgumentException("Row number cannot be less than 1 or greater than n");
        } else if (col < 1 || col > this.n) {
            throw new IllegalArgumentException("Col number cannot be less than 1 or greater than n");
        }
        return this.grid[row-1][col-1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > this.n) {
            throw new IllegalArgumentException("Row number cannot be less than 1 or greater than n");
        } else if (col < 1 || col > this.n) {
            throw new IllegalArgumentException("Col number cannot be less than 1 or greater than n");
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // if there is a single site in the bottom row that is a full site,
        // then we know the system percolates.
        for (int col = 0; col < this.n; col++) {
            if (this.isFull(n, col)) {
                return true;
            }
        }
        return false;
    }

    // 1-indexed
    private void openHelper(int row, int col) {
        // TODO: Need to make sure the row and col index aren't out of bounds + handle the two
        //  virtual sites.
        int currSite = this.getProperSite(row, col);
        int leftSite = this.getProperSite(row, col - 1);
        int rightSite = this.getProperSite(row, col + 1);
        int topSite = this.getProperSite(row - 1, col);
        int botSite = this.getProperSite(row + 1, col);
        if (this.isOpen(row, col - 1)) {
            // if the left site is open
            this.unionUF.union(currSite, leftSite);
        } else if (this.isOpen(row, col+1)) {
            // if the right site is open
            this.unionUF.union(currSite, rightSite);
        } else if (this.isOpen(row - 1, col)) {
            // if the top site is open
            this.unionUF.union(currSite, topSite);
        } else if (this.isOpen(row + 1, col)) {
            // if the bottom site is open
            this.unionUF.union(currSite, botSite);
        }
        // if no adjacent sites are open then nothing happens
    }

    // 1-indexed
    private int getProperSite(int row, int col) {
        int rowNum = row - 1;
        int colNum = col - 1;
        if (row < 0) {
            // if it's the top row, return the top virtual site
            return this.unionUF.find((this.n * this.n) + 1);
        } else if (rowNum > this.n) {
            return this.unionUF.find((this.n * this.n) + 2);
        } else if (colNum < 0) {
            // if col is less than the first col index, just use the first col index of 0
            return this.n * rowNum;
        } else if (colNum > this.n - 1) {
            // if col is greater than the last col index, just use thee last col index of n - 1
            return (this.n * rowNum) + (this.n - 1);
        }
        // if the indices are in range, just return the normal calculation
        return (this.n * rowNum) + colNum;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(10);
    }

}
