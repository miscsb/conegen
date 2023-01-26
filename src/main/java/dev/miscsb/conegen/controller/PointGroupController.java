package dev.miscsb.conegen.controller;

import java.awt.Color;

import dev.miscsb.conegen.transformations.Transformation;
import dev.miscsb.conegen.util.Point3D;

public class PointGroupController {

    protected Point3D[] points;
    protected int[][] edges;
    protected Color color;
    protected Transformation transformation; 

    public PointGroupController(Point3D[] points, int[][] edges, Color color) {
        this.points = points;
        this.edges = edges;
        this.color = color;
        this.transformation = null;
    }

    public Point3D[] getPoints() {
        return points;
    }

    public int[][] getEdges() {
        return edges;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    public void applyAll() {
        if (this.transformation == null) return;
        for (int i = 0; i < 8; i++) {
            this.points[i] = this.transformation.transform(this.points[i]);
        }
    }

    public void applyAll(Transformation transformation) {
        if (transformation == null) return;
        for (int i = 0; i < 8; i++) {
            this.points[i] = transformation.transform(this.points[i]);
        }
    }

}
