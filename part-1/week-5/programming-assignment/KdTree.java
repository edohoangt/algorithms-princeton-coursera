import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class TreeNode implements Comparable<TreeNode> {

        public TreeNode(Point2D point, boolean color) {
            this.point = point;
            this.color = color;
        }

        private Point2D point;
        private TreeNode left, right;
        private boolean color; // color of the node

        @Override
        public int compareTo(TreeNode that) {
            if (this.color == RED) {
                return Double.compare(this.point.x(), that.point.x());
            }
            return Double.compare(this.point.y(), that.point.y());
        }
    }

    private TreeNode root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (root == null) {
            root = new TreeNode(p, RED);
            size++;
            return;
        }
        if (contains(p)) return;

        TreeNode prev = null, cur = root;
        TreeNode newNode;
        while (cur != null) {
            newNode = new TreeNode(p, cur.color);
            prev = cur;
            if (newNode.compareTo(cur) < 0)
                cur = cur.left;
            else cur = cur.right;
        }
        newNode = new TreeNode(p, !prev.color);
        if (prev.compareTo(newNode) > 0) prev.left = newNode;
        else prev.right = newNode;
        size++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        TreeNode cur = root;
        TreeNode newNode;
        while (cur != null) {
            newNode = new TreeNode(p, cur.color);
            if (newNode.compareTo(cur) > 0) cur = cur.right;
            else if (newNode.compareTo(cur) < 0) cur = cur.left;
            else {
                if (newNode.point.x() == cur.point.x() && newNode.point.y() == cur.point.y()) return true;
                else cur = cur.right;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        // TODO: draw the illustration rectangle
    }

    private void rangeHelper(TreeNode cur, RectHV rect, List<Point2D> res) {
        // base case
        if (cur == null) return;

        // check if the point lies inside 'rect'
        if (rect.xmin() <= cur.point.x() && cur.point.x() <= rect.xmax()
            && rect.ymin() <= cur.point.y() && cur.point.y() <= rect.ymax())
            res.add(cur.point);

        if (cur.color == RED) { // compare x
            if (cur.point.x() > rect.xmax()) {
                rangeHelper(cur.left, rect, res);
            } else if (cur.point.x() <= rect.xmin()) {
                rangeHelper(cur.right, rect, res);
            } else { // xmin < cur.x <= xmax
                rangeHelper(cur.left, rect, res);
                rangeHelper(cur.right, rect, res);
            }
        } else {
            if (cur.point.y() > rect.ymax()) {
                rangeHelper(cur.left, rect, res);
            } else if (cur.point.y() <= rect.ymin()) {
                rangeHelper(cur.right, rect, res);
            } else {
                rangeHelper(cur.left, rect, res);
                rangeHelper(cur.right, rect, res);
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        List<Point2D> res = new ArrayList<>();
        TreeNode cur = root;
        rangeHelper(cur, rect, res);
        return res;
    }

    /* TODO:
        There is a mistake when we can prune a subtree in advance without traversing it, by comparing the current min
        with the distance between the target and the CORNER of the RECTANGLE corresponding to current node investigated,
        not just a side like we do below.
    */
    private TreeNode nearestHelper(TreeNode cur, Point2D target, TreeNode curMin) {
        // base case
        if (cur == null) return cur;

        if (cur.point.distanceTo(target) < curMin.point.distanceTo(target)) curMin = cur;

        if (cur.compareTo(new TreeNode(target, cur.color)) > 0) {
            TreeNode left = nearestHelper(cur.left, target, curMin);
            if (left != null && left.point.distanceTo(target) < curMin.point.distanceTo(target)) curMin = left;
            if ((cur.color == RED && curMin.point.distanceTo(target) > Math.abs(cur.point.x() - target.x()))
                    || (cur.color == BLACK && curMin.point.distanceTo(target) > Math.abs(cur.point.y() - target.y()))) {
                TreeNode right = nearestHelper(cur.right, target, curMin);
                if (right != null && right.point.distanceTo(target) < curMin.point.distanceTo(target))
                    curMin = right;
            }
        } else {
            TreeNode right = nearestHelper(cur.right, target, curMin);
            if (right != null && right.point.distanceTo(target) < curMin.point.distanceTo(target)) curMin = right;
            if ((cur.color == RED && curMin.point.distanceTo(target) > Math.abs(cur.point.x() - target.x()))
                    || (cur.color == BLACK && curMin.point.distanceTo(target) > Math.abs(cur.point.y() - target.y()))) {
                TreeNode left = nearestHelper(cur.left, target, curMin);
                if (left != null && left.point.distanceTo(target) < curMin.point.distanceTo(target))
                    curMin = left;
            }
        }
        return curMin;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (isEmpty()) return null;
        TreeNode cur = root;
        return nearestHelper(cur, p, cur).point;
    }

    public static void main(String[] args) {
        KdTree kt = new KdTree();

////        Test 1
//        kt.insert(new Point2D(0.25, 0.3));
//        kt.insert(new Point2D(0.1, 0.5));
//        kt.insert(new Point2D(0.4, 0.7));
//        kt.insert(new Point2D(0.4, 0.1));
//        kt.insert(new Point2D(0.9, 0.2));
//        kt.insert(new Point2D(1, 1));

////        Test 2
//        kt.insert(new Point2D(0.7, 0.2));
//        kt.insert(new Point2D(0.5, 0.4));
//        kt.insert(new Point2D(0.2, 0.3));
//        kt.insert(new Point2D(0.4, 0.7));
//        kt.insert(new Point2D(0.9, 0.6));

//        Test 3
        kt.insert(new Point2D(0.372, 0.497));
        kt.insert(new Point2D(0.564, 0.413));
        kt.insert(new Point2D(0.226, 0.577));
        kt.insert(new Point2D(0.144, 0.179));
        kt.insert(new Point2D(0.083, 0.51));
        kt.insert(new Point2D(0.32, 0.708));
        kt.insert(new Point2D(0.417, 0.362));
        kt.insert(new Point2D(0.862, 0.825));
        kt.insert(new Point2D(0.785, 0.7251));
        kt.insert(new Point2D(0.499, 0.208));

//        System.out.println("Size: " + kt.size());
//        System.out.println("Check 'contains' (0.1, 0.5): " + kt.contains(new Point2D(0.1, 0.5)));
//        System.out.println("Check 'contains' (0.2, 0.5): " + kt.contains(new Point2D(0.2, 0.5)));
//        System.out.println("Nearest point to (0.5, 0.4): " + kt.nearest(new Point2D(0.5, 0.4)));
//        System.out.println("Nearest point to (0.091, 0.598): " + kt.nearest(new Point2D(0.091, 0.598))); // Test 2
//        System.out.println("Nearest point to (0.816, 0.456): " + kt.nearest(new Point2D(0.816, 0.456))); // Test 3
//        System.out.println("Nearest point to (0.67, 0.442): " + kt.nearest(new Point2D(0.67, 0.442))); // Test 3
//        System.out.println("Nearest point to (0.28, 0.93): " + kt.nearest(new Point2D(0.28, 0.93))); // Test 4
        System.out.println("Nearest point to (0.56, 0.73): " + kt.nearest(new Point2D(0.56, 0.73))); // Test 4
//        Iterable<Point2D> points = kt.range(new RectHV(0.3, 0.25, 0.8, 0.8));
//        for (Point2D p : points) {
//            System.out.println(p);
//        }
//        kt.draw();
    }

}
