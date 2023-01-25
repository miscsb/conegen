package dev.miscsb.conegen.util;

/**
 * Utility class for dealing with Quaternions and point rotations.
 * @author antimony
 */
public class QuaternionUtil {
    /**
     * Converts the Axis-Angle representation of a rotation to a rotation Quaternion.
     * @param theta the angle
     * @param x the x-coordinate of the unit vector
     * @param y the y-coordinate of the unit vector
     * @param z the z-coordinate of the unit vector
     * @return the equivalent rotation Quaternion
     */
    public static Quaternion axisAngleToQuaternion(double theta, double x, double y, double z) {
        double norm = MatrixUtil.length(new double[] {x, y, z});
        double sinht = Math.sin(theta / 2);
        return new Quaternion(
            Math.cos(theta / 2),
            x * sinht / norm,
            y * sinht / norm,
            z * sinht / norm
        );
    }

    /**
     * Converts the Axis-Angle representation of a rotation to a rotation Quaternion.
     * @param theta the angle
     * @param vector the unit vector
     * @return the equivalent rotation Quaternion
     * @throws IllegalArgumentException if the provided vector is not a unit vector
     */
    public static Quaternion getRotationQuaternion(double theta, double[] vector) {
        return axisAngleToQuaternion(theta, vector[0], vector[1], vector[2]);
    }

    /**
     * Applies a rotation quaternion (active rotation) to a point.
     * @param p the point to rotate
     * @param q the rotation quaternion to apply
     * @return the transformed quaternion
     */
    public static Point3D applyRotationQuaternion(Point3D p, Quaternion q) {
        // active rotation
        Quaternion qp = Quaternion.multiply(
            Quaternion.inverse(q),
            new Quaternion(0, p.toArray()),
            q
        );
        return extractCoordinates(qp);
    }

    /**
     * Extracts the coordinates from a point quaternion by taking its vector part.
     * @param q the quaternion to extract the point from
     * @return the extracted point
     */
    public static Point3D extractCoordinates(Quaternion q) {
        return new Point3D(
            q.get(1),
            q.get(2),
            q.get(3)
        );
    }

    /**
     * Converts a quaternion to its equivalent Axis-Angle representation.
     * @param q the quaternion to convert
     * @return the equivalent Axis-Angle representation {theta, x, y, z}
     */
    public static double[] quaternionToAxisAngle(Quaternion q) {
        double rotation = 2 * Math.acos(q.get(0));
        double sinht = Math.sin(rotation / 2);
        if (Math.abs(rotation) <= 1E-9) {
            return new double[] {0, 1, 0, 0};
        }
        return new double[] {
            rotation,
            q.get(1) / sinht,
            q.get(2) / sinht,
            q.get(3) / sinht
        };
    }
}
