package dev.miscsb.conegen.controller;

import java.util.ArrayList;
import java.util.List;

import dev.miscsb.conegen.transformations.Transformation;
import dev.miscsb.conegen.util.Geo3DUtil;
import dev.miscsb.conegen.util.Point3D;

public class PointGroup {

    protected List<Point3D> vertices;
    protected List<int[]> edges;
    protected Point3D center;

    public PointGroup(List<Point3D> vertices, List<int[]> edges, Point3D center) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = edges;
        this.center = center;
    }

    public PointGroup(List<Point3D> vertices, List<int[]> edges) {
        this(vertices, edges, Geo3DUtil.average(vertices));
    }

    public List<Point3D> getVertices() {
        return vertices;
    }

    public List<int[]> getEdges() {
        return edges;
    }

    public void transform(Transformation transformation) {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.set(i, transformation.transform(this.vertices.get(i)));
        }
    }

    public void transformAboutCenter(Transformation transformation) {
        transformation = transformation.about(this.center);
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.set(i, transformation.about(this.center).transform(this.vertices.get(i)));
        }
    }

    public PointGroup transformed(Transformation transformation) {
        PointGroup transformed = new PointGroup(List.copyOf(vertices), List.copyOf(edges));
        transformed.transform(transformation);
        return transformed;
    }

    public PointGroup transformedAboutCenter(Transformation transformation) {
        PointGroup transformed = new PointGroup(List.copyOf(vertices), List.copyOf(edges));
        transformed.transformAboutCenter(transformation);
        return transformed;
    }

}
