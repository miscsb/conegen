package dev.miscsb.conegen.util;

public class Camera {
    public Point3D pinhole;
    public double focalLength;
    public Quaternion orientation;

    public Camera(Point3D pinhole, double focalLength, Quaternion orientation) {
        this.pinhole = pinhole; 
        this.focalLength = focalLength;
        this.orientation = orientation;
    }

    public Camera(Point3D pinhole, Quaternion orientation) {
        this(pinhole, 1, orientation);
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

    @Override
    public String toString() {
        return String.format("[Camera: pinhole=%s, focalLength=%.3f, orientation=%s]", pinhole.toString(), focalLength, orientation.toString());
    }
}
