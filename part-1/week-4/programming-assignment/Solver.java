import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public final class Solver {

    private final LinkedList<Board> solutions;

    private static class Node implements Comparable<Node> {
        private final Board board;
        private final int numMoves;
        private final Node prevNode;
        private final int priorityHamming;
        private final int priorityManhattan;

        public Node(Board board, int numMoves, Node prevNode) {
            this.board = board;
            this.numMoves = numMoves;
            this.prevNode = prevNode;
            this.priorityHamming = numMoves + board.hamming();
            this.priorityManhattan = numMoves + board.manhattan();
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.priorityManhattan, other.priorityManhattan);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> pqTwin = new MinPQ<>();
        pq.insert(new Node(initial, 0, null));
        pqTwin.insert(new Node(initial.twin(), 0, null));

        Node curNode;
        Node curNodeTwin;
        do {
            curNode = pq.delMin();
            curNodeTwin = pqTwin.delMin();

            // add 'neighbors' Nodes
            for (Board neighbor : curNode.board.neighbors()) {
                if (curNode.prevNode != null && neighbor.equals(curNode.prevNode.board)) continue;
                pq.insert(new Node(neighbor, curNode.numMoves + 1, curNode));
            }
            for (Board neighborTwin : curNodeTwin.board.neighbors()) {
                if (curNodeTwin.prevNode != null && neighborTwin.equals(curNodeTwin.prevNode.board)) continue;
                pqTwin.insert(new Node(neighborTwin, curNodeTwin.numMoves + 1, curNodeTwin));
            }
        } while (!curNode.board.isGoal() && !curNodeTwin.board.isGoal());

        solutions = new LinkedList<>();
        if (curNode.board.isGoal()) { // unsolvable
            while (curNode != null) {
                solutions.addFirst(curNode.board);
                curNode = curNode.prevNode;
            }
        }
    }



    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return !solutions.isEmpty();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solutions.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return (isSolvable()) ? solutions : null;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
