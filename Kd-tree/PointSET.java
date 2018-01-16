import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        set = new SET<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (set.contains(p)) {
            return;
        } else {
            set.add(p);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        } else {
            return set.contains(p);
        }
    }

    public void draw() {
        for (Point2D p: set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Queue<Point2D> point2DQueue = new Queue<>();
        for (Point2D p: set) {
            if (rect.contains(p)) {
                point2DQueue.enqueue(p);
            }
        }

        return point2DQueue;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }

        double minDistance = Double.MAX_VALUE;
        Point2D copyPoint = null;

        for (Point2D point: set) {
            if (point.distanceTo(p) <= minDistance) {
                minDistance = point.distanceTo(p);
                copyPoint = point;
            }
        }

        return copyPoint;
    }

    public static void main(String[] args) {
        Point2D p1 = new Point2D(0.1, 0.1);
        Point2D p2 = new Point2D(0.9, 0.9);
        Point2D p3 = new Point2D(0.5, 0.5);
        Point2D p4 = new Point2D(0.9, 0.1);
        RectHV rect = new RectHV(0.51, 0.09, 0.91, 0.91);

        PointSET test = new PointSET();
        test.insert(p1);
        test.insert(p2);
        test.insert(p3);
        test.insert(p4);
        System.out.println(test.isEmpty());
        System.out.println(test.nearest(new Point2D(0.8, 0.2)));
        System.out.println();
        Iterable<Point2D> itr = test.range(rect);
        for (Point2D pt : itr)
            System.out.println(pt);
    }
}
