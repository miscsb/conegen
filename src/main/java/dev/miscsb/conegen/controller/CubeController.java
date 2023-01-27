package dev.miscsb.conegen.controller;

import java.awt.Color;

import dev.miscsb.conegen.util.Point3D;

public class CubeController extends PointGroupController {
//
//  p5 --->  ____________ <--- p6   |          +y ^  / +z
//          /|          /|          |             | /
// p8 ---> /_|_________/_| <--- p7  |  -x         |/         +x
//         | |         | |          |   <---------+---------->
//    p1 --+>|_________|_| <--- p2  |            /|
//         | /         | /          |           / |
// p4 ---> |/__________|/ <--- p3   |       -z /  v -y
//     

    private static final int[][] EDGES = {
        {0, 1}, {0, 3}, {0, 4}, {1, 2}, {1, 5}, {2, 3},
        {2, 6}, {3, 7}, {4, 5}, {4, 7}, {5, 6}, {6, 7}
    };

    public CubeController(double d, Color color) {
        this(d, d, d, color);
    }

    public CubeController(double a, double b, double c, Color color) {
        super(new Point3D[] {
            new Point3D(-a, -b, c),
            new Point3D(a, -b, c),
            new Point3D(a, -b, -c),
            new Point3D(-a, -b, -c),
            new Point3D(-a, b, c),
            new Point3D(a, b, c),
            new Point3D(a, b, -c),
            new Point3D(-a, b, -c),
        }, EDGES, color);
    }
    
}
