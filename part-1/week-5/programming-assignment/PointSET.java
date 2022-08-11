import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private final Set<Point2D> tSet;

    // construct an empty set of points
    public PointSET() {
        tSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return tSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return tSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        tSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return tSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        Point2D origin = new Point2D(0, 0);
        for (Point2D point : tSet)
            point.drawTo(origin);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        List<Point2D> res = new ArrayList<>();
        for (Point2D point : tSet) {
            if (point.x() >= rect.xmin() && point.x() <= rect.xmax() && point.y() >= rect.ymin() && point.y() <= rect.ymax())
                res.add(point);
        }
        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (tSet.isEmpty()) return null;

        Point2D res = null;
        for (Point2D point : tSet) {
            if (res == null) res = point;
            else if (p.distanceTo(res) > p.distanceTo(point)) res = point;
        }
        return res;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET ps = new PointSET();

        ps.insert(new Point2D(0.25, 0.3));
        ps.insert(new Point2D(0.1, 0.5));
        ps.insert(new Point2D(0.4, 0.7));
        ps.insert(new Point2D(0.4, 0.1));
        ps.insert(new Point2D(0.9, 0.2));
        ps.insert(new Point2D(1, 1));

        System.out.println("Size: " + ps.size());
        System.out.println("Check 'contains' (0.1, 0.5): " + ps.contains(new Point2D(0.1, 0.5)));
        System.out.println("Check 'contains' (0.2, 0.5): " + ps.contains(new Point2D(0.2, 0.5)));
        System.out.println("Nearest point to (0.5, 0.4): " + ps.nearest(new Point2D(0.5, 0.4)));
        Iterable<Point2D> points = ps.range(new RectHV(0.3, 0.25, 0.8, 0.8));
        for (Point2D p : points) {
            System.out.println(p);
        }
        ps.draw();
    }
}