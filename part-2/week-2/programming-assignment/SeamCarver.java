import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private final double BORDER_ENERGY = 1000.0;
    private Picture curPicture;
    private int w;
    private int h;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Argument to constructor cannot be null.");

        Picture newPicture = new Picture(picture);
        setNewPicture(newPicture);
    }

    private void setNewPicture(Picture newPicture) {
        this.curPicture = newPicture;
        this.w = curPicture.width();
        this.h = curPicture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(curPicture);
    }

    // width of current picture
    public int width() {
        return w;
    }

    // height of current picture
    public int height() {
        return h;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateX(x);
        validateY(y);

        if (x == 0 || x == w-1 || y == 0 || y == h-1)
            return BORDER_ENERGY;

        Color colorLeft = curPicture.get(x-1, y);
        Color colorRight = curPicture.get(x+1, y);
        Color colorUp = curPicture.get(x, y-1);
        Color colorDown = curPicture.get(x, y+1);
        int deltaXSquare = calculateDeltaSquare(colorLeft, colorRight);
        int deltaYSquare = calculateDeltaSquare(colorDown, colorUp);
        return Math.sqrt(deltaXSquare + deltaYSquare);
    }

    private int calculateDeltaSquare(Color c1, Color c2) {
        int deltaR = c1.getRed() - c2.getRed();
        int deltaG = c1.getGreen() - c2.getGreen();
        int deltaB = c1.getBlue() - c2.getBlue();
        return deltaR * deltaR + deltaG * deltaG + deltaB * deltaB;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposeImageCW90Degree();
        int[] res = findVerticalSeam();
        for (int i = 0; i < res.length; ++i) {
            res[i] = curPicture.width()-1-res[i];
        }
        transposeImageCW90Degree();
        transposeImageCW90Degree();
        transposeImageCW90Degree();
        return res;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (w == 1) return new int[h];

        double[][] accumWeights = new double[h][w];
        int[][] prevPosition = new int[h][w];
        for (int i = 0; i < w; i++) {
            accumWeights[0][i] = BORDER_ENERGY;
            prevPosition[0][i] = -1;
        }

        for (int r = 1; r < h; ++r) {
            for (int c = 0; c < w; ++c) {
                double prevMin;
                if (c == 0) {
                    prevMin = accumWeights[r-1][c+1];
                    prevPosition[r][c] = c+1;
                }
                else if (c == w-1) {
                    prevMin = accumWeights[r-1][c-1];
                    prevPosition[r][c] = c-1;
                }
                else {
                    prevMin = Math.min(accumWeights[r-1][c-1], accumWeights[r-1][c+1]);
                    prevPosition[r][c] = accumWeights[r-1][c-1] >= accumWeights[r-1][c+1]
                            ? c+1 : c-1;
                }

                if (prevMin > accumWeights[r-1][c]) {
                    prevMin = accumWeights[r-1][c];
                    prevPosition[r][c] = c;
                }

                accumWeights[r][c] = prevMin + this.energy(c, r);
            }
        }

        double minAccumWeight = Double.MAX_VALUE;
        int minPos = -1;
        for (int i = 0; i < w; ++i) {
            if (minAccumWeight > accumWeights[h-1][i]) {
                minAccumWeight = accumWeights[h-1][i];
                minPos = i;
            }
        }

        int[] res = new int[h];
        for (int i = h-1; i >= 0; i--) {
            res[i] = minPos;
            minPos = prevPosition[i][minPos];
        }
        return res;
    }

    private void transposeImageCW90Degree() {
        Picture newPicture = new Picture(h, w);

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                Color curColor = curPicture.get(c, r);
                newPicture.set(h-r-1, c, curColor);
            }
        }
        setNewPicture(newPicture);
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("Given seam cannot be null.");
        validateSeam(seam, false);
        if (h <= 1)
            throw new IllegalArgumentException("Cannot remove seam horizontally.");

        Picture newPicture = new Picture(w, h-1);

        for (int c = 0; c < w; c++) {
            int curRow = 0;
            for (int r = 0; r < h; r++) {
                if (seam[c] == r) continue;
                newPicture.set(c, curRow++, curPicture.get(c, r));
            }
        }
        setNewPicture(newPicture);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("Given seam cannot be null.");
        validateSeam(seam, true);
        if (w <= 1)
            throw new IllegalArgumentException("Cannot remove seam vertically.");

        Picture newPicture = new Picture(w-1, h);

        for (int r = 0; r < h; r++) {
            int curCol = 0;
            for (int c = 0; c < w; c++) {
                if (seam[r] == c) continue;
                newPicture.set(curCol++, r, curPicture.get(c, r));
            }
        }
        setNewPicture(newPicture);
    }

    private void validateX(int x) {
        if (x < 0 || x > w-1)
            throw new IllegalArgumentException("'x' is out of range.");
    }

    private void validateY(int y) {
        if (y < 0 || y > h-1)
            throw new IllegalArgumentException("'y' is out of range.");
    }

    private void validateSeam(int[] seam, boolean isVertical) {
        // check array length
        int desiredLength = isVertical ? h : w;
        if (seam.length != desiredLength)
            throw new IllegalArgumentException("Seam's length is invalid.");

        // ensure individual entry in array is not out of range and two adjacent entries differ at most 1
        for (int i = 0; i < seam.length; i++) {
            if (isVertical) validateX(seam[i]);
            else validateY(seam[i]);
            if (i != seam.length-1 && Math.abs(seam[i] - seam[i+1]) > 1)
                throw new IllegalArgumentException("Two adjacent entries cannot differ more than 1.");
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        System.out.println("Unit testing.");
    }

}
