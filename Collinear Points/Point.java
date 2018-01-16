import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int compareTo(Point that) {
        if ((this.x == that.x) && (this.y == that.y)) {
            return 0;
        } else if ((that.y > this.y) || (that.y == this.y) && (that.x > this.x)) {
            return -1;
        } else {
            return 1;
        }
    }

    public double slopeTo(Point that) {
        if ((this.x == that.x) && (this.y == that.y)) {
            return Double.NEGATIVE_INFINITY;
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        } else if (this.y == that.y) {
            return 0;
        } else {
            return (double) (that.y - this.y) / (that.x - this.x);
        }
    }

    public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
    }

    private class SlopeOrder implements Comparator<Point> {


        @Override
        public int compare(Point p1, Point p2) {
            double slope1 = slopeTo(p1);
            double slope2 = slopeTo(p2);

            if (slope1 < slope2) {
                return -1;
            } else if (slope1 > slope2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {

    }
}
