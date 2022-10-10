import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {

    private final HashMap<String, Bag<Integer>> nounToIds = new HashMap<>();
    private final List<String> idToSynset = new ArrayList<>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        int numVertices = 0;

        // load file 'synsets'
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] values = line.split(",");
            int id = Integer.parseInt(values[0]);
            idToSynset.add(values[1]);
            String[] nounsInSynSet = values[1].split(" ");

            for (String noun: nounsInSynSet) {
                if (nounToIds.get(noun) != null) {
                    Bag<Integer> bag = nounToIds.get(noun);
                    bag.add(id);
                } else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    nounToIds.put(noun, bag);
                }
            }

            numVertices += 1;
        }

        // load file 'hypernyms'
        Digraph digraph = new Digraph(numVertices);
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] values = line.split(",");

            for (int i = 1; i < values.length; ++i) { // build graph
                digraph.addEdge(Integer.parseInt(values[0]), Integer.parseInt(values[i]));
            }
        }

        // check acyclic property
        DirectedCycle cycle = new DirectedCycle(digraph);
        if(cycle.hasCycle())
            throw new IllegalArgumentException("Graph must be acyclic.");

        // check if graph has more than 1 root
        int numRoots = 0;
        for (int v = 0; v < digraph.V(); ++v) {
            if (digraph.outdegree(v) == 0) numRoots += 1;
        }
        if(numRoots != 1)
            throw new IllegalArgumentException("Graph must be single-rooted.");

        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        return this.nounToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("Noun cannot be null.");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("The given noun is not in WordNet.");

        Bag<Integer> idsA = nounToIds.get(nounA);
        Bag<Integer> idsB = nounToIds.get(nounB);

        return sap.length(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("Noun cannot be null.");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("The given noun is not in WordNet.");

        Bag<Integer> idsA = nounToIds.get(nounA);
        Bag<Integer> idsB = nounToIds.get(nounB);

        return idToSynset.get(sap.ancestor(idsA, idsB));
    }

    // do unit testing of this class
    public static void main(String[] args) {
//        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");

    }
}


