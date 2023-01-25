package dev.miscsb.conegen.controller;

import java.awt.Color;

import dev.miscsb.conegen.transformations.Transformation;
import dev.miscsb.conegen.util.Point3D;

public class CubeController extends ShapeController {
//
//  p5 --->  ____________ <--- p6   |          +y ^  / +z
//          /|          /|          |             | /
// p8 ---> /_|_________/_| <--- p7  |  -x         |/         +x
//         | |         | |          |   <---------+---------->
//    p1 --+>|_________|_| <--- p2  |            /|
//         | /         | /          |           / |
// p4 ---> |/__________|/ <--- p3   |       -z /  v -y
//     
    private Point3D[] points;
    private Point3D center;

    private Color color;

    private int[][] edges = {
        {0, 1}, {0, 3}, {0, 4}, {1, 2}, {1, 5}, {2, 3},
        {2, 6}, {3, 7}, {4, 5}, {4, 7}, {5, 6}, {6, 7}
    };

    private Transformation transformation;

    public CubeController(double d) {
        this.points = new Point3D[8];
        this.points[0] = new Point3D(-d, -d, d);
        this.points[1] = new Point3D(d, -d, d);
        this.points[2] = new Point3D(d, -d, -d);
        this.points[3] = new Point3D(-d, -d, -d);
        this.points[4] = new Point3D(-d, d, d);
        this.points[5] = new Point3D(d, d, d);
        this.points[6] = new Point3D(d, d, -d);
        this.points[7] = new Point3D(-d, d, -d);
        this.center = new Point3D(0, 0, 0);
    }

    @Override
    public Point3D[] getPoints() {
        return this.points;
    }

    @Override
    public int[][] getEdges() {
        return this.edges;
    }

    @Override
    public void applyAll() {
        for (int i = 0; i < 8; i++) {
            this.points[i] = this.transformation.transform(this.points[i]);
        }
        this.center = this.transformation.transform(this.center);
    }

    @Override
    public Point3D getCenter() {
        return this.center;
    }

    @Override
    public void setTransformation(Transformation t) {
        this.transformation = t;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color c) {
        this.color = c;
    }
}
