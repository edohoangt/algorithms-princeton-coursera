import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FastCollinearPoints {

    private LineSegment[] segments = null;

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException();

        for (Point point : points) { // check null point
            if (point == null) throw new IllegalArgumentException();
        }

        Point[] copiedPoints = Arrays.copyOf(points, points.length);
        List<LineSegment> res = new ArrayList<>();
        Arrays.sort(copiedPoints); // 1st sorting with a Stable Sort afterwards helps order all points which have the same slope w.r.t current point

        Point prevPoint = null;
        for (Point curPoint : copiedPoints) {
            if (prevPoint != null && curPoint.compareTo(prevPoint) == 0) {
                throw new IllegalArgumentException();
            }
            prevPoint = curPoint;

            Point[] slopeOrderedPoints = Arrays.copyOf(copiedPoints, copiedPoints.length);
            Arrays.sort(slopeOrderedPoints, curPoint.slopeOrder());

            double prevSlope = Double.NEGATIVE_INFINITY;
            int startSlopeID = 0;
            for (int i = 1; i < slopeOrderedPoints.length; ++i) {
                double curSlope = curPoint.slopeTo(slopeOrderedPoints[i]);

                if (Double.compare(curSlope, prevSlope) != 0) {
                    if (i - startSlopeID >= 3) {
                        if (curPoint.compareTo(slopeOrderedPoints[startSlopeID]) <= 0) {
                            res.add(new LineSegment(curPoint, slopeOrderedPoints[i - 1]));
                        }
                    }
                    startSlopeID = i;
                } else if (i == slopeOrderedPoints.length - 1) {
                    if (i - startSlopeID >= 2) {
                        if (curPoint.compareTo(slopeOrderedPoints[startSlopeID]) <= 0) {
                            res.add(new LineSegment(curPoint, slopeOrderedPoints[i]));
                        }
                    }
                }
                prevSlope = curSlope;
            }
        }

        segments = res.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {                 // the number of line segments
        return segments.length;
    }

    public LineSegment[] segments() {               // the line segments
        return Arrays.copyOf(segments, segments.length);
    }

}
