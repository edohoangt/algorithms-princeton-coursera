import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private final int n;
    private final int usetSize;
    private final WeightedQuickUnionUF uset; // for checking percolation
    private final WeightedQuickUnionUF usetFull; // for checking full-ness of a cell
    private final boolean[] openStats; // track open/blocked states of all cells
    private int numOpenSites;

    private boolean inAllowedRange(int id) {
        return (id >= 1) && (id <= this.n);
    }

    private int getId(int row, int col) {
        return (row - 1) * this.n + (col - 1) + 1;
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) { // O(n^2)
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        this.usetSize = this.n * this.n + 2;
        this.uset = new WeightedQuickUnionUF(this.usetSize);
        this.usetFull = new WeightedQuickUnionUF(this.usetSize);
        this.openStats = new boolean[usetSize];
        this.numOpenSites = 0;

        for (int i = 0; i < usetSize; ++i) {
            openStats[i] = false;
        }

        // set the first cell and final cell to be opened
        openStats[0] = true;
        openStats[usetSize - 1] = true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!inAllowedRange(row) || !inAllowedRange(col))
            throw new IllegalArgumentException();

        int id = getId(row, col);

        // check if the cell was already opened
        if (isOpen(row, col)) return;

        // if cell is in the first row or last row
        if (row == 1) {
            uset.union(0, id);
            usetFull.union(0, id);
        }
        if (row == n)
            uset.union(usetSize - 1, id);

        openStats[id] = true;

        // otherwise, union with all other OPENED neighboring cells (in bound)
        if (inAllowedRange(row - 1) && openStats[getId(row - 1, col)]) {
            uset.union(id, getId(row - 1, col));
            usetFull.union(id, getId(row - 1, col));
        }
        if (inAllowedRange(row + 1) && openStats[getId(row + 1, col)]) {
            uset.union(id, getId(row + 1, col));
            usetFull.union(id, getId(row + 1, col));
        }
        if (inAllowedRange(col - 1) && openStats[getId(row, col - 1)]) {
            uset.union(id, getId(row, col - 1));
            usetFull.union(id, getId(row, col - 1));
        }
        if (inAllowedRange(col + 1) && openStats[getId(row, col + 1)]) {
            uset.union(id, getId(row, col + 1));
            usetFull.union(id, getId(row, col + 1));
        }

        numOpenSites += 1;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!inAllowedRange(row) || !inAllowedRange(col))
            throw new IllegalArgumentException();

        int id = getId(row, col);
        return openStats[id];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!inAllowedRange(row) || !inAllowedRange(col))
            throw new IllegalArgumentException();

        int id = getId(row, col);

        return openStats[id] && (usetFull.find(0) == usetFull.find(id));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uset.find(0) == uset.find(usetSize - 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        StdOut.println("test client from Percolation class...");

        Percolation lattice = new Percolation(5);
        lattice.open(1, 1);
        lattice.open(1, 2);
        lattice.open(2, 1);
        lattice.open(2, 4);
        lattice.open(3, 1);
        lattice.open(3, 2);
        lattice.open(4, 2);
        lattice.open(4, 3);
        lattice.open(5, 2);
        lattice.open(5, 4);

        StdOut.println(lattice.isFull(2, 2));
        StdOut.println(lattice.isFull(4, 2));
        StdOut.println(lattice.isFull(2, 4));
        StdOut.println(lattice.isFull(5, 4));

        StdOut.println(lattice.percolates());
        StdOut.println(lattice.numberOfOpenSites());
    }
}