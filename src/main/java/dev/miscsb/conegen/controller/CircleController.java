package dev.miscsb.conegen.controller;

import java.awt.Color;

import dev.miscsb.conegen.util.Point3D;

public class CircleController extends PointGroupController {

    public CircleController(double radius, double[] normal, int numEdges, Color color) {
        super(new Point3D[numEdges], new int[numEdges][2], color);
        for (int i = 0; i < numEdges; i++) {
            this.points[i] = new Point3D(radius, numEdges, i);
        }
    }
    
}
