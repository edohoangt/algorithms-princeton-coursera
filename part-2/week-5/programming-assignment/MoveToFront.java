import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static int indexOf(char c, char[] alpha) {
        for (int i = 0; i < alpha.length; i++) {
            if (alpha[i] == c) return i;
        }
        return -1;
    }

    private static void moveCharToFront(char[] alpha, char c) {
        int idx = indexOf(c, alpha);
        if (idx == -1) return;

        for (int i = idx; i > 0; i--) {
            alpha[i] = alpha[i-1];
        }
        alpha[0] = c;
        return;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] alphabet = new char[256];
        for (int i = 0; i < 256; i++) {
            alphabet[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(indexOf(c, alphabet), 8);
            moveCharToFront(alphabet, c);
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] alphabet = new char[256];
        for (int i = 0; i < 256; i++) {
            alphabet[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int ci = BinaryStdIn.readInt(8);
            char c = alphabet[ci];
            BinaryStdOut.write(c);
            moveCharToFront(alphabet, c);
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
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
            MoveToFront.encode();
        } else {
            MoveToFront.decode();
        }
    }
}
