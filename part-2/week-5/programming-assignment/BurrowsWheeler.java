import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BurrowsWheeler {

//    private int first;
//    private char[] tArr;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String inp = BinaryStdIn.readString();
        int len = inp.length();
        CircularSuffixArray csa = new CircularSuffixArray(inp);

        for (int i = 0; i < len; i++) {
            if (csa.index(i) == 0)
                BinaryStdOut.write(i);
        }

        for (int i = 0; i < len; i++) {
            int idx = csa.index(i);
            BinaryStdOut.write(inp.charAt((idx - 1 + len) % len), 8);
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();

        // construct next[] array
        Map<Character, Queue<Integer>> tCharToIdx = new HashMap<>();
        for (int i = 0; i < t.length; i++) {
            if (!tCharToIdx.containsKey(t[i]))
                tCharToIdx.put(t[i], new Queue<>());

            tCharToIdx.get(t[i]).enqueue(i);
        }

        Arrays.sort(t);
        int[] next = new int[t.length];
        for (int i = 0; i < t.length; i++) {
            next[i] = tCharToIdx.get(t[i]).dequeue();
        }

        // invert the message
        for (int i = 0; i < t.length; i++) {
            BinaryStdOut.write(t[first], 8);
            first = next[first];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Require exactly 1 argument.");
            return;
        }

        if (!args[0].equals("+") && !args[0].equals("-")) {
            System.out.println("Argument must be either + or -");
            return;
        }

        if (args[0].equals("-")) {
            BurrowsWheeler.transform();
        } else {
            BurrowsWheeler.inverseTransform();
        }
    }
}
