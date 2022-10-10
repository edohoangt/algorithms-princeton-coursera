import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    // helper to calculate distance to all other nodes
    private int sumDistance(String noun, String[] nounsArr) {
        int res = 0;
        for (String targetNoun: nounsArr) {
            res += wordNet.distance(noun, targetNoun);
        }
        return res;
    }

    public String outcast(String[] nouns) {
        String res = null;
        int maxSumDist = -1;
        for (String noun: nouns) {
            int curSumDist = sumDistance(noun, nouns);
            if (curSumDist > maxSumDist) {
                maxSumDist = curSumDist;
                res = noun;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
