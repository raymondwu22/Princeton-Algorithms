import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int[][] blocks;
    private final int N;

    public Board(int[][] blocks) {
        N = blocks.length;
        this.blocks = new int[N][];
        for (int i = 0; i < N; i++) {
            this.blocks[i] = Arrays.copyOf(blocks[i], N);
        }
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int count = 0;
        int inPlace = 1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != inPlace++) {
                    count++;
                }

                if (inPlace == N * N) {
                    break;
                }
            }
        }

        return count;
    }

    public int manhattan() {
        int row = 0, col = 0;
        int inPlace = 1;
        int sum = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0) {
                    continue;
                }
                row = blocks[i][j] / N;
                col = blocks[i][j] % N;
                if (blocks[i][j] != 0 && col == 0) {
                    row = blocks[i][j] / N - 1;
                    col = blocks[i][j] - row * N;
                }
                sum += Math.abs(row - i) + Math.abs(col - 1 - j);
            }
        }

        return sum;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        Board twinBoard = new Board(blocks);

        int getRow = 0;
        int getCol = 0;
        if (blocks[getRow][getCol] == 0) {
            getRow++;
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != blocks[getRow][getCol] && blocks[i][j] != 0) {
                    twinBoard.exch(twinBoard, i, j, getRow, getCol);
                    return twinBoard;
                }
            }
        }
        return twinBoard;
    }

    private void exch(Board board, int i1, int j1, int i2, int j2) {
        int temp = board.blocks[i1][j1];
        board.blocks[i1][j1] = board.blocks[i2][j2];
        board.blocks[i2][j2] = temp;
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }

        Board that = (Board) y;
        if (this.N != that.N) {
            return false;
        }
        if (!Arrays.deepEquals(this.blocks, that.blocks)) {
            return false;
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<>();

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (blocks[row][col] == 0) {
                    if (col + 1 <= N - 1) {
                        Board temp = new Board(blocks);

                        exch(temp, row, col, row, col + 1);
                        q.enqueue(temp);
                    }

                    if (col - 1 >= 0) {
                        Board temp = new Board(blocks);

                        exch(temp, row, col, row, col - 1);
                        q.enqueue(temp);
                    }

                    if (row + 1 <= N - 1) {
                        Board temp = new Board(blocks);

                        exch(temp, row, col, row + 1, col);
                        q.enqueue(temp);
                    }

                    if (row - 1 >= 0) {
                        Board temp = new Board(blocks);

                        exch(temp, row, col, row - 1, col);
                        q.enqueue(temp);
                    }
                    return q;
                }
            }
        }
        return q;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(N + "\n");

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        int[][] test = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board t = new Board(test);
//        System.out.println(t);
//        System.out.println(t.hamming());
//        System.out.println(t.manhattan());
        System.out.println(t.neighbors());
    }

}
