package dev.miscsb.conegen.controller;

import java.awt.Color;

import dev.miscsb.conegen.transformations.Transformation;
import dev.miscsb.conegen.util.Point3D;

public class PointGroup extends ShapeController {

//         +y ^  / +z
//            | /
// -x         |/         +x
//  <---------+---------->
//           /|
//          / |
//      -z /  v -y
    
    private Point3D[] points;
    private Point3D center;
    private int[][] edges;

    private Color color;

    private Transformation transformation;

    public PointGroup(Point3D[] points, int[][] edges, Point3D center) {
        this.points = points;
        this.edges  = edges;
        this.center = center;
        this.color = Color.WHITE;
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
        for (int i = 0; i < this.points.length; i++) {
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
