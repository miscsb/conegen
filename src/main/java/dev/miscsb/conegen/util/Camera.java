package dev.miscsb.conegen.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Camera {
    public Point3D pinhole;
    public Quaternion orientation;
    public double focalLength;

    public Camera(Point3D pinhole, Quaternion orientation, double focalLength) {
        this.pinhole = pinhole; 
        this.focalLength = focalLength;
        this.orientation = orientation;
    }

    public Camera(Point3D pinhole, Quaternion orientation) {
        this(pinhole, orientation, 1);
    }

    public Point2D projectPoint(Point3D worldCoordinates) {
        Point3D cameraCoordinates = QuaternionUtil.applyRotationQuaternion(
            Geo3DUtil.pointSubtract(worldCoordinates, pinhole), 
            Quaternion.scale(orientation, -1));
        double factor = focalLength/cameraCoordinates.z;
        return new Point2D(
            factor * cameraCoordinates.x,
            factor * cameraCoordinates.y
        );
    }

    /**
     * Returns a 2d array of lines, of form {x1, y1, x2, y2}.
     * @param worldCoordinates
     * @return
     */
    public List<double[]> projectLines(Point3D[] worldCoordinates, int[][] edges) {
        List<Point3D> cameraCoordinates = Arrays.stream(worldCoordinates)
            .map(wcd -> Geo3DUtil.pointSubtract(wcd, pinhole))
            .map(wcd -> QuaternionUtil.applyRotationQuaternion(wcd, Quaternion.inverse(orientation)))
            .toList();
        
        List<double[]> projectedEdges = new ArrayList<>();
        for (int[] edge : edges) {
            Point3D p1 = cameraCoordinates.get(edge[0]);
            Point3D p2 = cameraCoordinates.get(edge[1]);

            double d1 = p1.z - 0.01;
            double d2 = p2.z - 0.01;
            if (p1.z < 0) {
                if (p2.z < 0) continue;
                p1 = Geo3DUtil.pointScale(Geo3DUtil.pointSubtract(Geo3DUtil.pointScale(p2, d1), Geo3DUtil.pointScale(p1, d2)), 1/(p1.z-p2.z));
            } else if (p2.z < 0 /* && p1.z >= 0 */) {
                p2 = Geo3DUtil.pointScale(Geo3DUtil.pointSubtract(Geo3DUtil.pointScale(p1, d2), Geo3DUtil.pointScale(p2, d1)), 1/(p2.z-p1.z));
            }

            projectedEdges.add(new double[] {p1.x/p1.z*focalLength, p1.y/p1.z*focalLength, p2.x/p2.z*focalLength, p2.y/p2.z*focalLength});
        }
        return projectedEdges;
    }

    @Override
    public String toString() {
        return String.format("[pinhole:%s, orientation:%s, focalLength:%.3f]", pinhole.toString(), orientation.toString(), focalLength);
    }
}
