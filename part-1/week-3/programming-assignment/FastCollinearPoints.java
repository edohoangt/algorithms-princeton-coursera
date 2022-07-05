import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// TODO: Find a way to identify and do not add SUB-segment to the result

public class FastCollinearPoints {

    private final Point[] points;
    private int numberOfSegments;

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException();

        for (Point point : points) { // check null point
            if (point == null) throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; ++i) { // check duplicate points
            for (int j = i + 1; j < points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }

        this.points = points;
        this.numberOfSegments = -1;
    }

    public int numberOfSegments() {                 // the number of line segments
        if (this.numberOfSegments == -1) {
            LineSegment[] res = segments();
            this.numberOfSegments = res.length;
        }

        return this.numberOfSegments;
    }

    public LineSegment[] segments() {               // the line segments
        List<Point[]> res = new ArrayList<>();
        List<Integer> segSize = new ArrayList<>();
        int len = points.length;

        for (int i = 0; i < len; ++i) {
            Point cur = points[i];
            Point[] copiedPoints = Arrays.copyOf(points, len);
            Arrays.sort(copiedPoints, cur.slopeOrder());

            int leftId = 1;
            for (int rightId = 3; rightId < len; ++rightId) {
                double slope1 = cur.slopeTo(copiedPoints[rightId]);
                double slope2 = cur.slopeTo(copiedPoints[rightId - 1]);
                double slope3 = cur.slopeTo(copiedPoints[rightId - 2]);
                if (slope1 == slope2 && slope2 == slope3) { // 3 slopes are equal
                    if (rightId == len - 1) {
                        // process the group from <leftId> to <rightId>
                        processPossibleSegment(copiedPoints, leftId, rightId, segSize, res);
                    }
                    continue;
                }
                if (rightId - leftId >= 3) {
                    // process the group from <leftId> to <rightId - 1>
                    processPossibleSegment(copiedPoints, leftId, rightId - 1, segSize, res);
                    // update leftId
                    leftId = rightId - 1;
                    continue;
                }
                leftId++;
            }
        }

        List<LineSegment> segments = new ArrayList<>();
        for (Point[] line : res) {
            segments.add(new LineSegment(line[0], line[1]));
        }
        return segments.toArray(new LineSegment[0]);
    }

    private void processPossibleSegment(Point[] copiedPoints, int leftId, int rightId, List<Integer> segSize, List<Point[]> res) {
        // process the group from <leftId> to <rightId>
        Point[] pointsOnSegment = Arrays.copyOfRange(copiedPoints, leftId, rightId + 2);
        pointsOnSegment[pointsOnSegment.length - 1] = copiedPoints[0];
        Arrays.sort(pointsOnSegment);
        Point[] segment = {pointsOnSegment[0], pointsOnSegment[pointsOnSegment.length - 1]};

        // check if this segment is already in <res>
        double segmentSlope = segment[0].slopeTo(segment[1]);
        Point[] lineToRemove = null;
        Iterator<Integer> segSizeItr = segSize.iterator();
        for (Point[] line : res) {
            double curSlope = line[0].slopeTo(line[1]);
            double lineToCurSlope = line[0].slopeTo(segment[0]);
            int curSegSize = segSizeItr.next();
            if (segmentSlope == curSlope && (line[0] == segment[0] || (segmentSlope == lineToCurSlope || segmentSlope == -lineToCurSlope))) {
                if (pointsOnSegment.length >= curSegSize) {
                    lineToRemove = line;
                    break;
                }
            }
        }
        if (lineToRemove != null) {
            res.remove(lineToRemove);
            segSizeItr.remove();
        }
        res.add(segment);
        segSize.add(pointsOnSegment.length);
    }
}