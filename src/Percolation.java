import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final WeightedQuickUnionUF unionUF;
    private int openSites;
    private final int n;

    // creates n-by-n grid, with **all sites initially blocked**
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be less than or equal to 0");
        }
        this.grid = new boolean[n][n];
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
        if (!this.isOpen(row, col)) {
            this.grid[row-1][col-1] = true;
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
        return this.grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > this.n) {
            throw new IllegalArgumentException("Row number cannot be less than 1 or greater than n");
        } else if (col < 1 || col > this.n) {
            throw new IllegalArgumentException("Col number cannot be less than 1 or greater than n");
        }
        // if an OPEN site at (row, col) is connected to the TOP VIRTUAL SITE
        // then it is a full site.
        if (!this.isOpen(row, col)) {
            return false;
        }
        // check for a connection between the top virtual site and the open site at (row, col)
        int currSite = this.getProperSite(row, col);
        int virtualTopSite = this.unionUF.find(this.n * this.n);
        return this.unionUF.find(currSite) == virtualTopSite;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // if the bottom virtual site connects to the top virtual site then the system percolates
        return this.unionUF.find(this.n * this.n + 1) == this.unionUF.find(this.n * this.n);
    }

    // 1-indexed
    private void openTopHelper(int col) {
        // connecting the current site to the virtual top site
        int currSite = this.getProperSite(1, col);
        int virtualTopSite = this.unionUF.find(this.n * this.n);
        this.unionUF.union(currSite, virtualTopSite);
        // connecting right, left, and bottom sites if they are open as well
        int rightSite = this.getProperSite(1, col + 1);
        int leftSite = this.getProperSite(1, col - 1);
        int botSite = this.getProperSite(2, col);
        if (currSite != rightSite && this.isOpen(1, col + 1)) {
            this.unionUF.union(currSite, rightSite);
        }
        if (currSite != leftSite && this.isOpen(1, col - 1)) {
            this.unionUF.union(currSite, leftSite);
        }
        if (this.isOpen(2, col)) {
            this.unionUF.union(currSite, botSite);
        }
    }

    // 1-indexed
    private void openBotHelper(int col) {
        // connecting the current site to the virtual bottom site
        int currSite = this.getProperSite(this.n, col);
        int virtualBotSite = this.unionUF.find((this.n * this.n) + 1);
        this.unionUF.union(currSite, virtualBotSite);
        // connecting right, left, and top sites if they are open as well
        int rightSite = this.getProperSite(this.n, col + 1);
        int leftSite = this.getProperSite(this.n, col - 1);
        int topSite = this.getProperSite(this.n - 1, col);
        if (currSite != rightSite && this.isOpen(this.n, col + 1)) {
            this.unionUF.union(currSite, rightSite);
        }
        if (currSite != leftSite && this.isOpen(this.n, col - 1)) {
            this.unionUF.union(currSite, leftSite);
        }
        if (this.isOpen(this.n - 1, col)) {
            this.unionUF.union(currSite, topSite);
        }
    }

    // 1-indexed
    private void openHelper(int row, int col) {
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
        Percolation perc = new Percolation(5);
        perc.open(1, 1);
        System.out.println(perc.percolates());
        perc.open(2, 1);
        System.out.println(perc.percolates());
        perc.open(3, 1);
        System.out.println(perc.percolates());
        perc.open(4, 1);
        System.out.println(perc.percolates());
        perc.open(5, 1);
        System.out.println(perc.percolates());
    }

}
