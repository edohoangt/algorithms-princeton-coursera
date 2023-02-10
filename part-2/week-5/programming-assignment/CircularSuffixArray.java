import java.util.Arrays;

public class CircularSuffixArray {

    private String s;
    private Integer[] indexArr;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();

        this.s = s;
        this.indexArr = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++) {
            indexArr[i] = i;
        }

        Arrays.sort(indexArr, (Integer s1, Integer s2) -> {
            for (int i = 0; i < s.length(); i++) {
                int i1 = (s1 + i) % s.length();
                int i2 = (s2 + i) % s.length();
                int res = s.charAt(i1) - s.charAt(i2);
                if (res != 0) return res;
            }

            return 0;
        });
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= s.length())
            throw new IllegalArgumentException();

        return indexArr[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++) {
            System.out.print(csa.index(i) + " ");
        }
        System.out.println();
    }
}
