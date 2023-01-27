package dev.miscsb.conegen.util;

public class Geo3DUtil {
    public static double[] directionVector(Point3D anchor, Point3D tip) {
        return MatrixUtil.vectorSubtract(tip.toArray(), anchor.toArray());
    }

    public static Point3D pointSubtract(Point3D p1, Point3D p2) {
        return new Point3D(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
    }

    // ax + by + cz = d
    public static double[] perpendicularPlane(Point3D point, double[] direction) {
        double a = direction[0];
        double b = direction[1];
        double c = direction[2];
        return new double[] {a, b, c, a*point.x + b*point.y + c*point.z};
    }

    public static Point3D intersectionLinePlane(Point3D p1, Point3D p2, double[] plane) {
        double[] dir = directionVector(p1, p2);
        double a = plane[0];
        double b = plane[1];
        double c = plane[2];
        double u = (plane[3] - a*p1.x - b*p1.y - c*p1.z) / (a*dir[0] + b*dir[1] + c*dir[2]);
        return new Point3D(p1.x + u*dir[0], p1.y + u*dir[1], p1.z + u*dir[2]);
    }

    // [x, y, z] -> clockwise rotation on xz about +y from +z
    public static double yaw(double[] dir) {
        double x = dir[0];
        double z = dir[2];
        if (x == 0) return (z > 0) ? 0 : Math.PI;
        double theta = Math.atan(z/x);
        if (x > 0) return Math.PI / 2 - theta;
        if (z > 0) return theta;
        return Math.PI / 2 + theta;
    }

    // [x, y, z] -> counterclockwise rotation on a line on xz about the line perpendicular
    public static double pitch(double[] dir) {
        double y = dir[1];
        double d = Math.sqrt(dir[0]*dir[0] + dir[2]*dir[2]);
        if (d == 0) return (y > 0) ? Math.PI / 2 : -Math.PI / 2;
        return Math.atan(y/d);
    }

    public static Point3D pointScale(Point3D p, double k) {
        return new Point3D(k * p.x, k * p.y, k * p.z);
    }
}
