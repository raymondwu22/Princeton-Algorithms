import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] copyPoints;
    private ArrayList<LineSegment> lines;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException("points is null.");
        }

        copyPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copyPoints[i] = points[i];
        }

        for (int i = 0; i < copyPoints.length; i++) {
            if (copyPoints[i] == null) {
                throw new IllegalArgumentException("argument is null");
            }
        }

        Arrays.sort(copyPoints);

        for (int i = 0; i < copyPoints.length - 1; i++) {
            if (copyPoints[i].compareTo(copyPoints[i + 1]) == 0) {
                throw new java.lang.IllegalArgumentException("There exits repeated points!");
            }
        }

        lines = new ArrayList<>();

        for (Point p : points) {
            Arrays.sort(copyPoints, p.slopeOrder()); // now copyPoints[0] is point itself.

            int startIndex = 1;
            int count;
            double targetSlope;

            for (int i = startIndex; i < copyPoints.length - 2;) {
                count = 0;
                targetSlope = p.slopeTo(copyPoints[startIndex]);
                while (i < copyPoints.length && p.slopeTo(copyPoints[i]) == targetSlope) {
                    count++;
                    i++;
                }

                if (count >= 3) {
                    Arrays.sort(copyPoints, startIndex, i);
                    if (p.compareTo(copyPoints[startIndex]) < 0) {
                        lines.add(new LineSegment(p, copyPoints[startIndex + count - 1]));
                    }
                }
                startIndex = i;
            }
        }
    }

    public int numberOfSegments() {
        return lines.size();
    }

    public LineSegment[] segments() {
        return lines.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        StdOut.println("Points read from input.txt:");
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            points[i] = p;
            StdOut.print(p + "  ");
        }

        StdOut.println("\n");
        StdOut.println("Result:");

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
