package dev.miscsb.conegen.controller;

import java.awt.Color;

import dev.miscsb.conegen.util.Point3D;
import dev.miscsb.conegen.util.Quaternion;
import dev.miscsb.conegen.util.QuaternionUtil;

public class CircleController extends PointGroupController {

    public CircleController(double radius, double[] normal, int numEdges, Color color) {
        super(new Point3D[numEdges], new int[numEdges][2], color);
        Quaternion transformation = QuaternionUtil.getRotationQuaternion(new double[] {0, 1, 0}, normal);
        for (int i = 0; i < numEdges; i++) {
            double angle = (2 * Math.PI * i) / numEdges;
            Point3D point = new Point3D(radius * Math.cos(angle), 0, radius * Math.sin(angle));
            point = QuaternionUtil.applyRotationQuaternion(point, transformation);
            this.points[i] = point;
            this.edges[i][0] = i;
            this.edges[i][1] = (i + 1) % numEdges;
        }
    }
    
}
