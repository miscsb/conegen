package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;

public record Translation(double xd, double yd, double zd) implements Transformation {
    @Override
    public Point3D transform(Point3D p) {
        return new Point3D(
            p.x + xd,
            p.y + yd,
            p.z + zd
        );
    }
}
