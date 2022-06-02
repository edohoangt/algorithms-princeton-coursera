import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {

    public static void main(String[] args) {
        String res = "";
        int n = 1;

        while (!StdIn.isEmpty()) {
            String cur = StdIn.readString();
            if(StdRandom.bernoulli((double) 1 / n)) res = cur;
            n++;
        }

        StdOut.println(res);
    }

}

// 1. AT A RANDOM PLACE
// javac -classpath F:\MY-WORKING-SPACE\06-JUNE\programming\algorithms-coursera\week-0\src\algs4.jar F:\MY-WORKING-SPACE\06-JUNE\programming\algorithms-coursera\week-0\src\RandomWord.java
// java -classpath F:\MY-WORKING-SPACE\06-JUNE\programming\algorithms-coursera\week-0\src\algs4.jar;F:\MY-WORKING-SPACE\06-JUNE\programming\algorithms-coursera\week-0\src RandomWord

// 2. AT CURRENT WORKING DIRECTORY
// javac -classpath F:\MY-WORKING-SPACE\06-JUNE\programming\algorithms-coursera\week-0\src\algs4.jar RandomWord.java
// java -classpath F:\MY-WORKING-SPACE\06-JUNE\programming\algorithms-coursera\week-0\src\algs4.jar;. RandomWord < test.txt
