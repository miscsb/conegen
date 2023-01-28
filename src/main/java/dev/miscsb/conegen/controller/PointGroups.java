package dev.miscsb.conegen.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import dev.miscsb.conegen.util.Point3D;
import dev.miscsb.conegen.util.Quaternion;
import dev.miscsb.conegen.util.QuaternionUtil;

public class PointGroups {
    
    public static PointGroup line(Point3D p1, Point3D p2) {
        return new PointGroup(List.of(p1, p2), List.of(new int[] {0, 1}));
    }

    public static PointGroup circle(double radius, double[] normal, int numEdges) {
        Quaternion transformation = QuaternionUtil.getRotationQuaternion(new double[] {0, 1, 0}, normal);
        List<Point3D> vertices = IntStream.range(0, numEdges)
            .mapToDouble(i -> i*2*Math.PI/numEdges)
            .mapToObj(angle -> new Point3D(radius * Math.cos(angle), 0, radius * Math.sin(angle)))
            .map(point -> QuaternionUtil.applyRotationQuaternion(point, transformation))
            .toList();
        List<int[]> edges = IntStream.range(0, numEdges).mapToObj(i -> new int[] { i, (i + 1) % numEdges}).toList();
        return new PointGroup(vertices, edges);
    }

    /*
     *   p5 --->  ____________ <--- p6   |          +y ^  / +z
     *           /|          /|          |             | /
     *  p8 ---> /_|_________/_| <--- p7  |  -x         |/         +x
     *          | |         | |          |   <---------+---------->
     *     p1 --+>|_________|_| <--- p2  |            /|
     *          | /         | /          |           / |
     *  p4 ---> |/__________|/ <--- p3   |       -z /  v -y
     */

    public static PointGroup cube(double d) {
        return rectangularPrism(d, d, d);
    }

    public static PointGroup rectangularPrism(double a, double b, double c) {
        return new PointGroup(List.of(
            new Point3D(-a,-b, c),
            new Point3D( a,-b, c),
            new Point3D( a,-b,-c),
            new Point3D(-a,-b,-c),
            new Point3D(-a, b, c),
            new Point3D( a, b, c),
            new Point3D( a, b,-c),
            new Point3D(-a, b,-c)
        ), Arrays.asList(new int[][] {
            {0, 1}, {0, 3}, {0, 4}, {1, 2}, {1, 5}, {2, 3},
            {2, 6}, {3, 7}, {4, 5}, {4, 7}, {5, 6}, {6, 7}
        }));
    }

    // stitch format: { groupIndex1, pointIndex1, groupIndex2, pointIndex2 }
    public static PointGroup compose(List<PointGroup> groups, List<int[]> stitches) {
        List<Point3D> vertices = groups.stream().map(PointGroup::getVertices).flatMap(List::stream).toList();
        
        int[] offsets = new int[groups.size()];
        for (int i = 0; i < offsets.length - 1; i++) {
            offsets[i + 1] = offsets[i] + groups.get(i).getVertices().size();
        }

        List<int[]> edges = new ArrayList<>();
        for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
            PointGroup group = groups.get(groupIndex);
            final int groupOffset = offsets[groupIndex];
            group.getEdges().stream()
                .map(edge -> new int[] {edge[0] + groupOffset, edge[1] + groupOffset})
                .forEach(edges::add);
        }

        for (int[] stitch : stitches) {
            int i1 = offsets[stitch[0]] + stitch[1];
            int i2 = offsets[stitch[2]] + stitch[3];
            edges.add(new int[] { i1, i2 });
        }
        
        return new PointGroup(vertices, edges);
    }

}
