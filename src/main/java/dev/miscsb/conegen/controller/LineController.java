package dev.miscsb.conegen.controller;

import java.awt.Color;

import dev.miscsb.conegen.util.Point3D;

public class LineController extends PointGroupController {

    public LineController(Point3D p1, Point3D p2, Color color) {
        super(new Point3D[] { p1, p2 }, new int[][] {{0, 1}}, color);
    }
    
}
