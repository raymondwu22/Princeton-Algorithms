import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final WeightedQuickUnionUF weightedQuickUnionUFWithoutBottom;
    private final int sideLength;
    private final boolean[] id;
    private int numberOfOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n must be positive.");
        }

        this.sideLength = n;
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        this.weightedQuickUnionUFWithoutBottom = new WeightedQuickUnionUF(n * n + 1);
        this.id = new boolean[n * n + 2];
        this.numberOfOpenSites = 0;
    }

    public void open(int row, int col) {
        if (!inRange(row, col)) {
            throw new java.lang.IllegalArgumentException("This point is not in range.");
        }

        int index = trans2DTo1D(row, col);
        if (!isOpen(row, col)) {
            id[index] = true;
            numberOfOpenSites++;

            connectToOpenNeighbors(row - 1, col, index); // Up
            connectToOpenNeighbors(row + 1, col, index); // Down
            connectToOpenNeighbors(row, col - 1, index); // Left
            connectToOpenNeighbors(row, col + 1, index); // Right
        }
    }

    public boolean isOpen(int row, int col) {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("Error: (row, col) out of index");
        }

        return id[trans2DTo1D(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("Error: (row, col) out of index");
        }

        return weightedQuickUnionUFWithoutBottom.connected(trans2DTo1D(row, col), 0);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return weightedQuickUnionUF.connected(0, sideLength * sideLength +1);
    }

    private int trans2DTo1D(int row, int col) {
        return (row - 1) * sideLength + col;
    }

    private  boolean inRange(int row, int col) {
        if ((row > 0 && row <= sideLength) && (col > 0 && col <= sideLength)) {
            return true;
        }
        return false;
    }

    private void connectToOpenNeighbors(int row, int col, int sourceIDIndex) {
        if (col == 0 || col == sideLength + 1) {
            return;
        }

        if (row == 0) {
            weightedQuickUnionUF.union(0, sourceIDIndex);
            weightedQuickUnionUFWithoutBottom.union(0, sourceIDIndex);
        } else if (row == sideLength + 1) {
            weightedQuickUnionUF.union(sourceIDIndex, sideLength * sideLength + 1);
        } else if (isOpen(row, col)) {
            weightedQuickUnionUF.union(trans2DTo1D(row, col), sourceIDIndex);
            weightedQuickUnionUFWithoutBottom.union(trans2DTo1D(row, col), sourceIDIndex);
        }

    }

    public static void main(String[] args) {

        int n = 200;
        Percolation percolation = new Percolation(n);

        while (!percolation.percolates()) {
            int i = StdRandom.uniform(1, n + 1);
            int j = StdRandom.uniform(1, n + 1);
            percolation.open(i, j);
        }
        int fuck = percolation.numberOfOpenSites();
        System.out.println(percolation.numberOfOpenSites());
        System.out.println((double) fuck / (n * n));
    }
}
