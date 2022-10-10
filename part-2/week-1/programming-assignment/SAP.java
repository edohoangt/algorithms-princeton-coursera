import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class SAP {

    private final Digraph digraph;
    private final HashMap<HashSet<Integer>, Integer[]> cache; // cache for faster pair-wise (v, w) lookups

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("The given graph cannot be null.");

        this.digraph = new Digraph(G); // make an offensive copy of the given reference
        this.cache = new HashMap<>();
    }

    // validate a vertex
    private void validateVertex(int v) {
        if (v < 0 || v >= digraph.V())
            throw new IllegalArgumentException("Vertex " + v + " must be in range 0 to " + digraph.V() + " inclusively.");
    }

    // helper: 2 vertices
    private void sap(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        HashSet<Integer> vertexPair = new HashSet<>();
        vertexPair.add(v);
        vertexPair.add(w);

        // check if vertexPair is already computed
        if (cache.containsKey(vertexPair)) return;

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);

        int minDist = Integer.MAX_VALUE;
        int ancestor = -2;
        for (int cur = 0; cur < digraph.V(); ++cur) {
            if (vPath.hasPathTo(cur) && wPath.hasPathTo(cur)) {
                int distVW = vPath.distTo(cur) + wPath.distTo(cur);
                if (distVW < minDist) {
                    minDist = distVW;
                    ancestor = cur;
                }
            }
        }

        if (minDist == Integer.MAX_VALUE) {
            minDist = -1;
            ancestor = -1;
        }

        cache.put(vertexPair, new Integer[] {minDist, ancestor});
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        sap(v, w);
        HashSet<Integer> vertexPair = new HashSet<>();
        vertexPair.add(v); vertexPair.add(w);
        return cache.get(vertexPair)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        sap(v, w);
        HashSet<Integer> vertexPair = new HashSet<>();
        vertexPair.add(v); vertexPair.add(w);
        return cache.get(vertexPair)[1];
    }

    // validate a subset
    private void validateSubset(Iterable<Integer> subset) {
        for (Integer i: subset) {
            if(i == null)
                throw new IllegalArgumentException("Value in iterable cannot be null.");
            validateVertex(i);
        }
    }

    // helper: 2 subsets
    private int[] sap(Iterable<Integer> vSubset, Iterable<Integer> wSubset) {
        validateSubset(vSubset);
        validateSubset(wSubset);

        int vSize = 0, wSize = 0;
        for (Integer elem: vSubset)
            vSize += 1;
        for (Integer elem: wSubset)
            wSize += 1;
        if (vSize == 0 || wSize == 0) return new int[]{-1, -1};

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, vSubset);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, wSubset);

        int minDist = Integer.MAX_VALUE;
        int ancestor = -2;
        for (int cur = 0; cur < digraph.V(); ++cur) {
            if (vPath.hasPathTo(cur) && wPath.hasPathTo(cur)) {
                int distVW = vPath.distTo(cur) + wPath.distTo(cur);
                if (distVW < minDist) {
                    minDist = distVW;
                    ancestor = cur;
                }
            }
        }

        if (minDist == Integer.MAX_VALUE) {
            minDist = -1;
            ancestor = -1;
        }

        return new int[] {minDist, ancestor};
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        return sap(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        return sap(v, w)[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        /* test sap between 2 subsets
        In in  = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        List<Integer> subset1 = Arrays.asList(13, 23, 24);
        List<Integer> subset2 = Arrays.asList(6, 16, 17);
        assert sap.ancestor(subset1, subset2) == 3;
        assert sap.length(subset1, subset2) == 4;
         */

        /* test sap between 2 vertices
         */
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }

}
