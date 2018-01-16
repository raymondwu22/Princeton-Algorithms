import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
    private Node root;
    private int size;
    private RectHV rectHV;

    private class Node {
        private double x;
        private double y;
        private Node left;
        private Node right;
        private boolean isVertical;

        public Node(double x, double y, Node left, Node right, boolean isVertical) {
            this.x = x;
            this.y = y;
            this.left = left;
            this.right = right;
            this.isVertical = isVertical;
        }
    }

    public KdTree() {
        size = 0;
        root = null;
        rectHV = new RectHV(0, 0, 1, 1);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        this.root = insertNode(root, p, true);
    }

    private Node insertNode(Node node, Point2D p, boolean isVertical) {
        if (node == null) {
            size++;
            return new Node(p.x(), p.y(), null, null, isVertical);
        }

        if (node.x == p.x() && node.y == p.y()) {
            return node;
        }

        if (node.isVertical && p.x() < node.x || !node.isVertical && p.y() < node.y) {
            node.left = insertNode(node.left, p, !isVertical);
        } else {
            node.right = insertNode(node.right, p, !isVertical);
        }

        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return containsNode(root, p.x(), p.y());
    }

    private boolean containsNode(Node node, double x, double y) {
        if (node == null) {
            return false;
        }

        if (node.x == x && node.y == y) {
            return true;
        }

        if (node.isVertical && x < node.x || !node.isVertical && y < node.y) {
            return containsNode(node.left, x, y);
        } else {
            return containsNode(node.right, x, y);
        }
    }

    public void draw() {
        StdDraw.setScale(0, 1);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        rectHV.draw();

        drawWhole(root, rectHV);
    }

    private void drawWhole(Node node, RectHV rect) {
        if (node == null) {
            return;
        }

        // Draw points.
        StdDraw.setPenColor();
        StdDraw.setPenRadius(0.01);
        new Point2D(node.x, node.y).draw();

        // Find the min and max of the division line.
        Point2D min, max;
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            min = new Point2D(node.x, rect.ymin());
            max = new Point2D(node.x, rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            min = new Point2D(rect.xmin(), node.y);
            max = new Point2D(rect.xmax(), node.y);
        }

        // Draw the division line.
        StdDraw.setPenRadius();
        min.drawTo(max);

        // Recursive call for drawing the children node.
        drawWhole(node.left, leftRect(rect, node));
        drawWhole(node.right, rightRect(rect, node));
    }

    private RectHV leftRect(RectHV rect, Node node) {
        if (node.isVertical) {
            return new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());
        } else {
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);
        }
    }

    private RectHV rightRect(RectHV rect, Node node) {
        if (node.isVertical) {
            return new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            return new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Queue<Point2D> queue = new Queue<>();
        range(root, rectHV, rect, queue);

        return queue;
    }

    private void range(Node node, RectHV initialRect, RectHV givenRect, Queue<Point2D> q) {
        if (node == null) {
            return;
        }

        if (givenRect.intersects(initialRect)) {
            Point2D p = new Point2D(node.x, node.y);
            if (givenRect.contains(p)) {
                q.enqueue(p);
            }

            range(node.left, leftRect(initialRect, node), givenRect, q);
            range(node.right, rightRect(initialRect, node), givenRect, q);
        }
    }

    public Point2D nearest(Point2D p) {
        return nearestPoint(root, rectHV, p.x(), p.y(), null);
    }

    private Point2D nearestPoint(Node node, RectHV rect, double x, double y, Point2D temp) {
        if (node == null) {
            return temp;
        }

        double distanceQueryToNearest = 0.0;
        double distanceRectToQuery = 0.0;
        Point2D query = new Point2D(x, y);
        RectHV leftRect = null;
        RectHV rightRect = null;
        Point2D nearest = temp;

        if (nearest != null) {
            distanceQueryToNearest = query.distanceSquaredTo(nearest);
            distanceRectToQuery = rect.distanceSquaredTo(query);
        }

        if (nearest == null || distanceQueryToNearest > distanceRectToQuery) {
            Point2D point = new Point2D(node.x, node.y);
            if (nearest == null || distanceQueryToNearest > query.distanceSquaredTo(point)) {
                nearest = point;
            }

            if (node.isVertical) {
                leftRect = new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());
                rightRect = new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());

                if (x < node.x) {
                    nearest = nearestPoint(node.left, leftRect, x, y, nearest);
                    nearest = nearestPoint(node.right, rightRect, x, y, nearest);
                } else {
                    nearest = nearestPoint(node.right, rightRect, x, y, nearest);
                    nearest = nearestPoint(node.left, leftRect, x, y, nearest);
                }
            } else {
                leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);
                rightRect = new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());

                if (y < node.y) {
                    nearest = nearestPoint(node.left, leftRect, x, y, nearest);
                    nearest = nearestPoint(node.right, rightRect, x, y, nearest);
                } else {
                    nearest = nearestPoint(node.right, rightRect, x, y, nearest);
                    nearest = nearestPoint(node.left, leftRect, x, y, nearest);
                }
            }
        }
        return nearest;
    }
}
