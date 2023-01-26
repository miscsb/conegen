package dev.miscsb.conegen.util;

/**
 * Class representing a point in 2 dimensions
 * @author antimony
 */
public class Point2D {

    public static final Point2D ORIGIN = new Point2D(0, 0);

    public double x, y;

    /**
     * Constructs a new {@code Point3D} from the given coordinates.
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Converts this point into an equivalent vector of length 2.
     * @return the vector representation of this point
     */
    public double[] toArray() {
        return new double[] {
            this.x,
            this.y,
        };
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f)", this.x, this.y);
    }
}
