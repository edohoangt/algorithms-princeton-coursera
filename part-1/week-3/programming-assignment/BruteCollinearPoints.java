import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) throw new IllegalArgumentException();

        for (int i = 0; i < points.length; ++i) { // check null point
            if (points[i] == null) throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; ++i) { // check duplicate points
            for (int j = i + 1; j < points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }

        List<LineSegment> res = new ArrayList<>();
        Point[] copiedPoints = Arrays.copyOf(points, points.length);

        int len = points.length;

        for (int i = 0; i < len; ++i) {
            for (int j = i + 1; j < len; ++j) {
                for (int k = j + 1; k < len; ++k) {
                    for (int l = k + 1; l < len; ++l) {
                        double slopeIJ = copiedPoints[i].slopeTo(copiedPoints[j]);
                        double slopeJK = copiedPoints[j].slopeTo(copiedPoints[k]);
                        double slopeKL = copiedPoints[k].slopeTo(copiedPoints[l]);
                        if (slopeIJ == slopeJK && slopeJK == slopeKL)
                            res.add(maximalLineSegment(copiedPoints[i], copiedPoints[j], copiedPoints[k], copiedPoints[l]));
                    }
                }
            }
        }

        segments = res.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {                 // the number of line segments
        return segments.length;
    }

    private LineSegment maximalLineSegment(Point p1, Point p2, Point p3, Point p4) {
        Point lowPoint, highPoint;
        if (p1.compareTo(p2) > 0 && p1.compareTo(p3) > 0 && p1.compareTo(p4) > 0)
            highPoint = p1;
        else if (p2.compareTo(p1) > 0 && p2.compareTo(p3) > 0 && p2.compareTo(p4) > 0)
            highPoint = p2;
        else if (p3.compareTo(p2) > 0 && p3.compareTo(p1) > 0 && p3.compareTo(p4) > 0)
            highPoint = p3;
        else highPoint = p4;

        if (p1.compareTo(p2) < 0 && p1.compareTo(p3) < 0 && p1.compareTo(p4) < 0)
            lowPoint = p1;
        else if (p2.compareTo(p1) < 0 && p2.compareTo(p3) < 0 && p2.compareTo(p4) < 0)
            lowPoint = p2;
        else if (p3.compareTo(p2) < 0 && p3.compareTo(p1) < 0 && p3.compareTo(p4) < 0)
            lowPoint = p3;
        else lowPoint = p4;

        return new LineSegment(lowPoint, highPoint);
    }

    public LineSegment[] segments() {               // the line segments
        return Arrays.copyOf(segments, segments.length);
    }

}
