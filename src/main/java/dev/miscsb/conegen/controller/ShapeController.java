package dev.miscsb.conegen.controller;

import java.awt.Color;

import dev.miscsb.conegen.transformations.Transformation;
import dev.miscsb.conegen.util.Point3D;

public abstract class ShapeController {
    public abstract Point3D[] getPoints();
    public abstract int[][] getEdges();
    public abstract Point3D getCenter();

    public abstract void setTransformation(Transformation t);
    public abstract void applyAll();
    public abstract Color getColor();
    public abstract void setColor(Color c);
}
