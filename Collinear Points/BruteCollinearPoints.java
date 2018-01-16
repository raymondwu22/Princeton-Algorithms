import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int segmentsNumber;
    private Node last;
    private Point[] copyPoints;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }

        this.segmentsNumber = 0;

        copyPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copyPoints[i] = points[i];
        }

        for (int i = 0; i < copyPoints.length; i++) {
            if (copyPoints[i] == null) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Arrays.sort(copyPoints);
        for (int i = 0; i < copyPoints.length - 1; i++) {
            if (copyPoints[i].compareTo(copyPoints[i + 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        for (int i = 0; i < copyPoints.length; i++) {
            for (int j = i + 1; j < copyPoints.length; j++) {
                for (int m = j + 1; m < copyPoints.length; m++) {
                    for (int n = m + 1; n < copyPoints.length; n++) {
                        double slope1 = copyPoints[i].slopeTo(copyPoints[j]);
                        double slope2 = copyPoints[j].slopeTo(copyPoints[m]);
                        double slope3 = copyPoints[m].slopeTo(copyPoints[n]);

                        if (slope1 == slope2 && slope2 == slope3) {
                            if (last != null) {
                                Node newNode = new Node();
                                newNode.prev = last;
                                newNode.value = new LineSegment(copyPoints[i], copyPoints[n]);
                                last = newNode;
                            } else {
                                last = new Node();
                                last.value = new LineSegment(copyPoints[i], copyPoints[n]);
                            }

                            segmentsNumber++;
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return segmentsNumber;
    }

    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[segmentsNumber];
        Node current = last;

        for (int i = 0; i < segmentsNumber; i++) {
            lineSegments[i] = current.value;
            current = current.prev;
        }

        return lineSegments;
    }

    private class Node {
        private LineSegment value;
        private Node prev;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
