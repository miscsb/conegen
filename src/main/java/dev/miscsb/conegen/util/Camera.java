package dev.miscsb.conegen.util;

public class Camera {
    public Point3D pinhole, surface;
    public Quaternion orientation;

    public Camera(Point3D pinhole, Point3D surface, Quaternion orientation) {
        this.pinhole = pinhole;
        this.surface = surface;
        this.orientation = orientation;
    }

    public Camera(Point3D pinhole, double focalLength, Quaternion orientation) {
        this(pinhole, new Point3D(0, 0, focalLength), orientation);
    }

    public Camera(Point3D pinhole, Quaternion orientation) {
        this(pinhole, 1, orientation);
    }

    public Point2D projectPoint(Point3D worldCoordinates) {
        Point3D cameraCoordinates = QuaternionUtil.applyRotationQuaternion(
            Geo3DUtil.pointSubtract(worldCoordinates, pinhole), 
            Quaternion.scale(orientation, -1));
        double factor = surface.z/cameraCoordinates.z;
        return (cameraCoordinates.z > 0) ? new Point2D(
            factor * cameraCoordinates.x + surface.x,
            factor * cameraCoordinates.y + surface.y
        ) : new Point2D(
            -factor * cameraCoordinates.x + surface.x,
            -factor * cameraCoordinates.y + surface.y
        );
    }
}
