import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Board {

    private final int[][] tiles;
    private static final int[][] directions = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[][] cloneTitlesArray(int[][] target) {
        int[][] res = new int[target.length][target.length];
        for (int i = 0; i < target.length; ++i) {
            res[i] = target[i].clone();
        }
        return res;
    }

    public Board(int[][] tiles) {
        this.tiles = cloneTitlesArray(tiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(tiles.length);
        for (int[] tileRow : tiles) {
            res.append("\n");
            res.append(tileRow[0]);
            for (int j = 1; j < tiles.length; ++j) {
                res.append(" ");
                res.append(tileRow[j]);
            }
        }
        return res.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int res = 0;
        int curID = 1;
        for (int[] tileRow : tiles) {
            for (int j = 0; j < tiles.length; ++j) {
                if (tileRow[j] != curID) res++;
                curID++;
            }
        }
        res--;
        return res;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int res = 0;
        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles.length; ++j) {
                int val = tiles[i][j];
                if (val == 0) continue;
                res += (Math.abs(i - ((val - 1) / tiles.length)));
                res += (Math.abs(j - ((val - 1) % tiles.length)));
            }
        }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board board = (Board) y;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> res = new ArrayList<>();

        int zeroRow = -1, zeroCol = -1;
        for (int i = 0; i < tiles.length; ++i) {
            boolean founded = false;
            for (int j = 0; j < tiles.length; ++j) {
                if (tiles[i][j] == 0) {
                    founded = true;
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
            if (founded) break;
        }

        for (int[] direction : directions) {
            int r = zeroRow + direction[0];
            int c = zeroCol + direction[1];

            if (r >= 0 && r < tiles.length && c >= 0 && c < tiles.length) {
                Board neighbor = new Board(tiles);
                int[][] newTiles = neighbor.tiles;
                newTiles[zeroRow][zeroCol] = tiles[r][c];
                newTiles[r][c] = tiles[zeroRow][zeroCol];
                res.add(neighbor);
            }
        }

        return res;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard = new Board(tiles);
        int[][] twinArr = twinBoard.tiles;
        if (twinArr[0][0] == 0 || twinArr[0][1] == 0) {
            twinArr[1][0] = tiles[1][1];
            twinArr[1][1] = tiles[1][0];
        } else {
            twinArr[0][0] = tiles[0][1];
            twinArr[0][1] = tiles[0][0];
        }
        return twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // TEST: toString()
        Board testBoard1 = new Board(
                new int[][]{{1, 0, 3}, {4, 2, 5}, {7, 8, 6}}
        );
        System.out.println(testBoard1);

        // TEST: dimension()
        System.out.println(testBoard1.dimension());

        // TEST: hamming() and manhattan()
        Board testBoard2 = new Board(
                new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}}
        );
        System.out.println(testBoard2.hamming());
        System.out.println(testBoard2.manhattan());

        // TEST: equals()
        Board testBoard3 = new Board(
                new int[][]{{1, 0, 3}, {4, 2, 5}, {7, 8, 6}}
        );
        System.out.println(testBoard1.equals(testBoard3));
        System.out.println(testBoard1.equals(testBoard2));

        // TEST: neighbors()
        Iterable<Board> neighbors = testBoard3.neighbors();
        for (Board board : neighbors) {
            System.out.println(board);
        }

        // TEST: twin()
        System.out.println(testBoard3.twin());
    }
}
