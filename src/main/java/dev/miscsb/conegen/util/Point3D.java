package dev.miscsb.conegen.util;

/**
 * Class representing a point in 3 dimensions
 * @author antimony
 */
public class Point3D {

    public static final Point3D ORIGIN = new Point3D(0, 0, 0);

    public double x, y, z;

    /**
     * Constructs a new {@code Point3D} from the given coordinates.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Converts this point into an equivalent vector of length 3.
     * @return the vector representation of this point
     */
    public double[] toArray() {
        return new double[] {
            this.x,
            this.y,
            this.z
        };
    }

    public String toString() {
        return String.format("(%.3f, %.3f, %.3f)", this.x, this.y, this.z);
    }
}
