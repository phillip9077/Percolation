import edu.princeton.cs.algs4.QuickUnionUF;

public class Percolation {

    private int[][] grid;
    private QuickUnionUF unionUF;
    private int openSites;
    private final int n;

    // creates n-by-n grid, with **all sites initially blocked**
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be less than or equal to 0");
        }
        this.grid = new int[n][n];
        // last two indices are the virtual sites
        this.unionUF = new QuickUnionUF(n * n + 2);
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
        if (!this.isOpen(row, col)) {
            this.grid[row-1][col-1] = 1;
            this.openSites++;
            if (row == 1) {
                this.openTopHelper(col);
            } else if (row == this.n) {
                this.openBotHelper(col);
            } else {
                this.openHelper(row, col);
            }
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
        // TODO: 1. Figure out this logic
        if (row < 1 || row > this.n) {
            throw new IllegalArgumentException("Row number cannot be less than 1 or greater than n");
        } else if (col < 1 || col > this.n) {
            throw new IllegalArgumentException("Col number cannot be less than 1 or greater than n");
        }
        // if an OPEN site at (row, col) is connected to any site in the top row,
        // then it is a full site.
        if (!this.isOpen(row, col)) {
            return false;
        }
        // check for a connection between all top sites and the open site at (row, col)
        int currSite = this.getProperSite(row, col);
        for (int i = 1; i <= this.n; i++) {
            int topSite = this.getProperSite(0, i);
            if (this.unionUF.find(currSite) == topSite) {
                return true;
            }
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

    private void openTopHelper(int col) {
        // connecting the current site to the virtual top site
        int currentSite = this.getProperSite(1, col);
        int virtualTopSite = this.unionUF.find(this.n * this.n);
        this.unionUF.union(currentSite, virtualTopSite);
    }

    private void openBotHelper(int col) {
        // connecting the current site to the virtual bottom site
        int currentSite = this.getProperSite(this.n, col);
        int virtualBotSite = this.unionUF.find((this.n * this.n) + 1);
        this.unionUF.union(currentSite, virtualBotSite);
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
        // if the left site is open
        if (currSite != leftSite && this.isOpen(row, col - 1)) {
            this.unionUF.union(currSite, leftSite);
        }
        // if the right site is open
        if (currSite != rightSite && this.isOpen(row, col+1)) {
            this.unionUF.union(currSite, rightSite);
        }
        // if the top site is open
        if (this.isOpen(row - 1, col)) {
            this.unionUF.union(currSite, topSite);
        }
        // if the bottom site is open
        if (this.isOpen(row + 1, col)) {
            this.unionUF.union(currSite, botSite);
        }
        // if no adjacent sites are open then nothing happens
    }

    // 1-indexed
    private int getProperSite(int row, int col) {
        // formula for converting 2D row and col index to 1D index is (N * rowNum) + col
        int rowNum = row - 1;
        int colNum = col - 1;
        if (rowNum < 0) {
            // if it's the top row, return the top virtual site, which is the penultimate value in
            // the UF data structure
            return this.unionUF.find(this.n * this.n);
        } else if (rowNum > this.n) {
            // if it's the bottom row, return the bottom virtual site, which is the last value in
            // the UF data structure
            return this.unionUF.find((this.n * this.n) + 1);
        } else if (colNum < 0) {
            // if col is less than the first col index, just use the first col index of 0
            return this.n * rowNum;
        } else if (colNum > this.n - 1) {
            // if col is greater than the last col index, just use thee last col index of n - 1
            return (this.n * rowNum) + (this.n - 1);
        }
        // if the indices are in range, just return the normal converted 1D index
        return (this.n * rowNum) + colNum;
    }

    // test client (optional)
    public static void main(String[] args) {
    }

}
